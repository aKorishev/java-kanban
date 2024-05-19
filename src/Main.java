import taskmanagers.TaskManager;
import taskmanagers.TaskManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) throws Exception {

        TaskManager taskManager = TaskManagerFactory.initInMemoryTaskManager();

        taskManager.createTask(new Task("Придумать реализацию", "Придумать собственную реализацию"));
        taskManager.createTask(new Task("Сдать на проверку", "Сдать на проверку"));
        taskManager.createTask(new Task("Получить первый ответ", "Получить неполные замечания, ломающию собственное виденье задачи"));

		Epic epic = new Epic("Задача 8 спринта", "Streams, datetime, optional");

        taskManager.createEpic(epic);
        int epicId = epic.getTaskId();

        taskManager.createSubTask(new SubTask("Написать код", "", epicId));
        taskManager.createSubTask(new SubTask("Покрыть тестами", "", epicId));
        taskManager.createSubTask(new SubTask("Получить отличную оценку","Даже волнительно, может быть оценка была не очень, но я же молодец, с другой стороны", epicId));

        printAllTasks(taskManager, 1);
    }

    private static void printAllTasks(TaskManager taskManager, int number) {
        System.out.println("\nВывод резульата №" + number);

        System.out.println("Tasks:");
        for (Task task : taskManager.getTasks())
            System.out.println("   " + task);

        System.out.println("Epics:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println("   " + epic);
            for (SubTask subTask : epic.getSubTasks())
                System.out.println("      " + subTask);
        }
    }
}

