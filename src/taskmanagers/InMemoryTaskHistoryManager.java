package taskmanagers;

import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {
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

