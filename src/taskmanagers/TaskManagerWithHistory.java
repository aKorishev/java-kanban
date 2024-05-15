package taskmanagers;

import historyManagers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Optional<List<SubTask>> getSubTasks(int epicId){
        var subTasks = taskManagerBase.getSubTasks(epicId);
        subTasks.ifPresent(this::addInHistory);
        return subTasks;
    }

    @Override
    public Optional<Task> getTask(int taskId){
        var task = taskManagerBase.getTask(taskId);

        task.ifPresent(this::addInHistory);

        return task;
    }

    @Override
    public Optional<Epic> getEpic(int epicId) {
        var epic = taskManagerBase.getEpic(epicId);

        epic.ifPresent(this::addInHistory);

        return epic;
    }
    @Override
    public Optional<SubTask> getSubTask(int subTaskId) {
        var subTask = taskManagerBase.getSubTask(subTaskId);

        subTask.ifPresent(this::addInHistory);

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
        var task = taskManagerBase.getTask(taskId);
        task.ifPresent(this::removeFromHistory);
        taskManagerBase.removeTask(taskId);
    }
    @Override
    public void removeEpic(int epicId){
        var epic = taskManagerBase.getEpic(epicId);

        epic.ifPresent(i ->{
            i.getSubTasks().forEach(this::removeFromHistory);
            removeFromHistory(i);
        });

        taskManagerBase.removeEpic(epicId);
    }
    @Override
    public void removeSubTask(int subTaskId){
        var subTask = taskManagerBase.getSubTask(subTaskId);
        subTask.ifPresent(this::removeFromHistory);
        taskManagerBase.removeSubTask(subTaskId);
    }

    @Override
    public List<Task> getPrioritizedAll(int route) {
        return taskManagerBase.getPrioritizedAll(route);
    }

    @Override
    public List<Task> getPrioritizedTasks(int route) {
        return taskManagerBase.getPrioritizedTasks(route);
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
    public boolean getHasCrossAll(Task task) {
        return taskManagerBase.getHasCrossAll(task);
    }

    @Override
    public boolean containsIndex(int id) {
        return taskManagerBase.containsIndex(id);
    }

    @Override
    public void refreshSortedMap() {
        taskManagerBase.refreshSortedMap();
    }

    @Override
    public Optional<Exception> updateTask(Task task){
        return taskManagerBase.updateTask(task);
    }
    @Override
    public Optional<Exception> updateEpic(Epic epic) {
        return taskManagerBase.updateEpic(epic);
    }
    @Override
    public Optional<Exception> updateSubTask(SubTask subTask){
        return taskManagerBase.updateSubTask(subTask);
    }

    @Override
    public Optional<Exception> createTask(Task task){
        return taskManagerBase.createTask(task);
    }
    @Override
    public Optional<Exception> createEpic(Epic epic){
        return taskManagerBase.createEpic(epic);
    }
    @Override
    public Optional<Exception> createSubTask(SubTask subTask) {
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
