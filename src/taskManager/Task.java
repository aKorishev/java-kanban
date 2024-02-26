package taskManager;

public class Task {
    private final String name;
    private final String description;
    //Это поле упрощает работу с HashMap для TaskManager для Epic и SubTask
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

        return name.equals(task.name) && description.equals(task.description);
    }
    @Override
    public int hashCode(){
        return name.hashCode() + description.hashCode();
    }
    @Override
    public String toString(){
        return "taskId = " + taskId + ", name = '" + name + ", desc = " + description + ", taskType = " + getTaskType().name() + ", status = " + taskStatus.name();
    }

    public Task updateName(String name){
        return new Task(name, description, taskId);
    }
    public Task updateDescription(String description){
        return new Task(name, description, taskId);
    }
    public Task update(String name, String description){
        return new Task(name, description, taskId);
    }

    protected TaskType getTaskType(){
        return TaskType.TASK;
    }
}
