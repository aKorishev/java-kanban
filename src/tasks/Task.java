package tasks;

import enums.TaskStatus;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int taskId;
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;
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

    public int getTaskId() {return taskId; }

    public void setTaskId(Integer taskId) {
        if (this.taskId == 0)
            this.taskId = taskId;
    }

    public Duration getDuration(){
        return duration;
    }
    public void setDuration(Duration duration){
        this.duration = Objects.requireNonNullElse(duration, Duration.ZERO);
    }
    public LocalDateTime getStartTime(){
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime(){
        if (startTime == null)
            return null;

        return startTime.plus(duration);
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
    public TaskType getTaskType(){
        return TaskType.TASK;
    }

    public void doNew(){
        taskStatus = TaskStatus.NEW;
    }
    public void doProgress(){
        taskStatus = TaskStatus.IN_PROGRESS;
    }
    public void doDone(){
        taskStatus = TaskStatus.DONE;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;

        if (!(other instanceof Task)) return false;

        Task task = (Task)other;

        return
            taskId == task.taskId
            && name.equals(task.name)
            && description.equals(description)
            && taskStatus == task.taskStatus;
    }
    @Override
    public int hashCode(){
        return taskId;
    }
    @Override
    public String toString(){
        return "taskId = '" + taskId + "', name = '" + name + ", desc = " + description + ", taskType = " + getTaskType().name() + ", status = " + taskStatus.name();
    }
}
