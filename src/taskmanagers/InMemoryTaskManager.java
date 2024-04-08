package taskmanagers;

import historyManagers.HistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int lastNumber = 0;

    public InMemoryTaskManager(HistoryManager historyManager){
        this.historyManager = historyManager;
    }
    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> eoics = new ArrayList<>(this.epics.values());
        addInHistory(eoics);
        return eoics;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>(this.tasks.values());
        addInHistory(tasks);
        return tasks;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {

        ArrayList<SubTask> subTasks = new ArrayList<>(this.subTasks.values());
        addInHistory(subTasks);
        return subTasks;
    }

    @Override
    public ArrayList<SubTask> getSubTasks(int epicId){
        if (epics.containsKey(epicId)) {
            ArrayList<SubTask> subTasks = epics.get(epicId).getSubTasks();
            addInHistory(subTasks);
            return subTasks;
        }

        return new ArrayList<>();
    }

    @Override
    public Task getTask(int taskId){
        Task task = tasks.get(taskId);

        addInHistory(task);

        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.get(epicId);

        addInHistory(epic);

        return epic;
    }
    @Override
    public SubTask getSubTask(int subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);

        addInHistory(subTask);

        return subTask;
    }

    @Override
    public void clearAllTasks(){
        removeFromHistory(tasks.values());

        tasks.clear();
    }
    @Override
    public void clearAllEpics(){
        removeFromHistory(epics.values());
        removeFromHistory(subTasks.values());

        epics.clear();
        subTasks.clear();
    }
    @Override
    public void clearAllSubTasks(){
        for(Epic epic : epics.values())
            epic.clearSubTasks();

        removeFromHistory(subTasks.values());

        subTasks.clear();
    }
    @Override
    public void removeTask(int taskId){
        removeFromHistory(tasks.get(taskId));
        tasks.remove(taskId);
    }
    @Override
    public void removeEpic(int epicId){
        if(!epics.containsKey(epicId)) return;

        Epic epic = epics.get(epicId);

        for(SubTask i : epic.getSubTasks()) {
            removeFromHistory(i);
            subTasks.remove(i.getTaskId());
        }

        epics.remove(epicId);
        removeFromHistory(epic);
    }
    @Override
    public void removeSubTask(int subTaskId){
        if(!subTasks.containsKey(subTaskId)) return;

        SubTask subTask = subTasks.get(subTaskId);

        int epicId = subTask.getEpicId();

        if (epics.containsKey(epicId))
            epics.get(epicId).removeSubTask(subTaskId);

        subTasks.remove(subTaskId);
        removeFromHistory(subTask);
    }

    @Override
    public void updateTask(Task task){
        int taskId = task.getTaskId();

        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getTaskId();

        if (!epics.containsKey(epicId)) return;

        Epic oldEpic = epics.get(epicId);

        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }
    @Override
    public void updateSubTask(SubTask subTask){
        int subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId)) return;

        SubTask oldSubTask = subTasks.get(subTaskId);

        int oldEpicId = oldSubTask.getEpicId();
        int newEpicId = subTask.getEpicId();

        if (oldEpicId != newEpicId && epics.containsKey(newEpicId))
            epics.get(newEpicId).removeSubTask(subTaskId);

        subTasks.replace(subTaskId, subTask);
    }

    @Override
    public int createTask(Task task){
        int taskId = incrementLastNumber();

        task.setTaskId(taskId);

        tasks.put(taskId, task);

        return taskId;
    }
    @Override
    public int createEpic(Epic epic){
        int taskId = incrementLastNumber();

        epic.setTaskId(taskId);

        epics.put(taskId, epic);

        for(SubTask subTask : epic.getSubTasks()) {
            int subTaskId = subTask.getTaskId();
            if (!subTasks.containsKey(subTaskId))
                subTasks.put(subTaskId, subTask);
        }

        return taskId;
    }
    @Override
    public int createSubTask(SubTask subTask) throws Exception {
        int epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId))
            throw new Exception("Epic not found");

        int subTaskId = incrementLastNumber();

        subTask.setTaskId(subTaskId);

        subTasks.put(subTaskId, subTask);

        Epic epic = epics.get(epicId);
        if (!epic.containsSubTaskId(subTaskId))
            epic.putSubTask(subTask);

        return subTaskId;
    }

    @Override
    public Collection<Task> getHistory(){
        return historyManager.getHistory();
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }

    private <T extends Task> void addInHistory(List<T> tasks) {
        for(Task task : tasks)
            historyManager.add(task);
    }
    private <T extends Task> void addInHistory(T task) { historyManager.add(task);}

    private <T extends Task>  void removeFromHistory(Collection<T> tasks){
        for (Task task : tasks)
            historyManager.remove(task.getTaskId());
    }

    private <T extends Task>  void removeFromHistory(T task) { historyManager.remove(task.getTaskId()); }
}
