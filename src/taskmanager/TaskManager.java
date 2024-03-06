package taskmanager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import enums.TaskType;
import tools.ReadOnlyCollection;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private final HashMap<String, Task> tasks = new HashMap<>();
    private final  HashMap<String, Epic> epics = new HashMap<>();
    private final HashMap<String, SubTask> subTasks = new HashMap<>();

    private int lastNumber = 0;
    public ReadOnlyCollection<Epic> getEpics() {
        return new ReadOnlyCollection<>(epics.values());
    }

    public ReadOnlyCollection<Task> getTasks() {
        return new ReadOnlyCollection<>(tasks.values());
    }

    public ReadOnlyCollection<SubTask> getSubTasks() {
        return new ReadOnlyCollection<>(subTasks.values());
    }

    public ReadOnlyCollection<SubTask> getSubTasks(String epicId){
        if (epics.containsKey(epicId))
            return epics.get(epicId).getSubTasks();

        return new ReadOnlyCollection<>();
    }

    public Task getTask(String taskId){
        return tasks.get(taskId);
    }

    public Epic getEpic(String epicId) {
        return epics.get(epicId);
    }
    public SubTask getSubTask(String subTaskId) {
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
        for(String i : epics.keySet())
            epics.get(i).clearSubTasks();

        subTasks.clear();
    }
    public void removeTask(String taskId){
        tasks.remove(taskId);
    }
    public void removeEpic(String epicId){
        if(!epics.containsKey(epicId)) return;

        for(SubTask i : epics.get(epicId).getSubTasks())
            subTasks.remove(i.getTaskId());

        epics.remove(epicId);
    }
    public void removeSubTask(String subTaskId){
        if(!subTasks.containsKey(subTaskId)) return;

        SubTask subTask = subTasks.get(subTaskId);

        String epicId = subTask.getEpicId();

        if (epicId != null && !epicId.isEmpty())
            if (epics.containsKey(epicId))
                epics.get(epicId).removeSubTask(subTaskId);

        subTasks.remove(subTaskId);
    }

    public void updateTask(Task task){
        String taskId = task.getTaskId();

        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    public void updateEpic(Epic epic) {
        String epicId = epic.getTaskId();

        if (!epics.containsKey(epicId)) return;

        Epic oldEpic = epics.get(epicId);

        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }
    public void updateSubTask(SubTask subTask){
        String subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId)) return;

        SubTask oldSubTask = subTasks.get(subTaskId);

        String oldEpicId = oldSubTask.getEpicId();
        String newEpicId = subTask.getEpicId();

        if (oldEpicId != null && !oldEpicId.equals(newEpicId) && epics.containsKey(oldEpicId)) {
            Epic epic = epics.get(oldEpicId);
            epic.removeSubTask(subTaskId);
        } else if (newEpicId != null && !newEpicId.equals(oldEpicId) && epics.containsKey(newEpicId)) {
            Epic epic = epics.get(newEpicId);
            epic.putSubTask(subTask);
        }

        subTasks.replace(subTaskId, subTask);
    }

    public String createTask(Task task){
        String taskId = "" + incrementLastNumber();

        tasks.put(taskId, task);

        return taskId;
    }
    public String createEpic(Epic epic){
        String taskId = "" + incrementLastNumber();

        epics.put(taskId, epic);

        return taskId;
    }
    public String createSubTask(SubTask subTask){
        String taskId = "" + incrementLastNumber();

        subTasks.put(taskId, subTask);

        return taskId;
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
}
