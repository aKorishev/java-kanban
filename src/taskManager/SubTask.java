package taskManager;

public class SubTask extends Task{
    private final Epic epic;
    SubTask(String name, String description, int taskId, Epic epic) {
        super(name, description, taskId);

        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public SubTask updateName(String name){
        int subTaskId = getTaskId();
        SubTask subTask = new SubTask(name, getDescription(), subTaskId, epic);

        epic.putSubTask(subTaskId, subTask);

        return subTask;
    }
    @Override
    public SubTask updateDescription(String description){
        int subTaskId = getTaskId();
        SubTask subTask = new SubTask(getName(), description, getTaskId(), epic);

        epic.updateSubTask(subTask);

        return subTask;
    }
    public SubTask updateEpic(Epic newEpic){
        int subTaskId = getTaskId();
        SubTask subTask = new SubTask(getName(), getDescription(), getTaskId(), newEpic);

        newEpic.putSubTask(subTaskId, subTask);
        epic.removeSubTask(subTaskId);

        return subTask;
    }
    @Override
    public SubTask update(String name, String description){
        int subTaskId = getTaskId();
        SubTask subTask = new SubTask(name, description, subTaskId, epic);

        epic.updateSubTask(subTask);

        return subTask;
    }
    public SubTask update(String name, String description, Epic newEpic){
        int subTaskId = getTaskId();
        SubTask subTask = new SubTask(name, description, subTaskId,newEpic);

        newEpic.putSubTask(subTaskId, subTask);
        epic.removeSubTask(subTaskId);

        return subTask;
    }

    @Override
    protected TaskType getTaskType(){
        return TaskType.SUBTASK;
    }
}
