package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    Collection<Task> getHistory();

    ArrayList<Epic> getEpics();
    ArrayList<Task> getTasks();
    ArrayList<SubTask> getSubTasks();
    ArrayList<SubTask> getSubTasks(int epicId);

    Task getTask(int taskId);
    Epic getEpic(int epicId);
    SubTask getSubTask(int subTaskId);

    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubTasks();

    void removeTask(int taskId);
    void removeEpic(int epicId);
    void removeSubTask(int subTaskId);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTask(SubTask subTask);

    int createTask(Task task);
    int createEpic(Epic epic);
    int createSubTask(SubTask subTask) throws Exception;
}
