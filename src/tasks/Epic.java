package tasks;


import enums.TaskStatus;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private final SortedTaskMap<SubTask> subTasks = new SortedTaskMap<>();
    private LocalDateTime endTime;
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }
    public List<SubTask> getSubTasks(){
        return subTasks.getList();
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

    public void calcTaskDuration() {
        if (subTasks.isEmpty()){
            setDuration(Duration.ZERO);
            setStartTime(null);

            return;
        }

        Duration duration = Duration.ZERO;
        LocalDateTime localDateTime = LocalDateTime.MAX;

        for(SubTask subTask : subTasks.values()){
            duration = duration.plus(subTask.getDuration());

            LocalDateTime startTime = subTask.getStartTime();

            if (startTime != null && startTime.isBefore(localDateTime))
                localDateTime = startTime;
        }

        setDuration(duration);

        if (localDateTime.isBefore(LocalDateTime.MAX)) {
            setStartTime(localDateTime);
            endTime = localDateTime.plus(duration);
        }
        else {
            setStartTime(null);
            endTime = null;
        }

    }

    public LocalDateTime getEndTime(){
        calcTaskDuration();
        return endTime;
    }

    @Override
    public TaskType getTaskType(){
        return TaskType.EPIC;
    }

    @Override
    public void doDone() {

    }
    @Override
    public void doNew() {

    }
    @Override
    public void doProgress() {

    }

    @Override
    public TaskStatus getTaskStatus() {
        TaskStatus epicStatus = TaskStatus.NEW;
        for(SubTask subTask : subTasks.values()){
            TaskStatus status = subTask.getTaskStatus();

            if (status == epicStatus)
                continue;
            if (status == TaskStatus.IN_PROGRESS || status == TaskStatus.NEW)
                return TaskStatus.IN_PROGRESS;

            epicStatus = status;
        }

        return epicStatus;
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
