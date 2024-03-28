package tests.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import historyManagers.HistoryManager;
import historyManagers.InMemoryHistoryManager;
import tasks.Task;

import java.util.ArrayList;

class HistoryManagerTest {

    @Test
    void add() {
        HistoryManager taskHistoryManager = new InMemoryHistoryManager();

        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addDouble() {
        HistoryManager taskHistoryManager = new InMemoryHistoryManager();

        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addOver10() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));
        historyManager.add(new Task("",""));

        Assertions.assertEquals(10, historyManager.getHistory().size());
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        ArrayList<Task> tasks = new ArrayList<>();
        for(int i : new int[]{1,2,3,4,5,6,7,8,6}){
            Task task = new Task("","");
            task.setTaskId(i);
            tasks.add(task);
            historyManager.add(task);
        }

        ArrayList<Integer> history = new ArrayList<>();
        for(Task task : historyManager.getHistory())
            history.add(task.getTaskId());

        for(Task task : tasks)
            Assertions.assertTrue(history.contains(task.getTaskId()));
    }
}