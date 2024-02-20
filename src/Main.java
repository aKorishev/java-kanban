import TaskManager.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.putTask(new Task("Придумать кейсы", "Придумать модель поведения пользователя", TaskManager.incrementLastNumber()));
        taskManager.putTask(new Task("Реализовать действия пользователя", "Действия пользователя обозвать методами и реализовать в них функциональность", TaskManager.incrementLastNumber()));

        int taskId = TaskManager.incrementLastNumber();
        taskManager.putTask(new Task("Сдать работу", "Отправить архив с кодом ревьюверу", taskId));

        taskManager.update(new Task("Сдать работу", "Закомитить код в git", taskId));

        System.out.println("Список задач типа TASK:");
        for(Task task: taskManager.getAllTasks(TaskType.TASK)){
            System.out.println(task);
        }
        System.out.println("\n");

        Epic epicPull = new Epic("Стянуть", "Стянуть проект из гита", TaskManager.incrementLastNumber());
        taskManager.putEpic(epicPull);

        Epic epicCreateClasses = new Epic("Классы", "Создать структуру классов", TaskManager.incrementLastNumber());
        taskManager.putEpic(epicCreateClasses);

        Epic epicCreateImplements = new Epic("Реализация", "Реализация методов", TaskManager.incrementLastNumber());
        taskManager.putEpic(epicCreateClasses);

        Epic epicCreateTesting = new Epic("Тестирование", "Проверка кода через тестирование", TaskManager.incrementLastNumber());
        Epic epicDebug = new Epic("Дебаг", "Дабаггинг кода методовм Main", TaskManager.incrementLastNumber());

        Epic epicPublish = new Epic("Сдача", "Сдача задания", TaskManager.incrementLastNumber());

        taskManager.putSubTask(epicPull.getTaskId(), new SubTask("Авторизация","Авторизоваться на гите", TaskManager.incrementLastNumber()));
        taskManager.putSubTask(epicPull.getTaskId(), new SubTask("Клонирование","Клонирование проекта", TaskManager.incrementLastNumber()));

        taskManager.putSubTask(epicCreateClasses.getTaskId(), new SubTask("Классы","Создание струтктуры классов", TaskManager.incrementLastNumber()));

        taskManager.putSubTask(epicCreateImplements.getTaskId(), new SubTask("Класс Task","Реализация классов задач и методов", TaskManager.incrementLastNumber()));

        taskManager.putSubTask(epicCreateImplements.getTaskId(), new SubTask("Класс TaskManager","Реализация методов менеджера", TaskManager.incrementLastNumber()));

        SubTask subTaskOfTestTasks = new SubTask("Тест классов", "Отладка методов через тестирование функциональности класса Task", TaskManager.incrementLastNumber());
        SubTask subTaskOfTestTaskManager = new SubTask("Тест менеджера", "Отладка методов через тестирование йункциональности класса TaskManager", TaskManager.incrementLastNumber());

        taskManager.putEpic(epicCreateTesting);
        taskManager.putSubTask(epicCreateTesting.getTaskId(), subTaskOfTestTasks);
        taskManager.putSubTask(epicCreateTesting.getTaskId(), subTaskOfTestTaskManager);

        taskManager.putEpic(epicPublish);

        System.out.println("Эпики:");
        for(Epic epic : taskManager.getEpics()){
            System.out.println(epic);

            for(SubTask subTask: epic.subTasks())
                System.out.println("   " + subTask);
        }
        System.out.println("Кол-во эпиков - " + taskManager.getEpics().size() + ", кол-во подзадач - " + taskManager.getSubTasks().size());
        System.out.println("\n");

        taskManager.removeTask(epicCreateTesting.getTaskId());
        taskManager.putEpic(epicDebug);

        System.out.println("Эпики (дебаг):");
        for(Epic epic : taskManager.getEpics()){
            System.out.println(epic);

            for(SubTask subTask: epic.subTasks())
                System.out.println("   " + subTask);
        }
        System.out.println("Кол-во эпиков - " + taskManager.getEpics().size() + ", кол-во подзадач - " + taskManager.getSubTasks().size());
    }


}
