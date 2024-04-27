package tests.tasks;

import enums.TaskStatus;
import enums.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;


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
    void getSubTasksWithoutSubTaskId() {
        Epic epic = new Epic("name", "description");

        epic.putSubTask(new SubTask("","",0));
        epic.putSubTask(new SubTask("","",0));

        Assertions.assertNotEquals(2, epic.getSubTasks().size());
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
    @Test
    void getDurationWithOutSubtasks(){
        Epic epic = new Epic("name", "description");

        epic.calcTaskDuration();
        Assertions.assertEquals(Duration.ZERO, epic.getDuration());
    }
    @Test
    void getDurationSubtasks(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofHours(1));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(10));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(Duration.ofMinutes(70), epic.getDuration());
    }
    @Test
    void getDurationSubTaskWhileOneSubTaskWithoutDuration() {
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(25));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(Duration.ofMinutes(25), epic.getDuration());
    }
    @Test
    void getStartTimeWithOutSubtasks(){
        Epic epic = new Epic("name", "description");

        epic.calcTaskDuration();
        Assertions.assertNull(epic.getStartTime());
    }
    @Test
    void getMinimumStartTimeSubtasks(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 3));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 2));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(2, epic.getStartTime().getMinute());
    }
    @Test
    void getMinimumStartTimeSubtasksWhileOneSubTaskWithoutStartTime(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 3));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(3, epic.getStartTime().getMinute());
    }
    @Test
    void getMinimumStartTimeSubtasksWhileAllSubTaskWithoutStartTime(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertNull(epic.getStartTime());
    }
    @Test
    void getEndTimeWithOutSubtasks(){
        Epic epic = new Epic("name", "description");

        epic.calcTaskDuration();
        Assertions.assertNull(epic.getEndTime());
    }
    @Test
    void getEndTime(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 2));
        subTask.setDuration(Duration.ofHours(2));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 4, 2));
        subTask.setDuration(Duration.ofMinutes(10));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(LocalDateTime.of(2024,5, 1, 4, 12), epic.getEndTime());
    }
    @Test
    void getEndTimeWhileOneSubTaskWithoutStartTime(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 3));
        subTask.setDuration(Duration.ofHours(2));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(10));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(LocalDateTime.of(2024,5, 1, 4, 13), epic.getEndTime());
    }
    @Test
    void getEndTimeWhileOneSubTaskWithoutDuration(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setStartTime(LocalDateTime.of(2024,5, 1, 2, 3));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(10));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertEquals(LocalDateTime.of(2024,5, 1, 2, 13), epic.getEndTime());
    }
    @Test
    void getEndTimeWhileAllSubTaskWithoutStartTime(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(10));
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setDuration(Duration.ofMinutes(10));
        epic.putSubTask(subTask);

        epic.calcTaskDuration();
        Assertions.assertNull(epic.getEndTime());
    }
    @Test
    void allSubTaskNew(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.NEW, epic.getTaskStatus());
    }
    @Test
    void otherStatuses(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doProgress();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus());
    }
    @Test
    void OneInProgressOtherNew(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doProgress();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus());
    }
    @Test
    void OneDoneOtherNew(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doProgress();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus());
    }
    @Test
    void AllDone(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        subTask.doDone();
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.DONE, epic.getTaskStatus());
    }
    @Test
    void OneNewOtherDone(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        subTask.doDone();
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus());
    }
    @Test
    void OneInProgressOtherDone(){
        Epic epic = new Epic("name", "description");
        epic.setTaskId(1);

        SubTask subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(10);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(11);
        subTask.doProgress();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(12);
        subTask.doDone();
        epic.putSubTask(subTask);

        subTask = new SubTask("","",epic.getTaskId());
        subTask.setTaskId(13);
        subTask.doDone();
        epic.putSubTask(subTask);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic.getTaskStatus());
    }
}