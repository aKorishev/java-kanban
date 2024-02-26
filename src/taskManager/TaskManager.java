package taskManager;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final  HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int lastNumber = 0;
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }

    public Collection<SubTask> getSubTasks(int epicId){
        if (epics.containsKey(epicId))
            return epics.get(epicId).getSubTasks();

        return new ArrayList<>();
    }

    public Collection<Task> getAllTasks(){
        ArrayList<Task> collection = new ArrayList<>();

        collection.addAll(tasks.values());
        collection.addAll(epics.values());
        collection.addAll(subTasks.values());

        return collection;
    }

    public Collection<Task> getAllTasks(TaskType taskType){
        ArrayList<Task> collection = new ArrayList<>();

        switch (taskType){
            case TASK: collection.addAll(tasks.values()); break;
            case EPIC: collection.addAll(epics.values()); break;
            case SUBTASK: collection.addAll(subTasks.values()); break;
        }

        return collection;
    }

    public Task getTask(int taskId){
        if (tasks.containsKey(taskId))
            return tasks.get(taskId);

        return null;
    }

    public Epic getEpic(int epicId) {
        if(epics.containsKey(epicId))
            return epics.get(epicId);

        return null;
    }
    public SubTask getSubTask(int subTaskId) {
        if(subTasks.containsKey(subTaskId))
            return subTasks.get(subTaskId);

        return null;
    }

    public void clearAllTasks(){
        //надеюсь мусорщик удалит подзадачи и эпики, которые связаны друг с другом
        epics.clear();
        tasks.clear();
        subTasks.clear();
    }

    public void clearAllSubTasks(int epicId){
        if (!epics.containsKey(epicId)) return;

        Epic epic = epics.get(epicId);

        for(Task subTask: epic.getSubTasks()) {
            subTasks.remove(subTask.getTaskId());
        }

        epic.clearSubTasks();
    }

    public void removeTask(int taskId){
        tasks.remove(taskId);
    }
    public void removeEpic(int epicId){
        clearAllSubTasks(epicId);

        epics.remove(epicId);
    }
    public void removeSubTask(int subTaskId){
        if (subTasks.containsKey(subTaskId)){
            SubTask subTask = subTasks.get(subTaskId);
            subTask.getEpic().removeSubTask(subTaskId);

            subTasks.remove(subTaskId);
        }
    }

    public void updateTask(Task task){
        int taskId = task.getTaskId();
        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    public void updateEpic(Epic epic) throws Exception {
        if (!epic.getSubTasks().isEmpty())
            throw new Exception("Ожидалось обновление name и descriptions, но не обновление subTasks");

        int epicTaskId = epic.getTaskId();

        if (!epics.containsKey(epicTaskId)) return;

        Epic oldEpic = epics.get(epicTaskId);

        ArrayList<SubTask> oldSubTasks = new ArrayList<>(oldEpic.getSubTasks());

        for(SubTask oldSubTask : oldSubTasks){
            int subTaskId = oldSubTask.getTaskId();
            SubTask newSubTask = new SubTask(oldSubTask.getName(), oldSubTask.getDescription(), subTaskId, epic);

            oldEpic.removeSubTask(subTaskId);
            epic.putSubTask(subTaskId, newSubTask);

            subTasks.replace(subTaskId, newSubTask);
        }

        epics.replace(epicTaskId, epic);
    }
    public void updateSubTask(SubTask subTask){
        int subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId)) return;

        SubTask oldSubTask = subTasks.get(subTaskId);

        Epic oldEpic = oldSubTask.getEpic();
        Epic newEpic = subTask.getEpic();

        //Если эпики различаются, значит кейс, когда в новом эпике мы уже есть, а в старом нужно удалить
        if (oldEpic != null && !oldEpic.equals(newEpic))
            oldEpic.removeSubTask(subTaskId);

        subTasks.replace(subTaskId, subTask);
    }

    public int createTask(String name, String description){
        int taskId = incrementLastNumber();

        tasks.put(taskId, new Task(name, description, taskId));

        return taskId;
    }
    public int createEpic(String name, String description){
        int epicId = incrementLastNumber();

        epics.put(epicId, new Epic(name, description, epicId));

        return epicId;
    }
    public int createSubTask(String name, String description, Epic epic) throws Exception {
        int epicId = epic.getTaskId();

        if (!epics.containsKey(epicId))
            throw new Exception("Данный Epic не загружен в систему");

        int subTaskId = incrementLastNumber();
        SubTask subTask = new SubTask(name, description, subTaskId, epic);

        epic.putSubTask(subTaskId, subTask);
        subTasks.put(subTaskId, subTask);

        return subTaskId;
    }
    public int createSubTask(String name, String description, int epicId) throws Exception {
        if (!epics.containsKey(epicId))
            throw new Exception("Данный Epic не загружен в систему");

        Epic epic = epics.get(epicId);

        int subTaskId = incrementLastNumber();
        SubTask subTask = new SubTask(name, description, subTaskId, epic);

        epic.putSubTask(subTaskId, subTask);
        subTasks.put(subTaskId, subTask);

        return subTaskId;
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
}
