package taskManager;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public Epic(String name, String description, int taskId) {
        super(name, description, taskId);
    }
    public Collection<SubTask> getSubTasks(){
        return subTasks.values();
    }

    @Override
    public TaskStatus getTaskStatus(){
        int numberOfNews = 0;

        for(Task task : subTasks.values()) {
            if (task.getTaskStatus() == TaskStatus.IN_PROGRESS)
                return TaskStatus.IN_PROGRESS;

            if (task.getTaskStatus() == TaskStatus.NEW)
                numberOfNews++;
        }

        if (numberOfNews > 0 && numberOfNews == subTasks.size())
            return TaskStatus.NEW;

        if (numberOfNews == 0)
            return TaskStatus.DONE;

        return TaskStatus.IN_PROGRESS;
    }

    @Override
    public Epic updateName(String name){
        return new Epic(name, getDescription(), getTaskId());
    }
    @Override
    public Epic updateDescription(String description){
        return new Epic(getName(), description, getTaskId());
    }
    @Override
    public Epic update(String name, String description){
        return new Epic(name, description, getTaskId());
    }
    public void updateSubTask(SubTask subTask){
        subTasks.replace(subTask.getTaskId(), subTask);
    }

    void putSubTask(int taskId, SubTask task){
        if (!subTasks.containsKey(taskId))
            subTasks.put(taskId, task);
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
