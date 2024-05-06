package taskmanagers;

import historyManagers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TaskManagerWithHistory implements TaskManager {
    private final HistoryManager historyManager;
    private final TaskManager taskManagerBase;

    public TaskManagerWithHistory(TaskManager taskManagerBase, HistoryManager historyManager){
        this.taskManagerBase = taskManagerBase;
        this.historyManager = historyManager;
    }
    @Override
    public List<Epic> getEpics() {
        List<Epic> epics = taskManagerBase.getEpics();
        addInHistory(epics);
        return epics;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = taskManagerBase.getTasks();
        addInHistory(tasks);
        return tasks;
    }

    @Override
    public List<SubTask> getSubTasks() {
        List<SubTask> subTasks = taskManagerBase.getSubTasks();
        addInHistory(subTasks);
        return subTasks;
    }

    @Override
    public List<SubTask> getSubTasks(int epicId){
        List<SubTask> subTasks = taskManagerBase.getSubTasks(epicId);
        addInHistory(subTasks);
        return subTasks;
    }

    @Override
    public Task getTask(int taskId){
        Task task = taskManagerBase.getTask(taskId);

        if (task != null) {
            addInHistory(task);
        }

        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = taskManagerBase.getEpic(epicId);

        if (epic != null) {
            addInHistory(epic);
        }

        return epic;
    }
    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = taskManagerBase.getSubTask(subTaskId);

        if (subTask != null) {
            addInHistory(subTask);
        }

        return subTask;
    }

    @Override
    public void clearAllTasks(){
        for(Task task : taskManagerBase.getTasks()) {
            removeFromHistory(task);
        }

        taskManagerBase.clearAllTasks();
    }
    @Override
    public void clearAllEpics(){
        for(Epic epic : taskManagerBase.getEpics()) {
            removeFromHistory(epic);
        }

        taskManagerBase.clearAllEpics();
    }
    @Override
    public void clearAllSubTasks(){
        for(SubTask subTask : taskManagerBase.getSubTasks()) {
            removeFromHistory(subTask);
        }

        taskManagerBase.clearAllSubTasks();
    }
    @Override
    public void removeTask(int taskId){
        Task task = taskManagerBase.getTask(taskId);

        if (task == null) {
            return;
        }

        removeFromHistory(task);
        taskManagerBase.removeTask(taskId);
    }
    @Override
    public void removeEpic(int epicId){
        Epic epic = taskManagerBase.getEpic(epicId);

        if (epic == null) {
            return;
        }

        for(SubTask subTask : epic.getSubTasks()) {
            removeFromHistory(subTask);
        }

        removeFromHistory(epic);
        taskManagerBase.removeEpic(epicId);
    }
    @Override
    public void removeSubTask(int subTaskId){
        SubTask subTask = taskManagerBase.getSubTask(subTaskId);

        if (subTask == null) {
            return;
        }

        removeFromHistory(subTask);
        taskManagerBase.removeSubTask(subTaskId);
    }

    @Override
    public List<Epic> getPrioritizedEpics(int route) {
        return taskManagerBase.getPrioritizedEpics(route);
    }

    @Override
    public List<SubTask> getPrioritizedSubTasks(int route) {
        return taskManagerBase.getPrioritizedSubTasks(route);
    }

    @Override
    public List<Task> getPrioritizedTasks(int route) {
        return taskManagerBase.getPrioritizedTasks(route);
    }

    @Override
    public void updateTask(Task task){
        taskManagerBase.updateTask(task);
    }
    @Override
    public void updateEpic(Epic epic) {
        taskManagerBase.updateEpic(epic);
    }
    @Override
    public void updateSubTask(SubTask subTask){
        taskManagerBase.updateSubTask(subTask);
    }

    @Override
    public int createTask(Task task){
        return taskManagerBase.createTask(task);
    }
    @Override
    public int createEpic(Epic epic){
        return taskManagerBase.createEpic(epic);
    }
    @Override
    public int createSubTask(SubTask subTask) throws Exception {
        return taskManagerBase.createSubTask(subTask);
    }

    @Override
    public void setTaskDuration(int taskId, Duration duration) {
        taskManagerBase.setTaskDuration(taskId, duration);
    }

    @Override
    public void setEpicDuration(int epicId, Duration duration) {
        taskManagerBase.setTaskDuration(epicId, duration);
    }

    @Override
    public void setSubTaskDuration(int subTaskId, Duration duration) {
        taskManagerBase.setSubTaskDuration(subTaskId, duration);
    }

    @Override
    public void setTaskStartTime(int taskId, LocalDateTime startTime) {
        taskManagerBase.setTaskStartTime(taskId, startTime);
    }

    @Override
    public void setEpicStartTime(int epicId, LocalDateTime startTime) {
        taskManagerBase.setEpicStartTime(epicId, startTime);
    }

    @Override
    public void setSubTaskStartTime(int subTaskId, LocalDateTime startTime) {
        taskManagerBase.setSubTaskStartTime(subTaskId, startTime);
    }

    @Override
    public List<Task> getHistory(){

        return historyManager.getHistory();
    }

    private <T extends Task> void addInHistory(List<T> tasks) {
        for(Task task : tasks) {
            historyManager.add(task);
        }
    }
    private <T extends Task> void addInHistory(T task) { historyManager.add(task);}

    private <T extends Task>  void removeFromHistory(T task) { historyManager.remove(task.getTaskId()); }
}
