package taskHistoryManagers;

import java.util.Collection;

public interface TaskHistoryManager {
    void add(int key);

    Collection<Integer> getHistory();
}
