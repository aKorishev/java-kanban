package TaskManager;


import java.util.Collection;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }
    public Collection<SubTask> subTasks(){
        return subTasks.values();
    }

    @Override
    public void doInDone(){
        for (SubTask subtask : subTasks.values())
            if(subtask.getTaskStatus() != TaskStatus.DONE)
                return;

        super.doInDone();
    }
    void putSubTask(SubTask subTask){
        int taskId = subTask.getTaskId();

        if (!subTasks.containsKey(taskId))
            subTasks.put(taskId, subTask);
    }
    void removeSubTask(int taskId){
        if (subTasks.containsKey(taskId))
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
