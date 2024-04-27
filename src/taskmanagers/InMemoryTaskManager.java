package taskmanagers;

import tasks.Epic;
import tasks.SortedTaskMap;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final SortedTaskMap<Task> tasks = new SortedTaskMap<>();
    protected final SortedTaskMap<Epic> epics = new SortedTaskMap<>();
    protected final SortedTaskMap<SubTask> subTasks = new SortedTaskMap<>();
    private int lastNumber = 0;

    @Override
    public List<Epic> getEpics() {
        return epics.getList();
    }

    @Override
    public List<Task> getTasks() {
        return tasks.getList();
    }

    @Override
    public List<SubTask> getSubTasks() {
        return subTasks.getList();
    }

    @Override
    public List<SubTask> getSubTasks(int epicId){
        if (epics.containsKey(epicId)) {
            return epics.get(epicId).getSubTasks();
        }

        return List.of();
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
    public List<Task> getHistory(){
        return List.of();
    }


    @Override
    public void setTaskDuration(int taskId, Duration duration){
        Task task = tasks.get(taskId);
        if (task != null)
            task.setDuration(duration);
    }
    @Override
    public void setEpicDuration(int epicId, Duration duration){
        Epic epic = epics.get(epicId);
        if (epic != null)
            epic.setDuration(duration);
    }
    @Override
    public void setSubTaskDuration(int subTaskId, Duration duration){
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            subTask.setDuration(duration);
            epics.get(subTask.getEpicId()).calcTaskDuration();
        }
    }
    @Override
    public void setTaskStartTime(int taskId, LocalDateTime startTime){
        Task task = tasks.get(taskId);
        if (task != null)
            task.setStartTime(startTime);
    }
    @Override
    public void setEpicStartTime(int epicId, LocalDateTime startTime){
        Epic epic = epics.get(epicId);
        if (epic != null)
            epic.setStartTime(startTime);
    }
    @Override
    public void setSubTaskStartTime(int subTaskId, LocalDateTime startTime){
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            subTask.setStartTime(startTime);
            epics.get(subTask.getEpicId()).calcTaskDuration();
        }
    }

    @Override
    public List<Epic> getPrioritizedEpics(int route) {
        return epics.getSortedStartTimeSet(route);
    }

    @Override
    public List<SubTask> getPrioritizedSubTasks(int route) {
        return subTasks.getSortedStartTimeSet(route);
    }

    @Override
    public List<Task> getPrioritizedTasks(int route) {
        return tasks.getSortedStartTimeSet(route);
    }

    private int incrementLastNumber() {
        lastNumber++;
        return lastNumber;
    }
}
