package tasks;

import enums.TaskStatus;
import enums.TaskType;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description, TaskStatus.NEW);

        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus taskStatus, Integer epicId) {
        super(name, description, taskStatus);

        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) return false;

        SubTask subTask = (SubTask) other;

        return epicId == subTask.epicId;
    }
}
