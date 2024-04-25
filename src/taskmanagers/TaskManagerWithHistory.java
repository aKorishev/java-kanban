package taskmanagers;

import historyManagers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public ArrayList<SubTask> getSubTasks(int epicId){
        ArrayList<SubTask> subTasks = taskManagerBase.getSubTasks(epicId);
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
