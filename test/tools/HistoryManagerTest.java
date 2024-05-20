package tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import historymanagers.HistoryManager;
import historymanagers.InMemoryHistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

class HistoryManagerTest {

    @Test
    void add() {
        HistoryManager taskHistoryManager = new InMemoryHistoryManager();

        Task task = new Task("","");
        task.setTaskId(10);

        taskHistoryManager.add(task);

        task = new Task("","");
        task.setTaskId(11);

        taskHistoryManager.add(task);

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }

    @Test
    void addDouble() {
        HistoryManager taskHistoryManager = new InMemoryHistoryManager();

        Task task = new Task("","");
        task.setTaskId(10);

        taskHistoryManager.add(task);
        taskHistoryManager.add(task);

        Assertions.assertEquals(1, taskHistoryManager.getHistory().size());
    }

    @Test
    void getHistoryAfterRemove() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        List<Integer> ints = List.of(0,1,2,3,4,5,6,7,8);
        for (int i : ints) {
            Task task = new Task("","");
            task.setTaskId(i);
            historyManager.add(task);
        }

        ints = List.of(8,7,6,4,3,2,1,0);
        Task task = new Task("","");
        task.setTaskId(5);
        historyManager.remove(task.getTaskId());

        List<Task> history = historyManager.getHistory();

        for (int i = 0; i < ints.size(); i++)
            Assertions.assertEquals(ints.get(i), history.get(i).getTaskId());
    }


    @Test
    void getHistoryGorSize() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        int size = 5;

        List<Integer> ints = List.of(0,1,2,3,4,5,6,7,8);

        for (int i : ints) {
            Task task = new Task("","");
            task.setTaskId(i);
            historyManager.add(task);
        }

        ints = List.of(8,7,6,5,4);

        List<Task> history = new ArrayList<>(historyManager.getHistory(size));

        for (int i = 0; i < ints.size(); i++)
            Assertions.assertEquals(ints.get(i), history.get(i).getTaskId());
    }

    @Test
    void getHistoryGorSizeWithDoubles() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        int size = 5;

        List<Integer> ints = List.of(0,1,2,3,4,5,6,7,8,1,4);

        for (int i : ints) {
            Task task = new Task("","");
            task.setTaskId(i);
            historyManager.add(task);
        }

        ints = List.of(4,1,8,7,6);

        List<Task> history = new ArrayList<>(historyManager.getHistory(size));

        for (int i = 0; i < ints.size(); i++)
            Assertions.assertEquals(ints.get(i), history.get(i).getTaskId());
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        List<Integer> ints = List.of(0,1,2,3,4,5,6,7,8);
        for (int i : ints) {
            Task task = new Task("","");
            task.setTaskId(i);
            historyManager.add(task);
        }

        ints = ints.reversed();

        List<Task> history = historyManager.getHistory();

        for (int i = 0; i < ints.size(); i++)
            Assertions.assertEquals(ints.get(i), history.get(i).getTaskId());
    }
}