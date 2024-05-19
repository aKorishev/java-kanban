package taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileBackedTaskManagerTest {
    @Test
    void loadFromFile() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);


            Epic epic = new Epic("epic1","epic1desc");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("epic2","epic2desc");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("subtask1","subtask1desc", epic1Id));
            taskManager.createSubTask(new SubTask("subtask2","subtask2desc", epic12d));
            taskManager.createSubTask(new SubTask("subtask3","subtask3desc", epic1Id));
            taskManager.createSubTask(new SubTask("subtask4","subtask4desc", epic1Id));
            taskManager.createSubTask(new SubTask("subtask5","subtask5desc", epic12d));
            taskManager.createSubTask(new SubTask("subtask6","subtask6desc", epic12d));
            taskManager.createSubTask(new SubTask("subtask7","subtask7desc", epic12d));

            SubTask subTask = epic.getSubTasks().getFirst();
            subTask.setDuration(Duration.ofHours(10));
            subTask.setStartTime(Optional.of(LocalDateTime.of(2024,01,01,02,43,50)));
            subTask.doProgress();
            epic.calcTaskDuration();
            taskManager.refreshSortedMap();
            int controlTimeEpicId = epic.getTaskId();

            taskManager.createTask(new Task("task1","task1desc"));
            taskManager.createTask(new Task("task2","task2desc"));
            taskManager.createTask(new Task("task3","task3desc"));
            taskManager.createTask(new Task("task4","task4desc"));
            taskManager.createTask(new Task("task5","task5desc"));
            taskManager.createTask(new Task("task6","task6desc"));

            epic = new Epic("epic3","epic3desc");
            taskManager.createEpic(epic);

            TaskManager loadedTaskManager = TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Map<Integer, Task> loadedTasks = loadedTaskManager.getTasks().stream().collect(Collectors.toMap(Task::getTaskId, t -> t));

            for (Task task : taskManager.getTasks()) {
                Task loadedTask = loadedTasks.get(task.getTaskId());
                Assertions.assertNotNull(loadedTask, "Не найдена Task: " + task);

                Assertions.assertTrue(task.equals(loadedTask), "Различаются Tasks. \nExpected: " + task + "\nActual: " + loadedTasks);
            }

            Map<Integer, Epic> loadedEpics = loadedTaskManager.getEpics().stream().collect(Collectors.toMap(Task::getTaskId, t -> t));

            for (Epic oldEpic : taskManager.getEpics()) {
                int epicId = oldEpic.getTaskId();
                Epic loadedEpic = loadedEpics.get(epicId);
                Assertions.assertNotNull(loadedEpic, "Не найдена Epic: " + oldEpic);
                Assertions.assertTrue(oldEpic.equals(loadedEpic), "Различаются Epics. \nExpected: " + oldEpic + "\nActual: " + loadedTasks);

                if (epicId == controlTimeEpicId) {
                    loadedEpic.calcTaskDuration();
                    Assertions.assertEquals(oldEpic.getEndTime(), loadedEpic.getEndTime());
                }
            }
        } finally {
            tempFile.delete();
        }
    }
}
