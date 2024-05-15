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
import java.util.Optional;

public class SortedTaskMapTest {
    @Test
    void putTasks() throws Exception {
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
    void putDoubleTasks() throws Exception {
        SortedTaskMap<Task> tasks = new SortedTaskMap<>();

        Task task = new Task("","");
        task.setTaskId(1);
        tasks.put(task);

        task = new Task("","");
        task.setTaskId(1);
        try {
            tasks.put(task);
        } catch (Exception ex) {
            if (ex.getMessage().equals("Индекс уже есть в коллекции"))
                return;
            throw ex;
        }

        Assertions.fail("Повторный индекс был добавлен");
    }
    @Test
    void sortedTasks() throws Exception {
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

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedTasksWhileOneTaskWithNullStartTime() throws Exception {
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

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedDescTasks() throws Exception {
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

        Assertions.assertEquals(5, tasks.getSortedStartTimeSet(-1).getFirst().getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTime() throws Exception {
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

        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));

        tasks.refreshSortedSets(task);

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTimeAsNull() throws Exception {
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

        task = tasks.get(2);
        task.setStartTime(Optional.empty());
        tasks.refreshSortedSets(task);

        var firstTask = tasks.getSortedStartTimeSet(1).getFirst();

        Assertions.assertEquals(3, firstTask.getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedTasksAfterEditStartTimeAfterNull() throws Exception {
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

        task.setStartTime(LocalDateTime.of(2024,1,1,0,0));
        tasks.refreshSortedSets(task);

        Assertions.assertEquals(1, tasks.getSortedStartTimeSet(1).getFirst().getStartTime().get().getDayOfMonth());
    }
    @Test
    void sortedTasksWithStartTimeAsNull() throws Exception {
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
    void notCrossingTasks() throws Exception {
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
    void hasCrossingTasks() throws Exception {
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
        } catch (Exception e){
            if (e.getMessage().equals("Новый элемент пересекается с другими"))
                return;
        }

        Assertions.fail("Пропущена валидация пересечний");
    }
}
