package taskmanagers;

import historyManagers.HistoryManager;
import historyManagers.HistoryManagerFactory;
import historyManagers.InMemoryHistoryManager;
import tasks.Task;

public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager(){
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        return new TaskManagerWithHistory(taskManager, historyManager);
    }
}
