package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskManager {
    List<Task> getHistory();

    List<Epic> getEpics();
    List<Task> getTasks();
    List<SubTask> getSubTasks();
    Optional<List<SubTask>> getSubTasks(int epicId);

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

    void setTaskDuration(int taskId, Duration duration);
    void setEpicDuration(int epicId, Duration duration);
    void setSubTaskDuration(int subTaskId, Duration duration);

    void setTaskStartTime(int taskId, LocalDateTime startTime);
    void setEpicStartTime(int epicId, LocalDateTime startTime);
    void setSubTaskStartTime(int subTaskId, LocalDateTime startTime);

    List<Task> getPrioritizedTasks(int route);
    List<Epic> getPrioritizedEpics(int route);
    List<SubTask> getPrioritizedSubTasks(int route);
}
