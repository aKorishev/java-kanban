package taskmanager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public Epic(String name, String description) {
        this(name, description, TaskStatus.NEW);
    }
    public Epic(String name, String description, TaskStatus taskStatus){
        super(name, description, taskStatus);
    }
    public ArrayList<SubTask> getSubTasks(){
        return new ArrayList<>(subTasks.values());
    }
    public ArrayList<Integer> getSubTaskIds(){
        return new ArrayList<>(subTasks.keySet());
    }

    void putSubTask(int taskId, SubTask task){
        if (subTasks.containsKey(taskId))
            return;

        subTasks.put(taskId, task);

        TaskStatus status = getTaskStatus();
        if (getTaskStatus() == TaskStatus.DONE){
            if (task.getTaskStatus() != TaskStatus.DONE)
                doInProgress();
        }

    }
    void removeSubTask(int taskId){
        subTasks.remove(taskId);
    }
    void clearSubTasks(){
        subTasks.clear();
    }
    @Override
    protected TaskType getTaskType(){
        return TaskType.EPIC;
    }
}
