package historyManagers;

import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> list = new ArrayList<>();
    @Override
    public void add(Task task){
        if (list.size() == 10) list.removeFirst();

        list.add(task);
    }

    @Override
    public Collection<Task> getHistory(){

        return new ArrayList<>(list);
    }
}

