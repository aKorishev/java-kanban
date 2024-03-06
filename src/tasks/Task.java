package tasks;

import enums.TaskStatus;
import enums.TaskType;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private String taskId;
    public Task(String name, String description){
        this.name = name;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
    }
    public Task(String name, String description, TaskStatus taskStatus){
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() {
        return description;
    }

    public String getTaskId() {return taskId; }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void doNew(){
        taskStatus = TaskStatus.NEW;
    }
    public void doInProgress(){
        taskStatus = TaskStatus.IN_PROGRESS;
    }
    public void doInDone(){
        taskStatus = TaskStatus.DONE;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;

        if (!(other instanceof Task)) return false;

        Task task = (Task)other;

        return taskId.equals(task.taskId) && getTaskType() == task.getTaskType();
    }
    @Override
    public int hashCode(){
        return taskId.hashCode() + name.hashCode() + description.hashCode() + getTaskType().hashCode();
    }
    @Override
    public String toString(){
        return "taskId = '" + taskId + "', name = '" + name + ", desc = " + description + ", taskType = " + getTaskType().name() + ", status = " + taskStatus.name();
    }
    protected TaskType getTaskType(){
        return TaskType.TASK;
    }
}
