package TaskManager;

import java.util.*;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final  HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private static int lastNumber = 0;

    public static int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
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
            return epics.get(epicId).subTasks();

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

        if(epics.containsKey(taskId))
            return epics.get(taskId);

        if(subTasks.containsKey(taskId))
            return subTasks.get(taskId);

        return null;//Здесь не зватает option
    }

    public void clearAllTasks(){
        //надеюсь мусорщик удалит подзадачи и эпики, которые связаны друг с другом
        epics.clear();
        tasks.clear();
        subTasks.clear();
    }
    public void clearAllSubTasks(Epic epic){
        clearAllSubTasks(epic.getTaskId());
    }
    public void clearAllSubTasks(int epicId){
        if (!epics.containsKey(epicId)) return;

        Epic epic = epics.get(epicId);

        for(SubTask subTask: epic.subTasks()) {
            subTasks.remove(subTask.getTaskId());
            subTask.clearEpic();
        }

        epic.clearSubTasks();
    }

    public void removeTask(int taskId){
        if (tasks.containsKey(taskId))
            tasks.remove(taskId);

        if(epics.containsKey(taskId)) {
            Epic epic = epics.get(taskId);

            for(SubTask subTask : epic.subTasks())
                subTasks.remove(subTask.getTaskId());

            epics.remove(taskId);
        }

        if(subTasks.containsKey(taskId)) {
            SubTask subTask = subTasks.get(taskId);
            Epic epic = subTask.getEpic();
            if (epic != null) epic.removeSubTask(taskId);
            subTask.clearEpic();

            subTasks.remove(taskId);
        }
    }

    public void update(Task task){
        int taskId = task.getTaskId();
        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    public void update(Epic epic){
        int epicTaskId = epic.getTaskId();

        if (!epics.containsKey(epicTaskId)) return;

        Epic oldEpic = epics.get(epicTaskId);

        for(SubTask subTask: oldEpic.subTasks())
            subTasks.remove(subTask.getTaskId());
        oldEpic.clearSubTasks();

        for (SubTask subTask: epic.subTasks())
            subTasks.put(subTask.getTaskId(), subTask);
        epics.replace(epicTaskId, epic);
    }
    public void update(SubTask subTask){
        int subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId)) return;

        SubTask oldSubTask = subTasks.get(subTaskId);

        Epic oldEpic = oldSubTask.getEpic();
        Epic newEpic = subTask.getEpic();

        if (oldEpic != null && !oldEpic.equals(newEpic))
            oldEpic.removeSubTask(subTaskId);

        subTasks.replace(subTaskId, subTask);
    }

    public boolean putTask(Task task) {
        return putTask(task, tasks);
    }
    public boolean putEpic(Epic epic) {
        return putTask(epic, epics);
    }

    public boolean putSubTask(int epicId, SubTask subTask){
        if (!epics.containsKey(epicId)) return false;

        return putSubTask(epics.get(epicId), subTask);
    }
    private <T extends Task> boolean putTask (T task, HashMap<Integer, T> tasks){
        int id = task.getTaskId();

        if (this.tasks.containsKey(id)) return false;
        if (this.epics.containsKey(id)) return false;
        if (this.subTasks.containsKey(id)) return false;

        tasks.put(id, task);

        return true;
    }

    private boolean putSubTask(Epic epic, SubTask subTask){
        boolean isHavingEpic = subTask.isHavingEpic();
        int subTaskId = subTask.getTaskId();

        if (this.tasks.containsKey(subTaskId)) return false;
        if (this.epics.containsKey(subTaskId)) return false;

        if (this.subTasks.containsKey(subTaskId) && isHavingEpic && subTask.getEpic().equals(epic))
            return false;

        if (isHavingEpic){
            Epic exEpic = subTask.getEpic();
            exEpic.removeSubTask(subTaskId);
        }

        epic.putSubTask(subTask);
        subTask.setEpic(epic);

        if (epic.getTaskStatus() == TaskStatus.DONE && subTask.getTaskStatus() != TaskStatus.DONE)
            epic.doInProgress();

        subTasks.put(subTaskId, subTask);

        return true;
    }
}
