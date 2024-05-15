package tests.Api;

import HttpServer.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tools.json.SubTaskTypeAdapter;
import tools.json.TaskTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerOfSubTasksTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SubTask.class, new SubTaskTypeAdapter())
            .create();

    private TaskManager taskManager;
    private HttpTaskServer taskServer;

    @BeforeEach
    void initTest() throws IOException {
        taskManager = TaskManagerFactory.initInMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    void stopServer(){
        taskServer.stop();
    }

    @Test
    void getList() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        var epicId = epic.getTaskId();

        taskManager.createSubTask(new SubTask("", "", epicId));
        taskManager.createSubTask(new SubTask("", "", epicId));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var body = response.body();

        List<SubTask> tasks = gson.fromJson(body, new TypeToken<List<SubTask>>() {}.getType());

        Assertions.assertEquals(2, tasks.size());
    }
    @Test
    void NoFoundTask() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        var epicId = epic.getTaskId();

        taskManager.createSubTask(new SubTask("", "", epicId));
        taskManager.createSubTask(new SubTask("", "", epicId));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/10")));

        Assertions.assertEquals(404, response.statusCode(), response.body());
    }
    @Test
    void getTask() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        var epicId = epic.getTaskId();

        taskManager.createSubTask(new SubTask("", "", epicId));
        taskManager.createSubTask(new SubTask("", "", epicId));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        epicId = epic.getTaskId();

        var subtask = new SubTask("subTask", "desc", epicId);

        taskManager.createSubTask(new SubTask("", "", epicId));
        taskManager.createSubTask(subtask);
        taskManager.createSubTask(new SubTask("", "", epicId));


        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/" + subtask.getTaskId())));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var actualTask = gson.fromJson(response.body(), SubTask.class);

        Assertions.assertEquals(subtask, actualTask);
    }
    @Test
    void createTask() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        var subtask = new SubTask("subTask", "desc", epic.getTaskId());
        subtask.setStartTime(LocalDateTime.of(2024,12,12,5,5, 34));
        subtask.setDuration(Duration.ofHours(1));
        subtask.doProgress();

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .uri(new URI("http://127.0.0.1:8080/subtasks")));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        Assertions.assertEquals(1, taskManager.getSubTasks().size());

        var newTask = taskManager.getSubTasks().getFirst();
        subtask.setTaskId(newTask.getTaskId());

        Assertions.assertEquals(subtask, newTask);
    }

    @Test
    void createCrossedTask() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofHours(1));
        taskManager.createSubTask(subTask);

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,55));
        subTask.setDuration(Duration.ofHours(1));

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .uri(new URI("http://127.0.0.1:8080/subtasks")));

        Assertions.assertEquals(406, response.statusCode(), response.body());

        Assertions.assertEquals(1, taskManager.getSubTasks().size());
    }
    @Test
    void updateTask() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofHours(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofHours(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask.setName("updated");

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask)))
                .uri(new URI("http://127.0.0.1:8080/subtasks/" + subTask.getTaskId())));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        var actualTask = taskManager.getSubTask(subTask.getTaskId());
        if (actualTask.isEmpty())
            Assertions.fail("не нашел task");

        Assertions.assertTrue(actualTask.get().getName().equals("updated"));
    }
    @Test
    void updateCrossTask() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofHours(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofHours(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/" + subTask.getTaskId())));

        var updatedTask = gson.fromJson(response.body(), SubTask.class);
        updatedTask.setStartTime(LocalDateTime.of(2024,12,12,5,55));

        response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedTask)))
                .uri(new URI("http://127.0.0.1:8080/subtasks/" + updatedTask.getTaskId())));

        Assertions.assertEquals(406, response.statusCode(), response.body());

        Assertions.assertNotEquals(subTask, updatedTask);
    }


    @Test
    void getPrioritizedList() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,2,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/prioritized")));

        Assertions.assertEquals(200, response.statusCode());

        List<SubTask> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
        var hours = new Integer[] {2,3,5,6};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(hours[index], item.getStartTime().get().getHour());
            index++;
        }
    }
    @Test
    void getPrioritizedDescList() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,2,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/prioritized/-1")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        List<SubTask> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {}.getType());
        var hours = new Integer[] {6,5,3,2};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(hours[index], item.getStartTime().get().getHour());
            index++;
        }
    }

    @Test
    void badRequest() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,2,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks/bad")));

        Assertions.assertEquals(500, response.statusCode(), response.body());
    }

    private HttpResponse<String> getResponse(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
        var handler = HttpResponse.BodyHandlers.ofString();

        return HttpClient.newHttpClient().send(
                requestBuilder
                        .build()
                , handler);
    }
}
