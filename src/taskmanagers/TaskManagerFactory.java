package taskmanagers;

import historyManagers.FileBackedHistoryManager;
import historyManagers.HistoryManager;
import historyManagers.InMemoryHistoryManager;

public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager(){
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        return new TaskManagerWithHistory(taskManager, historyManager);
    }

    public static TaskManager initFileBackedTaskManager(String fullNameFile){
        TaskManager taskManager = new FileBackedTaskManager(fullNameFile);
        HistoryManager historyManager = new FileBackedHistoryManager();

        return new TaskManagerWithHistory(taskManager, historyManager);
    }
}
