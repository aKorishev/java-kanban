package historyManagers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task element);
    void remove(Task element);

    List<Task> getHistory();
    List<Task> getHistory(int size);

    int size();
}
