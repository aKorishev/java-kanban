package taskmanagers;

import historyManagers.InMemoryHistoryManager;

//Вместо класса Managers
public class TaskManagerFactory {
    public static TaskManager initInMemoryTaskManager(){

        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}
