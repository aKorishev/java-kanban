package tasks;

import enums.TaskStatus;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int taskId;
    private Duration duration = Duration.ZERO;
    private Optional<LocalDateTime> startTime = Optional.empty();

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.taskStatus = TaskStatus.NEW;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        if (this.taskId == 0)
            this.taskId = taskId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = Objects.requireNonNullElse(duration, Duration.ZERO);
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        setStartTime(Optional.of(startTime));
    }

    public void setStartTime(Optional<LocalDateTime> startTime) {
        this.startTime = startTime;
    }

    public Optional<LocalDateTime> getEndTime() {
        return startTime.map(t -> t.plus(duration));
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public void doNew() {
        taskStatus = TaskStatus.NEW;
    }

    public void doProgress() {
        taskStatus = TaskStatus.IN_PROGRESS;
    }

    public void doDone() {
        taskStatus = TaskStatus.DONE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;

        if (!(other instanceof Task task)) return false;

        if (!(
            taskId == task.taskId
            && name.equals(task.name)
            && description.equals(task.description)
            && taskStatus == task.taskStatus
        ))
            return false;

        if (!duration.equals(task.duration))
            return false;

        if (!startTime
                .map(t -> task.startTime.filter(t::equals).isPresent())
                .orElse(task.startTime.isEmpty()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return taskId;
    }

    @Override
    public String toString() {
        return "taskId = '" + taskId + "', name = '" + name + ", desc = " + description + ", taskType = " + getTaskType().name() + ", status = " + taskStatus.name();
    }
}
