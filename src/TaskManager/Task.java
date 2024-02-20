package TaskManager;

public class Task {
    private final String name;
    private final String description;
    private final int taskId;
    private TaskStatus taskStatus = TaskStatus.NEW;

    public Task(String name, String description, int taskId){
        this.name = name;
        this.description = description;
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void DoNew(){
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

        return taskId == task.taskId;
    }

    @Override
    public int hashCode(){
        return taskId;
    }

    @Override
    public String toString(){
        return "taskId = " + taskId + ", name = '" + name + ", taskType = " + getTaskType().name();
    }


    protected TaskType getTaskType(){
        return TaskType.TASK;
    }
}
