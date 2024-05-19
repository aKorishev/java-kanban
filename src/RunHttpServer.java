import httpserver.HttpTaskServer;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class RunHttpServer {
    public static void main(String[] args) throws IOException {
        var taskManager = TaskManagerFactory.initInMemoryTaskManager();
        var server = new HttpTaskServer(taskManager);

        var epic = new Epic("Первый епик", "");
        //epic.setStartTime(LocalDateTime.of(2024,01,15,12,00));
        //epic.setDuration(Duration.ofHours(2));

        taskManager.createEpic(epic);

        var epicId = epic.getTaskId();

        var subTask = new SubTask("Подзадача1", "", epicId);
        subTask.setStartTime(LocalDateTime.of(2024,05,9,9,02));
        subTask.setDuration(Duration.ofHours(2));

        taskManager.createSubTask(subTask);

        subTask = new SubTask("Подзадача2", "", epicId);
        subTask.setStartTime(LocalDateTime.of(2024,05,9,3,02));
        subTask.setDuration(Duration.ofHours(5));

        taskManager.createSubTask(subTask);

        taskManager.createEpic(new Epic("Второй епик", ""));

        server.start();
    }
}
