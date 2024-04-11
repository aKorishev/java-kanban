package tests.taskManagers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanagers.InMemoryTaskManager;
import taskmanagers.TaskManagerFactory;

class TaskManagerFactoryTest {
    @Test
    void getInMemoryTaskManagerNotNull() {

        try{
            TaskManagerFactory.initInMemoryTaskManager().toString();
        }catch (Exception ex){
            Assertions.fail();
        }
    }
}