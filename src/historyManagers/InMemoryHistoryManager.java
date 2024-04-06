package historyManagers;


import tasks.Task;

import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final InMemoryHistoryLinkedList list = new InMemoryHistoryLinkedList();
    @Override
    public void add(Task element){
        list.add(element);
    }

    @Override
    public void remove(Task element){
        list.remove(element);
    }

    @Override
    public List<Task> getHistory(){

        return list.getHistory();
    }

    @Override
    public List<Task> getHistory(int size){

        return list.getHistory(size);
    }

    @Override
    public int size() { return list.size(); }
}

