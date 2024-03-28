package historyManagers;

public class HistoryManagerFactory {

    public static HistoryManager initInMemoryHistoryManager() {

        return new InMemoryHistoryManager();
    }
}
