package taskmanagers;

import tasks.Task;

import java.util.Collection;

public interface TaskHistoryManager {
    void add(Task task);

    Collection<Task> getHistory();
}
