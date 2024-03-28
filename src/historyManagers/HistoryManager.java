package historyManagers;

import tasks.Task;

import java.util.Collection;

public interface HistoryManager {
    void add(Task task);

    Collection<Task> getHistory();
}
