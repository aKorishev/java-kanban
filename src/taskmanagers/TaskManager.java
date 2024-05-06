package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public interface TaskManager {
    Collection<Task> getHistory();

    ArrayList<Epic> getEpics();
    ArrayList<Task> getTasks();
    ArrayList<SubTask> getSubTasks();
    Optional<ArrayList<SubTask>> getSubTasks(int epicId);

    Optional<Task> getTask(int taskId);
    Optional<Epic> getEpic(int epicId);
    Optional<SubTask> getSubTask(int subTaskId);

    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubTasks();

    void removeTask(int taskId);
    void removeEpic(int epicId);
    void removeSubTask(int subTaskId);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubTask(SubTask subTask);

    Optional<Integer> createTask(Task task);
    Optional<Integer> createEpic(Epic epic);
    Optional<Integer> createSubTask(SubTask subTask) throws Exception;
}
