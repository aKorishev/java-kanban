package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskManagerFactory;

class TaskManagerFactoryTest {

    @Test
    void getInMemoryTaskManager() {
        TaskManagerFactory taskManagerFactory = new TaskManagerFactory();

        Assertions.assertTrue(taskManagerFactory.initInMemoryTaskManager() instanceof InMemoryTaskManager);
    }
    @Test
    void getInMemoryTaskManagerNotNull() {
        TaskManagerFactory taskManagerFactory = new TaskManagerFactory();

        try{
            taskManagerFactory.initInMemoryTaskManager().toString();
        }catch (Exception ex){
            Assertions.fail();
        }
    }
}