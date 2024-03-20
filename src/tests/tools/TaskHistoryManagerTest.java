package tests.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskHistoryManager;
import taskmanagers.InMemoryTaskHistoryManager;
import taskmanagers.TaskManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;

class TaskHistoryManagerTest {

    @Test
    void add() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addDouble() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));

        Assertions.assertEquals(2, taskHistoryManager.getHistory().size());
    }
    @Test
    void addOver10() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));
        taskHistoryManager.add(new Task("",""));

        Assertions.assertEquals(10, taskHistoryManager.getHistory().size());
    }

    @Test
    void getHistory() {
        TaskHistoryManager taskHistoryManager = new InMemoryTaskHistoryManager();

        ArrayList<Task> tasks = new ArrayList<>();
        for(int i : new int[]{1,2,3,4,5,6,7,8,6}){
            Task task = new Task("","");
            task.setTaskId(i);
            tasks.add(task);
            taskHistoryManager.add(task);
        }

        ArrayList<Integer> history = new ArrayList<>();
        for(Task task : taskHistoryManager.getHistory())
            history.add(task.getTaskId());

        for(Task task : tasks)
            Assertions.assertTrue(history.contains(task.getTaskId()));
    }
}