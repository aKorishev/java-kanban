package tasks;


import enums.TaskStatus;
import enums.TaskType;
import tools.ReadOnlyCollection;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<String, SubTask> subTasks = new HashMap<>();
    public Epic(String name, String description) {
        this(name, description, TaskStatus.NEW);
    }
    public Epic(String name, String description, TaskStatus taskStatus){
        super(name, description, taskStatus);
    }
    public ReadOnlyCollection<SubTask> getSubTasks(){
        return new ReadOnlyCollection<>(subTasks.values());
    }

    public void putSubTask(SubTask task){
        String taskId = task.getTaskId();

        if (subTasks.containsKey(taskId))
            return;

        subTasks.put(taskId, task);

        if (getTaskStatus() == TaskStatus.DONE){
            if (task.getTaskStatus() != TaskStatus.DONE)
                doInProgress();
        }
    }
    public void removeSubTask(String taskId){
        SubTask subTask = subTasks.remove(taskId);

        if (getTaskStatus() == TaskStatus.IN_PROGRESS && subTask.getTaskStatus() != TaskStatus.DONE){
            for(SubTask i: subTasks.values())
                if (i.getTaskStatus() != TaskStatus.DONE)
                    return;

            doInDone();
        }
    }
    public void clearSubTasks(){
        subTasks.clear();

        if (getTaskStatus() == TaskStatus.IN_PROGRESS)
            doInDone();
    }
    @Override
    protected TaskType getTaskType(){
        return TaskType.EPIC;
    }
}
