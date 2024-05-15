package tasks;


import enums.TaskStatus;
import enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private final SortedTaskMap<SubTask> subTasks = new SortedTaskMap<>();
    private Optional<LocalDateTime> endTime = Optional.empty();
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }
    public List<SubTask> getSubTasks(){
        return subTasks.getList();
    }

    public void putSubTask(SubTask subTask){
        int taskId = subTask.getTaskId();

        if (subTasks.containsKey(taskId))
            return;

        subTasks.put(taskId, subTask);

        if (getTaskStatus() == TaskStatus.DONE){
            if (subTask.getTaskStatus() != TaskStatus.DONE)
                doProgress();
        }

        subTask.getStartTime().ifPresent(i -> this.calcTaskDuration());
    }
    public void removeSubTask(int taskId) {
        subTasks.remove(taskId);

        for (SubTask i : subTasks.values())
            if (i.getTaskStatus() != TaskStatus.DONE)
                return;

        doDone();
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
            setStartTime(Optional.empty());

            return;
        }

        Duration duration = Duration.ZERO;
        Optional<LocalDateTime> epicStartTime = Optional.empty();

        for(SubTask subTask : subTasks.values()){
            duration = duration.plus(subTask.getDuration());

            var subTaskStartTime = subTask.getStartTime();

            if (subTaskStartTime.isPresent() && epicStartTime.isEmpty())
                epicStartTime = subTaskStartTime;
            else if (subTaskStartTime.isPresent() && epicStartTime.filter(subTaskStartTime.get()::isBefore).isPresent())
                epicStartTime = subTaskStartTime;
        }

        setDuration(duration);
        setStartTime(epicStartTime);

        Duration d = duration;
        this.endTime = epicStartTime.map(i -> i.plus(d));

    }

    public Optional<LocalDateTime> getEndTime(){
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
