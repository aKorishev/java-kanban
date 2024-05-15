package HttpServer;

import HttpServer.HttpHandlers.*;
import com.sun.net.httpserver.HttpServer;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final TaskManager taskManager;
    private HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(TaskManagerFactory.initInMemoryTaskManager());
        httpTaskServer.start();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0); // связываем сервер с сетевым портом
        httpServer.createContext("/tasks", new TaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager)); // связываем путь и обработчик
        httpServer.createContext("/epics", new EpicHandler(taskManager)); // связываем путь и обработчик
        httpServer.createContext("/history", new HistoryHandler(taskManager)); // связываем путь и обработчик
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager)); // связываем путь и обработчик


        httpServer.start(); // запускаем сервер
    }

    public void stop(){
        httpServer.stop(1);
    }
}
