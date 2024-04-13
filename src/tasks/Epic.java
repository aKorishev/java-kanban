package tasks;


import enums.TaskStatus;
import enums.TaskType;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }
    public ArrayList<SubTask> getSubTasks(){
        return new ArrayList<>(subTasks.values());
    }

    public void putSubTask(SubTask task){
        int taskId = task.getTaskId();

        if (subTasks.containsKey(taskId))
            return;

        subTasks.put(taskId, task);

        if (getTaskStatus() == TaskStatus.DONE){
            if (task.getTaskStatus() != TaskStatus.DONE)
                doProgress();
        }
    }
    public void removeSubTask(int taskId){
        SubTask subTask = subTasks.remove(taskId);

        if (getTaskStatus() == TaskStatus.IN_PROGRESS && subTask.getTaskStatus() != TaskStatus.DONE){
            for(SubTask i: subTasks.values())
                if (i.getTaskStatus() != TaskStatus.DONE)
                    return;

            doDone();
        }
    }
    public void clearSubTasks(){
        subTasks.clear();

        if (getTaskStatus() == TaskStatus.IN_PROGRESS)
            doDone();
    }

    public boolean containsSubTaskId(int subTaskId) { return subTasks.containsKey(subTaskId); }

    @Override
    public TaskType getTaskType(){
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object other){
        if (!super.equals(other)) return false;

        Epic epic = (Epic) other;

        if (subTasks.size() != epic.subTasks.size()) return false;

        for(SubTask subTask : subTasks.values()){
            int subTaskId = subTask.getTaskId();
            if (!epic.subTasks.containsKey(subTaskId)) return false;

            if (!((Task) subTask).equals(epic.subTasks.get(subTaskId))) return false;
        }

        return true;
    }
}
