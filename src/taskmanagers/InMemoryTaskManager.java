package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
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
    public ArrayList<SubTask> getSubTasks(int epicId){
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubTasks();
        }

        return new ArrayList<>();
    }

    @Override
    public Task getTask(int taskId){

        return tasks.get(taskId);
    }

    @Override
    public Epic getEpic(int epicId) {

        return epics.get(epicId);
    }
    @Override
    public SubTask getSubTask(int subTaskId) {

        return subTasks.get(subTaskId);
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
        for(Epic epic : epics.values())
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
        return Set.of();
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
}
