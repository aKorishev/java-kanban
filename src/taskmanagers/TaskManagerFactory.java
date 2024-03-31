package taskmanagers;

import historyManagers.InMemoryHistoryManager;
import tasks.Task;

public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager(){

        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}
