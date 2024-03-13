package tasks;

import enums.TaskStatus;
import enums.TaskType;

import java.util.Optional;

public class SubTask extends Task{
    private int epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description, TaskStatus.NEW);

        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
    public void setEpicId(Integer epicId) { this.epicId = epicId; }
    @Override
    public TaskType getTaskType(){
        return TaskType.SUBTASK;
    }
}
