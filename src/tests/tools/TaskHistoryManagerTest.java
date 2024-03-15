package tests.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskHistoryManagers.TaskHistoryManager;
import taskHistoryManagers.InMemoryTaskHistoryManager;

import java.util.Collection;

class TaskHistoryManagerTest {

    @Test
    void add() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(10);
        taskHistoryManager.add(11);

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addDouble() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(10);
        taskHistoryManager.add(10);

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addOver10() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(10);
        taskHistoryManager.add(11);
        taskHistoryManager.add(12);
        taskHistoryManager.add(13);
        taskHistoryManager.add(14);
        taskHistoryManager.add(15);
        taskHistoryManager.add(16);
        taskHistoryManager.add(17);
        taskHistoryManager.add(18);
        taskHistoryManager.add(19);
        taskHistoryManager.add(20);
        taskHistoryManager.add(21);
        taskHistoryManager.add(22);
        taskHistoryManager.add(23);
        taskHistoryManager.add(24);

        Assertions.assertEquals(10, taskHistoryManager.getHistory().size());
    }

    @Test
    void getHistory() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        int[] values = new int[]{1,2,3,4,5,6,7,8,6};
        for(int i : values) taskHistoryManager.add(i);

        Collection<Integer> history = taskHistoryManager.getHistory();
        for(int i : values)
            Assertions.assertTrue(history.contains(i));
    }
}