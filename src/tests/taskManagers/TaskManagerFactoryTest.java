package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerFactoryTest {

    @Test
    void getInMemoryTaskManager() {
        TaskManagerFactory taskManagerFactory = new TaskManagerFactory();

        Assertions.assertTrue(taskManagerFactory.getInMemoryTaskManager() instanceof InMemoryTaskManager);
    }
    @Test
    void getInMemoryTaskManagerNotNull() {
        TaskManagerFactory taskManagerFactory = new TaskManagerFactory();

        try{
            taskManagerFactory.getInMemoryTaskManager().toString();
        }catch (Exception ex){
            Assertions.fail();
        }
    }
}