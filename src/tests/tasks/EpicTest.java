package tests.tasks;

import enums.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;

import static org.junit.jupiter.api.Assertions.*;


class EpicTest {
    @Test
    void getSubTasks() {
        Epic epic = new Epic("name", "description");


        SubTask subTask = new SubTask("","", 0);
        subTask.setTaskId(10);
        epic.putSubTask(subTask);
        epic.putSubTask(new SubTask("","",0));

        Assertions.assertEquals(2, epic.getSubTasks().size());
    }

    @Test
    void putSubTask() {
        Epic epic = new Epic("name", "description");

        epic.putSubTask(new SubTask("","",0));

        Assertions.assertEquals(1, epic.getSubTasks().size());
    }

    @Test
    void putDoubleSubTask() {
        Epic epic = new Epic("name", "description");


        epic.putSubTask(new SubTask("","", 0));
        epic.putSubTask(new SubTask("","",0));

        Assertions.assertEquals(1, epic.getSubTasks().size());
    }

    @Test
    void removeSubTask() {
        Epic epic = new Epic("name", "description");

        SubTask subTask = new SubTask("","", 0);
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        epic.putSubTask(new SubTask("","",0));

        epic.removeSubTask(10);

        Assertions.assertEquals(1, epic.getSubTasks().size());
    }

    @Test
    void clearSubTasks() {
        Epic epic = new Epic("name", "description");

        SubTask subTask = new SubTask("","", 0);
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        epic.putSubTask(new SubTask("","",0));

        epic.clearSubTasks();

        Assertions.assertEquals(0, epic.getSubTasks().size());
    }

    @Test
    void containsSubTaskId() {
        Epic epic = new Epic("name", "description");

        SubTask subTask = new SubTask("","", 0);
        subTask.setTaskId(10);
        epic.putSubTask(subTask);
        epic.putSubTask(new SubTask("","",0));

        Assertions.assertTrue(epic.containsSubTaskId(10));
    }

    @Test
    void getTaskType() {
        Epic epic = new Epic("name", "description");

        Assertions.assertEquals(TaskType.EPIC, epic.getTaskType());
    }
}