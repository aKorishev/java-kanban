package taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.TaskManagerFactory;

import java.io.File;

class TaskManagerFactoryTest {
    @Test
    void getInMemoryTaskManagerNotNull() {

        try {
            Assertions.assertNotNull(TaskManagerFactory.initInMemoryTaskManager());
        } catch (Exception ex) {
            Assertions.fail();
        }
    }

    @Test
    void getFileBackedTaskManagerNotNull() {

        try {
            Assertions.assertNotNull(TaskManagerFactory.initFileBackedTaskManager(File.createTempFile("fileBacked", "test")));
        } catch (Exception ex) {
            Assertions.fail();
        }
    }
}