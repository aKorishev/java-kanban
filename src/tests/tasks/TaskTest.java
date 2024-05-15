package tests.tasks;

import enums.TaskStatus;
import enums.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class TaskTest {

    @Test
    void getName() {
        Task task = new Task("task name", "description");

        Assertions.assertEquals("task name", task.getName());
    }

    @Test
    void setName() {
        Task task = new Task("task name", "description");

        task.setName("to be");

        Assertions.assertEquals("to be", task.getName());
    }

    @Test
    void setDescription() {
        Task task = new Task("task name", "description");

        Assertions.assertEquals("description", task.getDescription());
    }

    @Test
    void getDescription() {
        Task task = new Task("task name", "description");

        task.setDescription("to be");

        Assertions.assertEquals("to be", task.getDescription());
    }

    @Test
    void getTaskId() {
        Task task = new Task("task name", "description");

        Assertions.assertEquals(0, task.getTaskId());
    }

    @Test
    void setTaskId() {
        Task task = new Task("task name", "description");

        task.setTaskId(989);

        Assertions.assertEquals(989, task.getTaskId());
    }

    @Test
    void setTaskIdReSetter() {
        Task task = new Task("task name", "description");

        task.setTaskId(989);
        task.setTaskId(90);

        Assertions.assertEquals(989, task.getTaskId());
    }

    @Test
    void getTaskStatus() {
        Task task = new Task("task name", "description");

        Assertions.assertEquals(TaskStatus.NEW, task.getTaskStatus());
    }

    @Test
    void doNew() {
        Task task = new Task("task name", "description");

        task.doDone();
        task.doNew();

        Assertions.assertEquals(TaskStatus.NEW, task.getTaskStatus());
    }

    @Test
    void doProgress() {
        Task task = new Task("task name", "description");

        task.doProgress();

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, task.getTaskStatus());
    }

    @Test
    void doDone() {
        Task task = new Task("task name", "description");

        task.doDone();

        Assertions.assertEquals(TaskStatus.DONE, task.getTaskStatus());
    }

    @Test
    void testEquals() {
        Task task1 = new Task("", "");
        task1.setTaskId(1);

        Task task2 = new Task("","");
        task2.setTaskId(1);

        Assertions.assertTrue(task1.equals(task2));
    }

    @Test
    void testFalseEquals() {
        Task task1 = new Task("", "");
        task1.setTaskId(1);

        Task task2 = new Task("","");
        task2.setTaskId(3);

        Assertions.assertFalse(task1.equals(task2));
    }
    @Test
    void testEqualsForNull() {
        Task task1 = new Task("", "");
        task1.setTaskId(1);

        Assertions.assertFalse(task1.equals(null));
    }
    @Test
    void testEqualsForOtherType() {
        Task task1 = new Task("", "");
        task1.setTaskId(1);

        Epic epic = new Epic("", "");
        task1.setTaskId(1);

        Assertions.assertFalse(task1.equals(epic));
    }

    @Test
    void testHashCode() {
        Task task1 = new Task("", "");
        task1.setTaskId(1);

        Assertions.assertEquals(1, task1.hashCode());
    }

    @Test
    void getTaskType() {
        Task task = new Task("name", "description");

        Assertions.assertEquals(TaskType.TASK, task.getTaskType());
    }
    @Test
    void getNulDuration(){
        Task task = new Task("name", "description");

        Assertions.assertEquals(Duration.ZERO, task.getDuration());
    }
    @Test
    void getDuration(){
        Task task = new Task("name", "description");

        task.setDuration(Duration.ofMinutes(10));

        Assertions.assertEquals(10, task.getDuration().toMinutes());
    }
    @Test
    void getNulStartTime(){
        Task task = new Task("name", "description");

        Assertions.assertTrue(task.getStartTime().isEmpty());
    }
    @Test
    void getStartTime(){
        Task task = new Task("name", "description");

        LocalDateTime time = LocalDateTime.now();
        task.setStartTime(time);

        Assertions.assertTrue(task.getStartTime().filter(time::equals).isPresent());
    }
    @Test
    void getNullEndTime(){
        Task task = new Task("name", "description");

        Assertions.assertTrue(task.getEndTime().isEmpty());
    }
    @Test
    void getEndTimeWithOutDuration(){
        Task task = new Task("name", "description");

        LocalDateTime time = LocalDateTime.now();

        task.setStartTime(time);

        Assertions.assertTrue(task.getEndTime().filter(time::equals).isPresent());
    }
    @Test
    void getEndTime(){
        Task task = new Task("name", "description");

        LocalDateTime time = LocalDateTime.now();
        task.setStartTime(time);
        task.setDuration(Duration.ofHours(5));

        time = time.plusHours(5);

        Assertions.assertTrue(task.getEndTime().filter(time::equals).isPresent());
    }
}