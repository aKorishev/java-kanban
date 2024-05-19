package tests.tasks;

import enums.TaskType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.SubTask;

class SubTaskTest {

    @Test
    void getEpicId() {
        SubTask subTask = new SubTask("name", "description", 100);

        Assertions.assertEquals(100, subTask.getEpicId());
    }

    @Test
    void setEpicId() {
        SubTask subTask = new SubTask("name", "description", 0);

        subTask.setEpicId(10);

        Assertions.assertEquals(10, subTask.getEpicId());
    }

    @Test
    void getTaskType() {
        SubTask subTask = new SubTask("name", "description", 0);

        Assertions.assertEquals(TaskType.SUBTASK, subTask.getTaskType());
    }
}