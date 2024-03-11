package tools;

import java.util.ArrayList;
import java.util.List;

//Для реаоизации достаточно одного класса, на всех TaskManager
public class TaskMapHistory {
    private final List<Integer> list = new ArrayList<>();
    public void put(int key){
        while (list.size() >= 10) list.removeFirst();

       // if (!list.contains(key))
            list.add(key);
    }

    public List<Integer> getHistory(){
        return list;
    }
}
