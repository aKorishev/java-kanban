package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.SortedTaskMap;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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

        var res = tasks.put(task);

        if (res.filter(ex -> ex.getMessage().equals("Индекс уже есть в коллекции")).isPresent())
            return;

        Assertions.fail("Повторный индекс был добавлен");
    }

    @Test
    void sortedTasks() {
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

        Assertions.assertTrue(tasks.getSortedStartTimeSet(1).getFirst().getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 1).isPresent());
    }

    @Test
    void sortedTasksWhileOneTaskWithNullStartTime() {
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

        Assertions.assertTrue(tasks.getSortedStartTimeSet(1).getFirst().getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 1).isPresent());
    }

    @Test
    void sortedDescTasks() {
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

        Assertions.assertTrue(tasks.getSortedStartTimeSet(-1).getFirst().getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 5).isPresent());
    }

    @Test
    void sortedTasksAfterEditStartTime() {
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

        Assertions.assertTrue(tasks.getSortedStartTimeSet(1).getFirst().getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 1).isPresent());
    }

    @Test
    void sortedTasksAfterEditStartTimeAsNull() {
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

        Assertions.assertTrue(firstTask.getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 3).isPresent());
    }

    @Test
    void sortedTasksAfterEditStartTimeAfterNull() {
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

        task = tasks.getSortedStartTimeSet(1).getFirst();

        Assertions.assertTrue(task.getStartTime().map(LocalDateTime::getDayOfMonth).filter(i -> i == 1).isPresent());
    }

    @Test
    void sortedTasksWithStartTimeAsNull() {
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
    void notCrossingTasks() {
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
    void hasCrossingTasks() {
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

        var res = tasks.put(task);

        if (res.filter(ex -> ex.getMessage().equals("Новый элемент пересекается с другими")).isPresent())
            return;

        Assertions.fail("Пропущена валидация пересечний");
    }
}
