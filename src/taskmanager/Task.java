package taskmanager;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    Task(String name, String description){
        this(name, description, TaskStatus.NEW);
    }
    Task(String name, String description, TaskStatus taskStatus){
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) { this.description = description; }
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
        return "name = '" + name + ", desc = " + description + ", taskType = " + getTaskType().name() + ", status = " + taskStatus.name();
    }
    protected TaskType getTaskType(){
        return TaskType.TASK;
    }
}
