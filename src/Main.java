import taskmanagers.TaskManager;
import taskmanagers.OtherManagerFactory;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {

    public static void main(String[] args) throws Exception {

        TaskManager taskManager = OtherManagerFactory.getDefault();

        TaskTree taskTree = createDifferentTasks(taskManager);
        printAllTasks(taskManager, 1);

        checkGetHistory(taskManager, taskTree);
        printAllTasks(taskManager,2);
    }

    private static TaskTree createDifferentTasks(TaskManager taskManager) throws Exception {
        TaskTree tree = new TaskTree();

        tree.taskId1 = taskManager.createTask(new Task("Придумать реализацию", "Придумать собственную реализацию"));
        tree.taskId2 = taskManager.createTask(new Task("Сдать на проверку", "Сдать на проверку"));
        tree.taskId3 = taskManager.createTask(new Task("Получить первый ответ", "Получить неполные замечания, ломающию собственное виденье задачи"));

        tree.epicId1 = taskManager.createEpic(new Epic("Сдать вторую реализацию", "Подготовить реализацию в соответсвиями с первыми замечаниями"));
        tree.subTaskId1_1 = taskManager.createSubTask(new SubTask("Додумать замечания","Додумать значения замечаний, т.к. текс их был не полный", tree.epicId1));
        tree.subTaskId2_1 = taskManager.createSubTask(new SubTask("Реализовать новые методы", "Подумал, что достаточно будет реализовать новые методы", tree.epicId1));
        tree.subTaskId3_1 = taskManager.createSubTask(new SubTask("Сдать на вторую проверку", "Сдать на вторую проверку", tree.epicId1));
        tree.subTaskId4_1 = taskManager.createSubTask(new SubTask("Получить второй ответ", "Опять получил замечания", tree.epicId1));

        tree.epicId2 = taskManager.createEpic(new Epic("Сдать третью реализацию", "Сдать третью реализацию"));
        tree.subTaskId1_2 = taskManager.createSubTask(new SubTask("Прочитал замечания", "Убедился, что моя реализация не нужна, нужно полное соответсвие с версией для проверки", tree.epicId2));
        tree.subTaskId2_2 = taskManager.createSubTask(new SubTask("Исправил все замечания", "Без оглядки на свою реализцию. исправил все замечания", tree.epicId2));
        tree.subTaskId3_2 = taskManager.createSubTask(new SubTask("Сдал на треть проверку", "Сдал на треть проверку", tree.epicId2));
        tree.subTaskId4_2 = taskManager.createSubTask(new SubTask("Получить третий ответ", "Опять получил замечания", tree.epicId1));

        tree.epicId3 = taskManager.createEpic(new Epic("Сдать 4 раз", "Очередная попытка"));
        tree.subTaskId1_3 = taskManager.createSubTask(new SubTask("Ознакомиться с замечаниями", "", tree.epicId3));
        tree.subTaskId2_3 = taskManager.createSubTask(new SubTask("Понять, что выполнил не все замечания", "", tree.epicId3));
        tree.subTaskId3_3 = taskManager.createSubTask(new SubTask("Написать в личку", "Чтобы понять какой нужно результат написал в личку смотрящему", tree.epicId3));
        tree.subTaskId4_3 = taskManager.createSubTask(new SubTask("Сдал 4 раз", "", tree.epicId3));
        tree.subTaskId5_3 = taskManager.createSubTask(new SubTask("Получил 4 отказ", "", tree.epicId3));

        tree.epicId4 = taskManager.createEpic(new Epic("Сдать 5 раз", ""));
        tree.subTaskId1_4 = taskManager.createSubTask(new SubTask("Ознакомится с замечаниями", "", tree.epicId4));
        tree.subTaskId2_4 = taskManager.createSubTask(new SubTask("Написать в личку за разяснениями", "", tree.epicId4));
        tree.subTask3_4 = taskManager.createSubTask(new SubTask("Сдать работу", "", tree.epicId4));

        return tree;
    }

    private static void printAllTasks(TaskManager taskManager, int number) {
        System.out.println("\nВывод резульата №" + number);

        System.out.println("Tasks:");
        for(Task task : taskManager.getTasks())
            System.out.println("   " + task);

        System.out.println("Epics:");
        for(Epic epic : taskManager.getEpics()) {
            System.out.println("   " + epic);
            for(SubTask subTask : epic.getSubTasks())
                System.out.println("      " + subTask);
        }
    }

    private static void checkGetHistory(TaskManager taskManager, TaskTree tree) throws Exception {
        Epic epic = taskManager.getEpic(tree.epicId4);

        taskManager.createSubTask(new SubTask("Убедиться в успехе", "", epic.getTaskId()));

        taskManager.getTask(tree.taskId2);
        taskManager.getSubTask(tree.subTaskId1_3);

        System.out.println("Печать истории");
        for(Task task : taskManager.getHistory())
            System.out.println("   " + task);
    }


}

class TaskTree{
    public int taskId1;
    public int taskId2;
    public int taskId3;

    public int epicId1;
    public int subTaskId1_1;
    public int subTaskId2_1;
    public int subTaskId3_1;
    public int subTaskId4_1;

    public int epicId2;
    public int subTaskId1_2;
    public int subTaskId2_2;
    public int subTaskId3_2;
    public int subTaskId4_2;

    public int epicId3;
    public int subTaskId1_3;
    public int subTaskId2_3;
    public int subTaskId3_3;
    public int subTaskId4_3;
    public int subTaskId5_3;

    public int epicId4;
    public int subTaskId1_4;
    public int subTaskId2_4;
    public int subTask3_4;
}
