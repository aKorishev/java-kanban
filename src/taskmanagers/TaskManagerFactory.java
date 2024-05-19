package taskmanagers;

import historymanagers.FileBackedHistoryManager;
import historymanagers.HistoryManager;
import historymanagers.InMemoryHistoryManager;

import java.io.File;

public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager() {
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        return new TaskManagerWithHistory(taskManager, historyManager);
    }

    public static TaskManager initFileBackedTaskManager(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        taskManager.reload();

        HistoryManager historyManager = new FileBackedHistoryManager();

        return new TaskManagerWithHistory(taskManager, historyManager);
    }
}
