package taskmanagers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryTaskHistoryManager implements TaskHistoryManager {
    private final List<Integer> list = new ArrayList<>();
    @Override
    public void add(int key){
        if (list.size() == 10) list.removeFirst();

        list.add(key);
    }

    @Override
    public Collection<Integer> getHistory(){

        return new ArrayList<>(list);
    }
}

