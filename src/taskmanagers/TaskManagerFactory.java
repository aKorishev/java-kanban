package taskmanagers;

import taskHistoryManagers.TaskHistoryManagerFactory;

//Вместо класса Managers
public class TaskManagerFactory {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager(TaskHistoryManagerFactory.initInMemoryTaskHistoryManager());
    }

    public TaskManager initInMemoryTaskManager(){
        return new InMemoryTaskManager(TaskHistoryManagerFactory.initInMemoryTaskHistoryManager());
    }
}
