package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int lastNumber = 0;

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    @Override
    public Optional<ArrayList<SubTask>> getSubTasks(int epicId){
        if (epics.containsKey(epicId)) {
            return Optional.of(epics.get(epicId).getSubTasks());
        }

        return Optional.empty();
    }

    @Override
    public Optional<Task> getTask(int taskId){

        return Optional.ofNullable(tasks.get(taskId));
    }

    @Override
    public Optional<Epic> getEpic(int epicId) {

        return Optional.ofNullable(epics.get(epicId));
    }
    @Override
    public Optional<SubTask> getSubTask(int subTaskId) {

        return Optional.ofNullable(subTasks.get(subTaskId));
    }

    @Override
    public void clearAllTasks(){
        tasks.clear();
    }
    @Override
    public void clearAllEpics(){
        epics.clear();
        subTasks.clear();
    }
    @Override
    public void clearAllSubTasks(){
        for(var epic : epics.values())
            epic.clearSubTasks();

        subTasks.clear();
    }
    @Override
    public void removeTask(int taskId){
        tasks.remove(taskId);
    }
    @Override
    public void removeEpic(int epicId){
        if(!epics.containsKey(epicId)) return;

        Epic epic = epics.get(epicId);

        for(SubTask i : epic.getSubTasks()) {
            subTasks.remove(i.getTaskId());
        }

        epics.remove(epicId);
    }
    @Override
    public void removeSubTask(int subTaskId){
        if(!subTasks.containsKey(subTaskId)) return;

        SubTask subTask = subTasks.get(subTaskId);

        int epicId = subTask.getEpicId();

        if (epics.containsKey(epicId))
            epics.get(epicId).removeSubTask(subTaskId);

        subTasks.remove(subTaskId);
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
        var subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId)) return;

        var oldSubTask = subTasks.get(subTaskId);

        var oldEpicId = oldSubTask.getEpicId();
        var newEpicId = subTask.getEpicId();

        if (oldEpicId != newEpicId && epics.containsKey(newEpicId))
            epics.get(newEpicId).removeSubTask(subTaskId);

        subTasks.replace(subTaskId, subTask);
    }

    @Override
    public Optional<Integer> createTask(Task task){
        var nextNumber = incrementNextNumber();

        if (tasks.containsKey(nextNumber))
            return Optional.empty();

        task.setTaskId(nextNumber);

        tasks.put(nextNumber, task);

        return Optional.of(nextNumber);
    }
    @Override
    public Optional<Integer> createEpic(Epic epic){
        var nextNumber = incrementNextNumber();

        if (epics.containsKey(nextNumber))
            return Optional.empty();

        epic.setTaskId(nextNumber);

        epics.put(nextNumber, epic);

        for(var subTask: epic.getSubTasks()){
            var subTaskId = incrementNextNumber();
            subTask.setTaskId(subTaskId);
            subTask.setEpicId(nextNumber);

            subTasks.put(subTaskId, subTask);
        }

        return Optional.of(nextNumber);
    }
    @Override
    public Optional<Integer> createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId))
            return Optional.empty();

        var nextNumber = incrementNextNumber();

        subTask.setTaskId(nextNumber);

        subTasks.put(nextNumber, subTask);

        epics.get(epicId).putSubTask(subTask);

        return Optional.of(nextNumber);
    }

    @Override
    public Collection<Task> getHistory(){
        return Set.of();
    }

    private int incrementNextNumber() {
        lastNumber++;
        return lastNumber;
    }
}
