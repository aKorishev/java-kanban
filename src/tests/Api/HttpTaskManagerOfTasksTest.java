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

public class HttpTaskManagerOfTasksTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskTypeAdapter())
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
        taskManager.createTask(new Task("", ""));
        taskManager.createTask(new Task("", ""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var body = response.body();

        List<Task> tasks = gson.fromJson(body, new TypeToken<List<Task>>() {}.getType());

        Assertions.assertEquals(2, tasks.size());
    }
    @Test
    void NoFoundTask() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createTask(new Task("", ""));
        taskManager.createTask(new Task("", ""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/10")));

        Assertions.assertEquals(404, response.statusCode(), response.body());
    }
    @Test
    void getTask() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createTask(new Task("", ""));
        taskManager.createTask(new Task("", ""));

        var task = new Task("task", "");

        taskManager.createTask(task);
        taskManager.createTask(new Task("", ""));


        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/" + task.getTaskId())));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var actualTask = gson.fromJson(response.body(), Task.class);

        Assertions.assertEquals(task, actualTask);
    }
    @Test
    void createTask() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        task.setDuration(Duration.ofHours(1));
        task.doProgress();

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(new URI("http://127.0.0.1:8080/tasks")));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        Assertions.assertEquals(1, taskManager.getTasks().size());

        var newTask = taskManager.getTasks().getFirst();
        task.setTaskId(newTask.getTaskId());

        Assertions.assertEquals(task, newTask);
    }

    @Test
    void createCrossedTask() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("task2","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,5,55));
        task.setDuration(Duration.ofHours(1));

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(new URI("http://127.0.0.1:8080/tasks")));

        Assertions.assertEquals(406, response.statusCode(), response.body());

        Assertions.assertEquals(1, taskManager.getTasks().size());
    }
    @Test
    void updateTask() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("task2","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task.setName("updated");

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(new URI("http://127.0.0.1:8080/tasks/" + task.getTaskId())));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        var actualTask = taskManager.getTask(task.getTaskId());
        if (actualTask.isEmpty())
            Assertions.fail("не нашел task");

        Assertions.assertTrue(actualTask.get().getName().equals("updated"));
    }
    @Test
    void updateCrossTask() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/" + task.getTaskId())));

        var updatedTask = gson.fromJson(response.body(), Task.class);
        updatedTask.setStartTime(LocalDateTime.of(2024,12,12,5,55));

        response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updatedTask)))
                .uri(new URI("http://127.0.0.1:8080/tasks/" + updatedTask.getTaskId())));

        Assertions.assertEquals(406, response.statusCode(), response.body());

        Assertions.assertNotEquals(task, updatedTask);
    }


    @Test
    void getPrioritizedList() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,1,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,4,45));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,2,25));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/prioritized")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        List<Task> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        var hours = new Integer[] {1,2,4,6};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(hours[index], item.getStartTime().get().getHour());
            index++;
        }
    }
    @Test
    void getPrioritizedDescList() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,1,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,4,45));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,2,25));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/prioritized/-1")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        List<Task> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {}.getType());
        var hours = new Integer[] {6,4,2,1};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(hours[index], item.getStartTime().get().getHour());
            index++;
        }
    }

    @Test
    void badRequest() throws IOException, InterruptedException, URISyntaxException {
        var task = new Task("task1","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,1,5));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,4,45));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,2,25));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        task = new Task("updated","desc");
        task.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/tasks/bad")));

        Assertions.assertEquals(500, response.statusCode(), response.body());
    }

    @Test
    void getEpicList() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createEpic(new Epic("", ""));
        taskManager.createEpic(new Epic("", ""));
        taskManager.createEpic(new Epic("", ""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/epics")));

        Assertions.assertEquals(200, response.statusCode());

        var body = response.body();

        List<Epic> epics = gson.fromJson(body, new TypeToken<List<Epic>>() {}.getType());

        Assertions.assertEquals(3, epics.size());
    }

    @Test
    void getSubTaskList() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("", "");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));
        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/subtasks")));

        Assertions.assertEquals(200, response.statusCode());

        var body = response.body();

        List<SubTask> subTasks = gson.fromJson(body, new TypeToken<List<SubTask>>() {}.getType());

        Assertions.assertEquals(3, subTasks.size());
    }
    @Test
    void getUnknownTitleResponse() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createTask(new Task("", ""));
        taskManager.createTask(new Task("", ""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/bad")));

        Assertions.assertEquals(404, response.statusCode());
    }

    private HttpResponse<String> getResponse(HttpRequest.Builder requestBuilder) throws IOException, InterruptedException {
        var handler = HttpResponse.BodyHandlers.ofString();

        return HttpClient.newHttpClient().send(
                requestBuilder
                        .build()
                , handler);
    }
}
