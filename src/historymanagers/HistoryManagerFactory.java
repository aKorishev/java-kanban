package historymanagers;

public class HistoryManagerFactory {

    public static HistoryManager initInMemoryHistoryManager() {

        return new InMemoryHistoryManager();
    }
}
