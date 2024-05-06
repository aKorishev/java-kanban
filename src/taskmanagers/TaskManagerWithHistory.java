package taskmanagers;

import historyManagers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
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
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epics = taskManagerBase.getEpics();
        addInHistory(epics);
        return epics;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = taskManagerBase.getTasks();
        addInHistory(tasks);
        return tasks;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        ArrayList<SubTask> subTasks = taskManagerBase.getSubTasks();
        addInHistory(subTasks);
        return subTasks;
    }

    @Override
    public Optional<ArrayList<SubTask>> getSubTasks(int epicId){
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
    public Optional<Integer> createTask(Task task){
        return taskManagerBase.createTask(task);
    }
    @Override
    public Optional<Integer> createEpic(Epic epic){
        return taskManagerBase.createEpic(epic);
    }
    @Override
    public Optional<Integer> createSubTask(SubTask subTask) throws Exception {
        return taskManagerBase.createSubTask(subTask);
    }

    @Override
    public Collection<Task> getHistory(){

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
