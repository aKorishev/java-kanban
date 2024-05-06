package tests.tasks;

import enums.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SortedTaskMap;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class SortedTaskMapTest {
    @Test
    void putTasks() {
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        tasks.put(task);

        Assertions.assertEquals(3, tasks.size());
    }
    @Test
    void putDoubleTasks() {
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(1);
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        tasks.put(task);

        Assertions.assertEquals(2, tasks.size());
    }
    @Test
    void sortedTasks(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,5,0,0));
        tasks.put(task);

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedTasksWhileOneTaskWithNullStartTime(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,5,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(4);
        tasks.put(task);

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedDescTasks(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,5,0,0));
        tasks.put(task);

        Assertions.assertEquals(5, tasks.getSortedStartTimeSet(-1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTime(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,5,0,0));
        tasks.put(task);

        tasks.setStartTime(3, LocalDateTime.of(2024,1,1,0,0));

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTimeAsNull(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,5,0,0));
        tasks.put(task);

        tasks.setStartTime(2, null);

        Assertions.assertEquals(3, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTimeAfterNull(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        tasks.put(task);

        tasks.setStartTime(3, LocalDateTime.of(2024,1,1,0,0));

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().getDayOfMonth());
    }
    @Test
    void sortedTasksWithStartTimeAsNull(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        tasks.put(task);

        Assertions.assertEquals(2, tasks.getSortedStartTimeSet(1).size());
    }
    @Test
    void notCrossingTasks(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        task.setDuration(Duration.ofHours(20));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        task.setDuration(Duration.ofHours(20));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(3);
        task.setStartTime(LocalDateTime.of(2024,1,3,0,0));
        task.setDuration(Duration.ofHours(20));
        tasks.put(task);

        Assertions.assertFalse(tasks.getHasCross());
    }
    @Test
    void hasCrossingTasks(){
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        task.setDuration(Duration.ofHours(25));
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(2);
        task.setStartTime(LocalDateTime.of(2024,1,2,0,0));
        task.setDuration(Duration.ofHours(10));

        try {
            tasks.put(task);
        } catch (Error error){
            if (error.getMessage().equals("Новый элемент пересекается с другими"))
                return;
        }

        Assertions.fail("Пропущена валидация пересечний");
    }
}
