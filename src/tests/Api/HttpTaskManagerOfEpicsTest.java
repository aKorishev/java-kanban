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
import tools.json.EpicTypeAdapter;
import tools.json.SubTaskTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerOfEpicsTest {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SubTask.class, new SubTaskTypeAdapter())
            .registerTypeAdapter(Epic.class, new EpicTypeAdapter())
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
        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/epics")));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var body = response.body();

        List<Epic> tasks = gson.fromJson(body, new TypeToken<List<Epic>>() {}.getType());

        Assertions.assertEquals(3, tasks.size());
    }
    @Test
    void NoFoundTask() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));

        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/epics/10")));

        Assertions.assertEquals(404, response.statusCode(), response.body());
    }
    @Test
    void getTask() throws IOException, InterruptedException, URISyntaxException {
        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));
        var epic = new Epic("","");
        taskManager.createEpic(epic);
        taskManager.createEpic(new Epic("",""));


        taskManager.createSubTask(new SubTask("", "", epic.getTaskId()));
        taskManager.createSubTask(new SubTask("", "", epic.getTaskId()));


        var response = getResponse(HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://127.0.0.1:8080/epics/" + epic.getTaskId())));

        Assertions.assertEquals(200, response.statusCode(), response.body());

        var actualTask = gson.fromJson(response.body(), Epic.class);

        Assertions.assertEquals(epic, actualTask);
    }
    @Test
    void createTask() throws IOException, InterruptedException, URISyntaxException {
        var epic = new Epic("","");
        taskManager.createEpic(epic);

        epic = new Epic("epicNew","");

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(new URI("http://127.0.0.1:8080/epics")));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        Assertions.assertEquals(2, taskManager.getEpics().size());

        var newTask = taskManager.getEpics().get(1);
        epic.setTaskId(newTask.getTaskId());

        Assertions.assertEquals(epic, newTask);
    }
    @Test
    void updateTask() throws Exception {
        var epic = new Epic("","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        epic.setName("updated");

        var response = getResponse(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .uri(new URI("http://127.0.0.1:8080/epics/" + epic.getTaskId())));

        Assertions.assertEquals(201, response.statusCode(), response.body());

        var actualTask = taskManager.getEpic(epic.getTaskId());
        if (actualTask.isEmpty())
            Assertions.fail("не нашел task");

        Assertions.assertTrue(actualTask.get().getName().equals("updated"));
    }


    @Test
    void getPrioritizedList() throws Exception {
        var epic = new Epic("1","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("2","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("3","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("4","");
        res = taskManager.createEpic(epic);
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
                .uri(new URI("http://127.0.0.1:8080/epics/prioritized")));

        Assertions.assertEquals(200, response.statusCode());

        List<Epic> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        var names = new String[] {"4","3","1","2"};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(names[index], item.getName());
            index++;
        }
    }
    @Test
    void getPrioritizedDescList() throws Exception {
        var epic = new Epic("1","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("2","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("3","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("4","");
        res = taskManager.createEpic(epic);
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
                .uri(new URI("http://127.0.0.1:8080/epics/prioritized/-1")));

        Assertions.assertEquals(200, response.statusCode());

        List<Epic> prioritizedList = gson.fromJson(response.body(), new TypeToken<List<Epic>>() {}.getType());
        var names = new String[] {"2","1","3","4"};
        var index = 0;

        for(var item : prioritizedList){
            Assertions.assertEquals(names[index], item.getName());
            index++;
        }
    }

    @Test
    void badRequest() throws Exception {
        var epic = new Epic("1","");
        var res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();

        var subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,5,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("2","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task2","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,6,55));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("3","");
        res = taskManager.createEpic(epic);
        if (res.isPresent())
            throw res.get();
        subTask = new SubTask("task1","desc", epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,12,12,3,5));
        subTask.setDuration(Duration.ofMinutes(1));
        res = taskManager.createSubTask(subTask);
        if (res.isPresent())
            throw res.get();

        epic = new Epic("4","");
        res = taskManager.createEpic(epic);
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
                .uri(new URI("http://127.0.0.1:8080/epics/bad")));

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
