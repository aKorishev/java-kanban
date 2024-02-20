import TaskManager.*;

import java.util.ArrayList;


public class SandBoxOfTaskManager {

    public static void main(String[] args) {
        try {
            checkCreatedTaskManager();

            checkIncrementTaskId();

            checkToAddTasks();
            checkToAddDoubleTasks();

            checkClearAllTasks();

            checkGetForTaskId();

            checkRemoveTask();

            checkUpdateTask();
        } catch (Exception e) {
            System.out.println("Тесты упали");

            throw new RuntimeException(e);
        }

        System.out.println("Done");
    }

    private static void checkCreatedTaskManager() throws Exception {
        TaskManager taskManager = new TaskManager();

        int numberOfEpics = taskManager.getEpics().size();

        if (numberOfEpics != 0)
            throw new Exception("Кол-во эпиков при создании менеджера должно быть 0, а сейчас " + numberOfEpics);

        int numberOfTasks = taskManager.getTasks().size();

        if (numberOfTasks != 0)
            throw new Exception("Кол0во эпиков при создании менеджера должно быть 0, а сейчас " + numberOfTasks);

        int numberOfSubTasks = taskManager.getSubTasks().size();

        if (numberOfSubTasks != 0)
            throw new Exception("Кол0во эпиков при создании менеджера должно быть 0, а сейчас " + numberOfSubTasks);
    }

    private static void checkIncrementTaskId() throws Exception {
        int numberOne = TaskManager.incrementLastNumber();

        if (numberOne != 1)
            throw new Exception("Первый таскИД должен был быть равен 1. Сейчас равен " + numberOne);

        for (int i = 2; i < 100; i++) {
            int id = TaskManager.incrementLastNumber();

            if (id != i)
                throw new Exception("Инкремент должен быть равен шагу. Шаг = " + i + ", инкремент = " + id);
        }
    }
    private static void checkToAddTasks() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic2);
        Epic epic3 = new Epic("Третий эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic3);
        Epic epic4 = new Epic("Четвертый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic4);
        Epic epic5 = new Epic("Пятый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());

        SubTask subTask = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("идти", "Он пошел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("прыгнуть", "Он прыгнул", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("лететь", "Он летел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic5.getTaskId(),subTask);

        taskManager.putTask(new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Второй Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Третий Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Четвертый Task", "Task для теста создания", TaskManager.incrementLastNumber()));

        int numberOfTasks = taskManager.getTasks().size();
        if (numberOfTasks != 4)
            throw new Exception("Кол-во задач должно быть равно 4, а оно равно " + numberOfTasks);

        int numberOfSubTasks = taskManager.getSubTasks().size();
        if (numberOfSubTasks != 5)
            throw new Exception("Кол-во подзадач должно быть равно 5, а оно равно " + numberOfSubTasks);

        int numberOfEpics = taskManager.getEpics().size();
        if (numberOfEpics != 4)
            throw new Exception("Кол-во эпиков должно быть равно 4, а оно равно " + numberOfEpics);

        int numberOfAllTasks = taskManager.getAllTasks().size();
        if (numberOfAllTasks != 13)
            throw new Exception("Кол-во задач всех типов должно быть равно 13, а оно равно " + numberOfEpics);
    }

    private static void checkToAddDoubleTasks() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic2);

        taskManager.putEpic(epic2);

        int numberOfEpics = taskManager.getEpics().size();
        if (numberOfEpics != 2)
            throw new Exception("Кол-во эпиков должно быть равно 2, а оно равно " + numberOfEpics);

        Epic epic3 = new Epic("Третий эпик", "эпик для теста создания", epic1.getTaskId());
        taskManager.putEpic(epic3);

        if (taskManager.getEpics().size() != 2)
            throw new Exception("Добавили 2 эпика с одиним ИД");

        Task task = new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber());
        taskManager.putTask(task);

        int taskId = task.getTaskId();

        task = new Task("Второй Task", "Task для теста создания", taskId);
        taskManager.putTask(task);

        if (taskManager.getTasks().size() != 1)
            throw new Exception("Добавили 2 задачи с одиним ИД");

        task = new Task("Второй Task", "Task для теста создания", epic2.getTaskId());
        taskManager.putTask(task);

        if (taskManager.getTasks().size() != 1)
            throw new Exception("Добавили 2 задачи с одним ИД");

        SubTask subTask = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        int subTaskId = subTask.getTaskId();

        subTask = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("сесть", "Он сел", taskId);
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        if (taskManager.getSubTasks().size() != 2)
            throw new Exception("Добавили 2 подзадачи с одним ИД");

        subTask = new SubTask("сесть", "Он сел", subTaskId);
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        if (taskManager.getSubTasks().size() != 2)
            throw new Exception("Добавили 2 подзадачи с одним ИД");

        subTask = new SubTask("сесть", "Он сел", epic2.getTaskId());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        if (taskManager.getSubTasks().size() != 2)
            throw new Exception("Добавили 2 подзадачи с одним ИД");

        subTask = new SubTask("сесть", "Он сел", epic1.getTaskId());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        if (taskManager.getSubTasks().size() != 2)
            throw new Exception("Добавили 2 подзадачи с одним ИД");
    }

    private static void checkClearAllTasks() throws Exception {
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic2);
        Epic epic3 = new Epic("Третий эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic3);
        Epic epic4 = new Epic("Четвертый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic4);

        SubTask subTask = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("идти", "Он пошел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("прыгнуть", "Он прыгнул", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("лететь", "Он летел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic3.getTaskId(),subTask);

        taskManager.putTask(new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Второй Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Третий Task", "Task для теста создания", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Четвертый Task", "Task для теста создания", TaskManager.incrementLastNumber()));

        taskManager.clearAllTasks();

        if (!taskManager.getAllTasks().isEmpty())
            throw new Exception("Список задач должен быть пустым");
    }

    public static void checkGetForTaskId() throws Exception {
        TaskManager taskManager = new TaskManager();

        ArrayList<Task> tasks = new ArrayList<>();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        tasks.add(epic1);
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        tasks.add(epic2);
        taskManager.putEpic(epic2);

        SubTask subTask = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic1.getTaskId(),subTask);

        subTask = new SubTask("идти", "Он пошел", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("прыгнуть", "Он прыгнул", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        subTask = new SubTask("лететь", "Он летел", TaskManager.incrementLastNumber());
        tasks.add(subTask);
        taskManager.putSubTask(epic2.getTaskId(),subTask);

        Task task = new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task);
        taskManager.putTask(task);

        task = new Task("Второй Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task);
        taskManager.putTask(task);

        task = new Task("Третий Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task);
        taskManager.putTask(task);

        task = new Task("Четвертый Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task);
        taskManager.putTask(task);


        for(Task taskI : tasks){
            int taskId = taskI.getTaskId();
            task = taskManager.getTask(taskId);

            if (task == null)
                throw new Exception("Ожидается задача: '" + taskI + "', по факту: 'null'");
            if (!taskI.equals(task))
                throw new Exception("Ожидается задача: '" + taskI + "', по факту: '" + task + "'");
        }
    }
    private static void checkRemoveTask() throws Exception {
        TaskManager taskManager = new TaskManager();

        ArrayList<Task> tasks = new ArrayList<>();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        tasks.add(epic1);
        taskManager.putEpic(epic1);

        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        tasks.add(epic2);
        taskManager.putEpic(epic2);

        Epic epic3 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        tasks.add(epic3);
        taskManager.putEpic(epic3);

        SubTask subTask1 = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        tasks.add(subTask1);
        taskManager.putSubTask(epic1.getTaskId(),subTask1);

        SubTask subTask2 = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        tasks.add(subTask2);
        taskManager.putSubTask(epic1.getTaskId(),subTask2);

        SubTask subTask3 = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        tasks.add(subTask3);
        taskManager.putSubTask(epic1.getTaskId(),subTask3);

        SubTask subTask4 = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());
        tasks.add(subTask4);
        taskManager.putSubTask(epic2.getTaskId(),subTask4);

        Task task1 = new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task1);
        taskManager.putTask(task1);

        Task task2 = new Task("Второй Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task2);
        taskManager.putTask(task2);

        Task task3 = new Task("Третий Task", "Task для теста создания", TaskManager.incrementLastNumber());
        tasks.add(task3);
        taskManager.putTask(task3);

        taskManager.removeTask(epic3.getTaskId());
        int numberOfEpics = taskManager.getEpics().size();
        if (numberOfEpics != 2)
            throw new Exception("Ожидается кол-во эпиков = 2, по факту: " + numberOfEpics);

        taskManager.removeTask(task2.getTaskId());
        int numberOfTasks = taskManager.getTasks().size();
        if (numberOfTasks != 2)
            throw new Exception("Ожидается кол-во задач = 2, по факту: " + numberOfTasks);

        taskManager.removeTask(subTask2.getTaskId());
        int numberOfSubTasks = taskManager.getSubTasks().size();
        if (numberOfSubTasks != 3)
            throw new Exception("Ожидается кол-во подзадач = 3, по факту: " + numberOfTasks);

        numberOfSubTasks = epic1.subTasks().size();
        if (numberOfSubTasks != 2)
            throw new Exception("Ожидается кол-во подзадач = 2, по факту: " + numberOfTasks);

        taskManager.removeTask(epic2.getTaskId());
        numberOfEpics = taskManager.getEpics().size();
        if (numberOfEpics != 1)
            throw new Exception("Ожидается кол-во эпиков = 1, по факту: " + numberOfEpics);

        numberOfSubTasks = taskManager.getSubTasks().size();
        if (numberOfSubTasks != 2)
            throw new Exception("Ожидается кол-во подзадач = 2, по факту: " + numberOfSubTasks);
    }
    private static void checkUpdateTask(){
        TaskManager taskManager = new TaskManager();

        Epic epic1 = new Epic("Первый эпик", "эпик для теста создания", TaskManager.incrementLastNumber());
        taskManager.putEpic(epic1);

        Epic epic2 = new Epic("Второй эпик", "эпик для теста создания", TaskManager.incrementLastNumber());

        SubTask subTask1 = new SubTask("встать", "Он встал", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask1);

        SubTask subTask2 = new SubTask("сесть", "Он сел", TaskManager.incrementLastNumber());
        taskManager.putSubTask(epic1.getTaskId(),subTask2);

        SubTask subTask1_2 = new SubTask("лечь", "Он лег", subTask1.getTaskId());

        SubTask subTask4 = new SubTask("лечь", "Он лег", TaskManager.incrementLastNumber());

        Task task1 = new Task("Первый Task", "Task для теста создания", TaskManager.incrementLastNumber());
        taskManager.putTask(task1);

        Task task2 = new Task("Второй Task", "Task для теста создания", TaskManager.incrementLastNumber());
        taskManager.putTask(task2);

        Task task1_2 = new Task("Третий Task", "Task для теста создания", TaskManager.incrementLastNumber());


    }
}
