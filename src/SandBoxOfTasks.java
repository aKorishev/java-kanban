import TaskManager.*;

import java.util.ArrayList;

public class SandBoxOfTasks {

    public static void main(String[] args) {
        try {
            checkInitTask();
            checkInitEpic();
            checkInitSubTask();
            checkDoChangeTask();

            checkPutSubTaskInEpic();
            checkExchangeSubTaskInEpic();

            checkDoneEpic();
        } catch (Exception e) {
            System.out.println("Тесты упали");

            throw new RuntimeException(e);
        }

        System.out.println("Done");
    }

    private static void checkInitTask() throws Exception {
        String name = "name";
        String description = "description";
        int taskId = 1;

        Task task = new Task(name, description, taskId);

        if (!name.equals(task.getName()))
            throw new Exception("name задачи должно быть равно '" + name + "' , а оно равно '" + task.getName() + "'");

        if (!description.equals(task.getDescription()))
            throw new Exception("description задачи должно быть равно '" + description + "' , а оно равно '" + task.getDescription() + "'");

        if (taskId != task.getTaskId())
            throw new Exception("taskId задачи должно быть равно '" + taskId + "' , а оно равно '" + task.getTaskId() + "'");
    }
    private static void checkInitEpic() throws Exception {
        String name = "name";
        String description = "description";
        int taskId = 1;

        Epic epic = new Epic(name, description, taskId);

        if (!(epic instanceof Task))
            throw new Exception("Класс Epic должен наследовать Task");

        if (epic.subTasks().size() != 0)
            throw new Exception("Новый Epic не должен иметь подзадач");
    }
    private static void checkInitSubTask() throws Exception {
        String name = "name";
        String description = "description";
        int taskId = 1;

        SubTask subTask = new SubTask(name, description, taskId);

        if (!(subTask instanceof Task))
            throw new Exception("Класс SubTask должен наследовать Task");

        if (subTask.isHavingEpic())
            throw new Exception("Новая подзадача должна иметь пустой эпик");
    }

    private static void checkDoChangeTask() throws Exception {
        String name = "name";
        String description = "description";
        int taskId = 1;

        Task task = new Task(name, description, taskId);

        TaskStatus status = task.getTaskStatus();

        if (status != TaskStatus.NEW)
            throw new Exception("Задача при создании должна имет статус NEW, а сейчас это '" + status.name() + "'");

        task.doInProgress();
        status = task.getTaskStatus();
        if (status != TaskStatus.IN_PROGRESS)
            throw new Exception("Задача при создании должна имет статус IN_PROGRESS, а сейчас это '" + status.name() + "'");

        task.doInDone();
        status = task.getTaskStatus();
        if (status != TaskStatus.DONE)
            throw new Exception("Задача при создании должна имет статус DONE, а сейчас это '" + status.name() + "'");
    }

    private static void checkPutSubTaskInEpic() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic = new Epic("Эпик", "Большая подзадача", TaskManager.incrementLastNumber());
        int epicId = epic.getTaskId();
        taskManager.putEpic(epic);

        SubTask task1 = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        SubTask task2 = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        SubTask task3 = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());

        taskManager.putSubTask(epicId, task1);
        taskManager.putSubTask(epicId, task2);

        if (!task1.isHavingEpic() || !task2.isHavingEpic())
            throw new Exception("Задачи 1-2 должны иметь эпиков");
        if (task3.isHavingEpic())
            throw new Exception("Задача 3 не должна иметь эпика");
        if (epic.subTasks().size() != 2)
            throw new Exception("Эпик должен иметь 2 задачи");

        taskManager.removeTask(task1.getTaskId());
        if (task1.isHavingEpic())
            throw new Exception("Задача 1 не должна иметь эпиков");
        if (epic.subTasks().size() != 1)
            throw new Exception("Эпик должен иметь 1 задачу");
        if (taskManager.getSubTasks().size() != 1)
            throw new Exception("taskManager должен иметь 1 задачу");

        if (!epic.subTasks().toArray()[0].equals(task2))
            throw new Exception("Эпик должен иметь 1 задачу: " + task2.getName());

        taskManager.putSubTask(epicId, task3);
        if (!task3.isHavingEpic())
            throw new Exception("Задача 3 не должна иметь эпиков");
        if (epic.subTasks().size() != 2)
            throw new Exception("Эпик должен иметь 2 задачи");
    }

    private static void checkExchangeSubTaskInEpic() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Эпик", "Большая подзадача", TaskManager.incrementLastNumber());
        int epic1Id = epic1.getTaskId();
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic("Эрик второй", "Дополнительный эпик", TaskManager.incrementLastNumber());
        int epic2Id = epic2.getTaskId();
        taskManager.putEpic(epic2);

        SubTask task1 = new SubTask("втстать", "Он встал", TaskManager.incrementLastNumber());
        SubTask task2 = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        SubTask task3 = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        SubTask task4 = new SubTask("идти", "Он пошел", TaskManager.incrementLastNumber());
        SubTask task5 = new SubTask("бежать", "Он побежал", TaskManager.incrementLastNumber());
        SubTask task6 = new SubTask("упасть", "Он упал", TaskManager.incrementLastNumber());

        taskManager.putSubTask(epic1Id, task1);
        taskManager.putSubTask(epic1Id, task2);
        taskManager.putSubTask(epic2Id, task3);
        taskManager.putSubTask(epic2Id, task4);
        taskManager.putSubTask(epic2Id, task5);

        if (task6.isHavingEpic())
            throw new Exception("Задача 6 не должна иметь эпиков");

        if (epic1.subTasks().size() != 2)
            throw new Exception("Эпик1 должен иметь 2 задачи");
        if (epic2.subTasks().size() != 3)
            throw new Exception("Эпик2 должен иметь 3 задачи");

        taskManager.putSubTask(epic1Id, task6);
        if (epic1.subTasks().size() != 3)
            throw new Exception("Эпик1 должен иметь 3 задачи");

        taskManager.putSubTask(epic1Id, task6);
        if (epic1.subTasks().size() != 3)
            throw new Exception("Эпик1 должен иметь 3 задачи");

        taskManager.putSubTask(epic1Id, task4);

        if (epic1.subTasks().size() != 4)
            throw new Exception("Эпик1 должен иметь 4 задачи");

        taskManager.removeTask(task5.getTaskId());

        if (task5.isHavingEpic())
            throw new Exception("Задача 5 не должна иметь эпиков");

        ArrayList<Integer> subtasks = new ArrayList<>();
        subtasks.add(task1.getTaskId());
        subtasks.add(task2.getTaskId());
        subtasks.add(task6.getTaskId());
        subtasks.add(task4.getTaskId());

        for(SubTask subTask : epic1.subTasks()){
            Integer taskId = subTask.getTaskId();
            subtasks.remove(taskId);
        }
        if (!subtasks.isEmpty())
            throw new Exception("Ошибка подсчета задач в эпик1");

        if (epic2.subTasks().size() != 1)
            throw new Exception("Эпик2 должен иметь 1 задачу");
        if (!epic2.subTasks().toArray()[0].equals(task3))
            throw new Exception("Эпик2 должен иметь 1 задачу: " + task3.getName());

        taskManager.clearAllSubTasks(epic1);
        if (!epic1.subTasks().isEmpty())
            throw new Exception("Эпик1 должен иметь 0 задач");
        if(task1.isHavingEpic() || task2.isHavingEpic() || task6.isHavingEpic() || task4.isHavingEpic())
            throw new Exception("Задачи 1,2,4,6 не должна иметь эпика");
    }

    public static void checkDoneEpic() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic = new Epic("Эпик", "Большая подзадача", TaskManager.incrementLastNumber());
        int epicId = epic.getTaskId();
        taskManager.putEpic(epic);

        epic.doInDone();

        if (epic.getTaskStatus() != TaskStatus.DONE)
            throw new Exception("Эпик должен иметь статус done");

        SubTask task1 = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());

        taskManager.putSubTask(epicId, task1);

        if (epic.getTaskStatus() == TaskStatus.DONE)
            throw new Exception("Эпик не должен иметь статус done");

        task1.doInDone();
        if (epic.getTaskStatus() != TaskStatus.DONE)
            throw new Exception("Эпик должен иметь статус done");

        task1.doInProgress();

        if (task1.getTaskStatus() != TaskStatus.IN_PROGRESS)
            throw new Exception("Задача1 должен иметь статус IN_PROGRESS");
        if (epic.getTaskStatus() == TaskStatus.DONE)
            throw new Exception("Эпик не должен иметь статус done");

        SubTask task2 = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epicId, task2);

        task1.doInDone();
        if (epic.getTaskStatus() == TaskStatus.DONE)
            throw new Exception("Эпик не должен иметь статус done");

        task2.doInDone();
        if (epic.getTaskStatus() != TaskStatus.DONE)
            throw new Exception("Эпик должен иметь статус done");

        task1.doInProgress();
        if (epic.getTaskStatus() == TaskStatus.DONE)
            throw new Exception("Эпик не должен иметь статус done");
    }
}
