package taskmanagers;

//Вместо класса Managers
public class OtherManagerFactory {
    public static TaskManager getDefault(){

        return new InMemoryTaskManager(new InMemoryTaskHistoryManager());
    }

    public static TaskHistoryManager initInMemoryTaskHistoryManager() {

        return new InMemoryTaskHistoryManager();
    }
}
