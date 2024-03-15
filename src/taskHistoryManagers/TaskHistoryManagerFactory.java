package taskHistoryManagers;

public class TaskHistoryManagerFactory {
    public static InMemoryTaskHistoryManager initInMemoryTaskHistoryManager() {

        return new InMemoryTaskHistoryManager();
    }
}
