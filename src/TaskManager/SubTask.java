package TaskManager;

public class SubTask extends Task{
    private Epic epic = null;
    public SubTask(String name, String description, int taskId) {
        super(name, description, taskId);
    }


    public boolean isHavingEpic(){
        return epic != null;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public void DoNew(){
        super.DoNew();

        if (isHavingEpic())
            epic.doInProgress();
    }
    @Override
    public void doInProgress(){
        super.doInProgress();

        if (isHavingEpic())
            epic.doInProgress();
    }
    @Override
    public void doInDone(){
        super.doInDone();

        if (isHavingEpic())
            epic.doInDone();
    }
    void setEpic(Epic epic) {
        this.epic = epic;
    }
    void clearEpic(){
        epic = null;
    }
    @Override
    protected TaskType getTaskType(){
        return TaskType.SUBTASK;
    }
}
