package tools;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

//Для записи истории выделили в отдельный класс, чтобы не писать это во всех реализациях TaskManager
public class TaskMap<T extends Task> extends HashMap<Integer, T> {
    public TaskMap(){
        super();
    }
}
