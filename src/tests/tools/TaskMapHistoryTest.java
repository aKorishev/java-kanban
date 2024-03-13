package tests.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.TaskMapHistory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapHistoryTest {

    @Test
    void put() {
        TaskMapHistory taskMapHistory = new TaskMapHistory();

        taskMapHistory.put(10);
        taskMapHistory.put(11);

        Assertions.assertEquals(2, taskMapHistory.getHistory().size());
    }
    @Test
    void putDouble() {
        TaskMapHistory taskMapHistory = new TaskMapHistory();

        taskMapHistory.put(10);
        taskMapHistory.put(10);

        Assertions.assertEquals(2, taskMapHistory.getHistory().size());
    }
    @Test
    void putOver10() {
        TaskMapHistory taskMapHistory = new TaskMapHistory();

        taskMapHistory.put(10);
        taskMapHistory.put(11);
        taskMapHistory.put(12);
        taskMapHistory.put(13);
        taskMapHistory.put(14);
        taskMapHistory.put(15);
        taskMapHistory.put(16);
        taskMapHistory.put(17);
        taskMapHistory.put(18);
        taskMapHistory.put(19);
        taskMapHistory.put(20);
        taskMapHistory.put(21);
        taskMapHistory.put(22);
        taskMapHistory.put(23);
        taskMapHistory.put(24);

        Assertions.assertEquals(10, taskMapHistory.getHistory().size());
    }

    @Test
    void getHistory() {
        TaskMapHistory taskMapHistory = new TaskMapHistory();

        int[] values = new int[]{1,2,3,4,5,6,7,8,6};
        for(int i : values) taskMapHistory.put(i);

        List<Integer> history = taskMapHistory.getHistory();
        for(int i : values)
            Assertions.assertTrue(history.contains(i));
    }
}