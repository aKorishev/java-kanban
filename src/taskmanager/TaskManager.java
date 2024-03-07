package taskmanager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import enums.TaskType;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final  HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int lastNumber = 0;
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<SubTask> getSubTasks(int epicId){
        if (epics.containsKey(epicId))
            return epics.get(epicId).getSubTasks();

        return new ArrayList<>();
    }

    public Task getTask(int taskId){
        return tasks.get(taskId);
    }

    public Epic getEpic(int epicId) {
        return epics.get(epicId);
    }
    public SubTask getSubTask(int subTaskId) {
        return subTasks.get(subTaskId);
    }

    public void clearAllTasks(){
        tasks.clear();
    }
    public void clearAllEpics(){
        epics.clear();
        subTasks.clear();
    }
    public void clearAllSubTasks(){
        for(Epic epic : epics.values())
            epic.clearSubTasks();

        subTasks.clear();
    }
    public void removeTask(int taskId){
        tasks.remove(taskId);
    }
    public void removeEpic(int epicId){
        if(!epics.containsKey(epicId)) return;

        for(SubTask i : epics.get(epicId).getSubTasks())
            subTasks.remove(i.getTaskId());

        epics.remove(epicId);
    }
    public void removeSubTask(int subTaskId){
        if(!subTasks.containsKey(subTaskId)) return;

        SubTask subTask = subTasks.get(subTaskId);

        int epicId = subTask.getEpicId();

        if (epics.containsKey(epicId))
            epics.get(epicId).removeSubTask(subTaskId);

        subTasks.remove(subTaskId);
    }

    public void updateTask(Task task){
        int taskId = task.getTaskId();

        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    public void updateEpic(Epic epic) {
        int epicId = epic.getTaskId();

        if (!epics.containsKey(epicId)) return;

        Epic oldEpic = epics.get(epicId);

        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }
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

    public int createTask(Task task){
        int taskId = incrementLastNumber();

        tasks.put(taskId, task);

        return taskId;
    }
    public int createEpic(Epic epic){
        int taskId = incrementLastNumber();

        epics.put(taskId, epic);

        return taskId;
    }
    public int createSubTask(SubTask subTask){
        int taskId = incrementLastNumber();

        subTasks.put(taskId, subTask);

        return taskId;
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
}
