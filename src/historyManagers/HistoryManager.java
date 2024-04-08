package historyManagers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task element);
    void remove(int taskId);

    List<Task> getHistory();
    List<Task> getHistory(int size);

    int size();
}
