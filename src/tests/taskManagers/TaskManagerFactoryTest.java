package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.OtherManagerFactory;

class TaskManagerFactoryTest {

    @Test
    void getInMemoryTaskManager() {

        Assertions.assertTrue(OtherManagerFactory.getDefault() instanceof InMemoryTaskManager);
    }
    @Test
    void getInMemoryTaskManagerNotNull() {

        try{
            OtherManagerFactory.getDefault().toString();
        }catch (Exception ex){
            Assertions.fail();
        }
    }
}