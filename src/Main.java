import taskManager.*;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            TaskManager taskManager = new TaskManager();

            debugTasks(taskManager);
            debugTasksOfDone(taskManager);

            int epicCreateTestingId = debugEpicsUsedTests(taskManager);

            int epicDebugId = replaceTestOnDebug(taskManager, epicCreateTestingId);

            int epicImplementionId = updateImplemention(taskManager);
        }
        catch (Exception ex){
            throw ex;
        }
    }

    private static void debugTasks(TaskManager taskManager){
        taskManager.createTask("Придумать кейсы", "Придумать модель поведения пользователя");
        taskManager.createTask("Реализовать действия пользователя", "Действия пользователя обозвать методами и реализовать в них функциональность");

        int taskId = taskManager.createTask("Сдать работу", "Отправить архив с кодом ревьюверу");

        System.out.println("1. Список задач типа TASK:");
        for (Task task : taskManager.getAllTasks(TaskType.TASK)) {
            System.out.println(task);
        }
        System.out.println("\n");

        Task taskEndedOfWork = taskManager.getTask(taskId).updateDescription("Закомитить код в git");

        taskManager.updateTask(taskEndedOfWork);

        System.out.println("2. Список задач типа TASK:");
        for (Task task : taskManager.getAllTasks(TaskType.TASK)) {
            System.out.println(task);
        }
        System.out.println("\n");
    }

    private static void debugTasksOfDone(TaskManager taskManager){
        for(Task task : taskManager.getTasks())
            task.doInDone();

        System.out.println("3. Список задач типа TASK:");
        for (Task task : taskManager.getAllTasks(TaskType.TASK)) {
            System.out.println(task);
        }
        System.out.println("\n");
    }

    private static int debugEpicsUsedTests(TaskManager taskManager) throws Exception {
        int epicPullId = taskManager.createEpic("Стянуть", "Стянуть проект из гита");
        taskManager.createSubTask("Авторизация", "Авторизоваться на гите", epicPullId);
        taskManager.createSubTask("Клонирование", "Клонирование проекта", epicPullId);

        int epicCreateClassesId = taskManager.createEpic("Классы", "Создать структуру классов");
        taskManager.createSubTask("Классы", "Создание струтктуры классов", epicCreateClassesId);

        int epicCreateImplementsId = taskManager.createEpic("Реализация", "Реализация методов");
        taskManager.createSubTask("Класс Task", "Реализация классов задач и методов", epicCreateImplementsId);
        taskManager.createSubTask("Класс TaskManager", "Реализация методов менеджера", epicCreateImplementsId);

        int epicCreateTestingId = taskManager.createEpic("Тестирование", "Проверка кода через тестирование");
        taskManager.createSubTask("Тест классов", "Отладка методов через тестирование функциональности класса Task", epicCreateTestingId);
        taskManager.createSubTask("Тест менеджера", "Отладка методов через тестирование йункциональности класса TaskManager", epicCreateTestingId);

        taskManager.createEpic("Сдача", "Сдача задания");

        System.out.println("4. Список задач типа Epic:");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (SubTask subTask : epic.getSubTasks())
                System.out.println("   " + subTask);
        }
        System.out.println("Кол-во эпиков - " + taskManager.getEpics().size() + ", кол-во подзадач - " + taskManager.getSubTasks().size());
        System.out.println("\n");

        return epicCreateTestingId;
    }

    private static int replaceTestOnDebug(TaskManager taskManager, int epicCreateTestingId) throws Exception {
        taskManager.removeEpic(epicCreateTestingId);

        int epicDebugId = taskManager.createEpic("Дебаг", "Дабаггинг кода методовм Main");
        taskManager.createSubTask("Реализация действий пользователя", "Реализация действий пользователя в Main", epicDebugId);

        System.out.println("5. Список задач типа Epic:");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (SubTask subTask : epic.getSubTasks())
                System.out.println("   " + subTask);
        }
        System.out.println("Кол-во эпиков - " + taskManager.getEpics().size() + ", кол-во подзадач - " + taskManager.getSubTasks().size());

        return epicDebugId;
    }

    private static int updateImplemention(TaskManager taskManager) throws Exception {
        int implementId = 0;

        for (Epic epic: taskManager.getEpics())
            if (epic.getName().equals("Реализация")) {
                implementId = epic.getTaskId();
                break;
            }
        if (implementId == 0) return 0;

        Epic implementEpic = taskManager.getEpic(implementId).updateDescription("Исправление после ревью");
        taskManager.updateEpic(implementEpic);
        taskManager.createSubTask("Метод update", "Реализация обновлений задач", implementEpic);

        System.out.println("6. Список задач типа Epic:");
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (SubTask subTask : epic.getSubTasks())
                System.out.println("   " + subTask);
        }
        System.out.println("Кол-во эпиков - " + taskManager.getEpics().size() + ", кол-во подзадач - " + taskManager.getSubTasks().size());

        return implementId;
    }
}
