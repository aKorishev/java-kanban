package taskmanagers;

import historyManagers.HistoryManager;
import historyManagers.HistoryManagerFactory;
import historyManagers.InMemoryHistoryManager;
import tasks.Task;

public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager(){

        return new InMemoryTaskManager(HistoryManagerFactory.initInMemoryHistoryManager());
    }
}
