package taskmanagers;

//Вместо класса Managers
public class TaskManagerFactory {
    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public TaskManager getInMemoryTaskManager(){
        return new InMemoryTaskManager();
    }
}
