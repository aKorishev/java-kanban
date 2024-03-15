package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

class InMemoryTaskManagerTest {

    @Test
    void getEpics() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));

        Assertions.assertEquals(2, taskManager.getEpics().size());
    }

    @Test
    void getTasks() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        taskManager.createTask(new Task("",""));
        taskManager.createTask(new Task("",""));

        Assertions.assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    void getSubTasksOneEpic() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epic1Id = epic.getTaskId();

        taskManager.createSubTask(new SubTask("","", epic1Id));
        taskManager.createSubTask(new SubTask("","", epic1Id));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        Assertions.assertEquals(2, taskManager.getSubTasks(epic1Id).size());
    }

    @Test
    void testGetSubTasksForSomeEpic() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epic1Id = epic.getTaskId();

        taskManager.createSubTask(new SubTask("","", epic1Id));
        taskManager.createSubTask(new SubTask("","", epic1Id));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        Assertions.assertEquals(3, taskManager.getSubTasks().size());
    }

    @Test
    void getTask() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Task task = new Task("","");
        taskManager.createTask(task);

        Assertions.assertTrue(task.equals(taskManager.getTask(task.getTaskId())));
    }

    @Test
    void getEpic() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        Assertions.assertTrue(epic.equals(taskManager.getEpic(epic.getTaskId())));
    }

    @Test
    void getSubTask() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("","", epic.getTaskId());
        taskManager.createSubTask(subTask);

        Assertions.assertTrue(subTask.equals(taskManager.getSubTask(subTask.getTaskId())));
    }

    @Test
    void clearAllTasks() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        taskManager.createTask(new Task("",""));
        taskManager.createTask(new Task("",""));

        taskManager.clearAllTasks();

        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void clearAllEpics() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));

        taskManager.clearAllEpics();

        Assertions.assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void clearAllSubTasks() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epicId = epic.getTaskId();

        taskManager.createSubTask(new SubTask("","", epicId));
        taskManager.createSubTask(new SubTask("","", epicId));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        taskManager.clearAllSubTasks();

        Assertions.assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    void removeTask() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Task task = new Task("","");
        taskManager.createTask(task);

        int taskId = task.getTaskId();

        taskManager.createTask(new Task("",""));

        taskManager.removeTask(taskId);

        Assertions.assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void removeEpic() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epicId = epic.getTaskId();

        taskManager.createEpic(new Epic("",""));

        taskManager.removeEpic(epicId);

        Assertions.assertEquals(1, taskManager.getEpics().size());
    }
    @Test
    void removeEpicWithSubTasks() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epic1Id = epic.getTaskId();

        taskManager.createSubTask(new SubTask("","", epic1Id));
        taskManager.createSubTask(new SubTask("","", epic1Id));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        int epic2Id = epic.getTaskId();

        taskManager.createSubTask(new SubTask("","", epic2Id));

        taskManager.removeEpic(epic2Id);

        Assertions.assertEquals(2, taskManager.getSubTasks().size());
    }

    @Test
    void removeSubTask() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);
        int epicId = epic.getTaskId();

        SubTask subTask = new SubTask("","", epicId);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(new SubTask("","", epicId));

        epic = new Epic("","");
        taskManager.createEpic(epic);

        taskManager.createSubTask(new SubTask("","", epic.getTaskId()));

        taskManager.removeSubTask(subTask.getTaskId());

        Assertions.assertEquals(2, taskManager.getSubTasks().size());
    }

    @Test
    void updateTask() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Task task = new Task("","");
        taskManager.createTask(task);

        int taskId = task.getTaskId();

        task = new Task("updated task", "");
        task.setTaskId(taskId);

        taskManager.updateTask(task);

        Assertions.assertTrue(taskManager.getTask(taskId).getName().equals("updated task"));
    }

    @Test
    void updateEpic() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);

        int epicId = epic.getTaskId();

        epic = new Epic("updated task", "");
        epic.setTaskId(epicId);

        taskManager.updateEpic(epic);

        Assertions.assertTrue(taskManager.getEpic(epicId).getName().equals("updated task"));
    }

    @Test
    void updateSubTask() throws Exception {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        Epic epic = new Epic("","");
        taskManager.createEpic(epic);


        SubTask subTask = new SubTask("","", epic.getTaskId());
        taskManager.createSubTask(subTask);

        int subTaskId = subTask.getTaskId();

        SubTask updatedSubTask = new SubTask("updated task", "", 1);
        updatedSubTask.setTaskId(subTaskId);

        taskManager.updateSubTask(updatedSubTask);

        Assertions.assertTrue(taskManager.getSubTask(subTaskId).getName().equals("updated task"));
    }

    @Test
    void getHistory() {
        TaskManager taskManager =
                new TaskManagerFactory().initInMemoryTaskManager();

        taskManager.createEpic(new Epic("",""));
        taskManager.createEpic(new Epic("",""));
        taskManager.createTask(new Task("",""));
        taskManager.createTask(new Task("",""));

        for(Task task : taskManager.getTasks())
            taskManager.getTask(task.getTaskId());
        for(Epic epic : taskManager.getEpics())
            taskManager.getEpic(epic.getTaskId());

        Assertions.assertEquals(4, taskManager.getHistory().size());
    }
}