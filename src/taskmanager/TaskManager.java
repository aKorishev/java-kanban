package taskmanager;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

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
            return new ArrayList<>(epics.get(epicId).getSubTasks());

        return new ArrayList<>();
    }

    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> arrayList = new ArrayList<>();

        arrayList.addAll(tasks.values());
        arrayList.addAll(epics.values());
        arrayList.addAll(subTasks.values());

        return arrayList;
    }

    public ArrayList<Integer> getEpicIds() {
        return new ArrayList<>(epics.keySet());
    }

    public ArrayList<Integer> getTaskIds() {
        return new ArrayList<>(tasks.keySet());
    }

    public ArrayList<Integer> getSubTaskIds() {
        return new ArrayList<>(subTasks.keySet());
    }

    public ArrayList<Integer> getSubTaskIds(int epicId){
        if (epics.containsKey(epicId))
            return new ArrayList<>(epics.get(epicId).getSubTaskIds());

        return new ArrayList<>();
    }

    public ArrayList<Integer> getAllTaskIds(){
        ArrayList<Integer> arrayList = new ArrayList<>();

        arrayList.addAll(tasks.keySet());
        arrayList.addAll(epics.keySet());
        arrayList.addAll(subTasks.keySet());

        return arrayList;
    }



    public ArrayList<Task> getAllTasks(TaskType taskType){
        ArrayList<Task> arrayList = new ArrayList<>();

        switch (taskType){
            case TASK: arrayList.addAll(tasks.values()); break;
            case EPIC: arrayList.addAll(epics.values()); break;
            case SUBTASK: arrayList.addAll(subTasks.values()); break;
        }

        return arrayList;
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
        for(int i : epics.keySet())
            epics.get(i).clearSubTasks();

        epics.clear();

        subTasks.clear();
    }
    public void removeTask(int taskId){
        tasks.remove(taskId);
    }
    public void removeEpic(int epicId){
        if(!epics.containsKey(epicId)) return;

        for(int i : epics.get(epicId).getSubTaskIds())
            subTasks.remove(i);
        epics.remove(epicId);
    }
    public void removeSubTask(int subTaskId){
        for(int i : epics.keySet())
            epics.get(i).removeSubTask(subTaskId);

        subTasks.remove(subTaskId);
    }

    public void updateTask(Task task, int taskId){
        if (tasks.containsKey(taskId))
            tasks.replace(taskId, task);
    }
    public void updateEpic(Epic epic, int epicId) throws Exception {
        if (!epics.containsKey(epicId)) return;

        Epic oldEpic = epics.get(epicId);

        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
    }
    public void updateSubTask(SubTask subTask, int subTaskId){
        if (!subTasks.containsKey(subTaskId)) return;

        SubTask oldSubTask = subTasks.get(subTaskId);

        oldSubTask.setName(subTask.getName());
        oldSubTask.setDescription(subTask.getDescription());
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
