package taskmanager;

public class SubTask extends Task{
    private final String epicId;

    public SubTask(String name, String description, String epicId) {
        this(name, description, epicId, TaskStatus.NEW);
    }
    public SubTask(String name, String description, String epicId, TaskStatus taskStatus){
        super(name, description, taskStatus);

        this.epicId = epicId;
    }

    public String getEpicId() {
        return epicId;
    }
    @Override
    protected TaskType getTaskType(){
        return TaskType.SUBTASK;
    }
}
