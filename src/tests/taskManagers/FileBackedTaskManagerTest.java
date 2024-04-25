package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.FileBackedTaskManager;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class FileBackedTaskManagerTest {

    @Test
    void controlLinesInFileAfterAddedEpics() throws IOException {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            taskManager.createEpic(new Epic("", ""));
            taskManager.createEpic(new Epic("", ""));

            Assertions.assertEquals(3, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterAddedTasks() throws IOException {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));

            Assertions.assertEquals(5, getLineFromFile(tempFile));
        }
            finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterAddedSubTasks() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            Assertions.assertEquals(9, getLineFromFile(tempFile));
    }
        finally {
                tempFile.delete();
        }
    }
    @Test
    void controlLinesInFileAfterRemoveOnlyOneEpic() throws IOException {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            taskManager.createEpic(new Epic("", ""));
            taskManager.createEpic(new Epic("", ""));
            taskManager.createEpic(new Epic("", ""));

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            taskManager.removeEpic(epic.getTaskId());

            Assertions.assertEquals(4, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterRemoveTask() throws IOException {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));

            Task task = new Task("","");
            taskManager.createTask(task);

            taskManager.removeTask(task.getTaskId());

            Assertions.assertEquals(3, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterRemoveSubTask() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));

            SubTask subTask = new SubTask("","", epic12d);
            taskManager.createSubTask(subTask);

            taskManager.removeSubTask(subTask.getTaskId());

            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            Assertions.assertEquals(7, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }
    @Test
    void controlLinesInFileAfterRemoveEpic() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            taskManager.removeEpic(epic1Id);

            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            Assertions.assertEquals(8, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterClearAllEpic() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            taskManager.clearAllEpics();

            Assertions.assertEquals(2, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterClearAllTask() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            taskManager.clearAllEpics();

            Assertions.assertEquals(7, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterClearAllSubTask() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            taskManager.clearAllSubTasks();

            Assertions.assertEquals(10, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

    @Test
    void controlLinesInFileAfterClearAllOtherTask() throws Exception {
        File tempFile = File.createTempFile("fileBackedTest", "");

        try {
            TaskManager taskManager =
                    TaskManagerFactory.initFileBackedTaskManager(tempFile);

            Epic epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic1Id = epic.getTaskId();

            epic = new Epic("","");
            taskManager.createEpic(epic);

            int epic12d = epic.getTaskId();

            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic1Id));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));
            taskManager.createSubTask(new SubTask("","", epic12d));

            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));
            taskManager.createTask(new Task("",""));

            epic = new Epic("","");
            taskManager.createEpic(epic);

            taskManager.clearAllEpics();
            taskManager.clearAllTasks();

            Assertions.assertEquals(1, getLineFromFile(tempFile));
        }
        finally {
            tempFile.delete();
        }
    }

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

            for(Task task : taskManager.getTasks()){
                Task loadedTask = loadedTasks.get(task.getTaskId());
                Assertions.assertNotNull(loadedTask, "Не найдена Task: " + task);

                Assertions.assertTrue(task.equals(loadedTask), "Различаются Tasks. \nExpected: " + task + "\nActual: " + loadedTasks);
            }

            Map<Integer, Epic> loadedEpics = loadedTaskManager.getEpics().stream().collect(Collectors.toMap(Task::getTaskId, t -> t));

            for(Epic oldEpic : taskManager.getEpics()){
                Epic loadedEpic = loadedEpics.get(oldEpic.getTaskId());
                Assertions.assertNotNull(loadedEpic, "Не найдена Epic: " + oldEpic);
                Assertions.assertTrue(oldEpic.equals(loadedEpic), "Различаются Epics. \nExpected: " + oldEpic + "\nActual: " + loadedTasks);
            }
        }
        finally {
            tempFile.delete();
        }
    }

    int getLineFromFile(File file) throws IOException {
        try(FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader)){
            int countLines = 0;
            while(buffer.ready()) {
                buffer.readLine();
                countLines++;
            }

            return countLines;
        }
    }
}
