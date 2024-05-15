package taskmanagers;

import tasks.*;
import tools.Option;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Optional<List<SubTask>> getSubTasks(int epicId){
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
    public Optional<Exception> updateTask(Task task){
        int taskId = task.getTaskId();

        if (tasks.containsKey(taskId)) {
            if (tasks.getHasCross(task))
                return Optional.of(new Exception("Элемент пересекается с другими"));

            tasks.replace(taskId, task);
            tasks.refreshSortedSets(task);

            return Optional.empty();
        } else
            return Optional.of(new Exception("Индекс задачи не найден"));
    }
    @Override
    public Optional<Exception> updateEpic(Epic epic) {
        int epicId = epic.getTaskId();

        if (!epics.containsKey(epicId))
            return Optional.of(new Exception("Индекс задачи не найден"));

        Epic oldEpic = epics.get(epicId);

        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());

        return Optional.empty();
    }
    @Override
    public Optional<Exception> updateSubTask(SubTask subTask) {
        var subTaskId = subTask.getTaskId();

        if (!subTasks.containsKey(subTaskId))
            return Optional.of(new Exception("Индекс задачи не найден"));

        var oldSubTask = subTasks.get(subTaskId);

        var oldEpicId = oldSubTask.getEpicId();
        var newEpicId = subTask.getEpicId();

        if (!epics.containsKey(newEpicId))
            return Optional.of(new Exception("Новый эпик не доступен"));

        if (oldEpicId != newEpicId){
            var oldEpic = epics.get(oldEpicId);
            oldEpic.removeSubTask(subTaskId);
            epics.refreshSortedSets(oldEpic);

            var newEpic = epics.get(newEpicId);
            newEpic.putSubTask(subTask);
            epics.refreshSortedSets(newEpic);
        }

        if (subTasks.getHasCross(subTask))
            return Optional.of(new Exception("Элемент пересекается с другими"));

        subTasks.remove(oldSubTask);
        try {
            subTasks.put(subTask);
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Exception> createTask(Task task){
        var nextNumber = incrementNextNumber();

        if (containsIndex(nextNumber))
            return Optional.of(new Exception("Индекс пересекается с другими задачами"));

        task.setTaskId(nextNumber);

        return tasks.put(task);
    }
    @Override
    public Optional<Exception> createEpic(Epic epic){
        var nextNumber = incrementNextNumber();

        if (containsIndex(nextNumber))
            return Optional.of(new Exception("Индекс пересекается с другими задачами"));

        epic.setTaskId(nextNumber);

        var resOfPutting = epics.put(epic);

        if (resOfPutting.isPresent())
            return resOfPutting;

        for(var subTask: epic.getSubTasks()){
            var subTaskId = incrementNextNumber();
            subTask.setTaskId(subTaskId);
            subTask.setEpicId(nextNumber);

            resOfPutting = subTasks.put(subTask);

            if (resOfPutting.isPresent())
                return resOfPutting;
        }

        return Optional.empty();
    }
    @Override
    public Optional<Exception> createSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();

        if (!epics.containsKey(epicId))
            return Optional.of(new Exception("epicId не найден"));

        var nextNumber = incrementNextNumber();

        if (containsIndex(nextNumber))
            return Optional.of(new Exception("Индекс пересекается с другими задачами"));

        subTask.setTaskId(nextNumber);

        var resOfPutting = subTasks.put(subTask);

        if (resOfPutting.isPresent())
            return resOfPutting;

        epics.get(epicId).putSubTask(subTask);

        return Optional.empty();
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
            task.setStartTime(Optional.of(startTime));
    }
    @Override
    public void setEpicStartTime(int epicId, LocalDateTime startTime){
        Epic epic = epics.get(epicId);
        if (epic != null)
            epic.setStartTime(Optional.of(startTime));
    }
    @Override
    public void setSubTaskStartTime(int subTaskId, LocalDateTime startTime){
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            subTask.setStartTime(Optional.of(startTime));
            epics.get(subTask.getEpicId()).calcTaskDuration();
        }
    }

    @Override
    public List<Task> getPrioritizedAll(int route) {
        List<Task> list = tasks.getSortedStartTimeSet(route);
        //list.addAll(epics.getSortedStartTimeSet(route)); Епики пропустим, т.к. вся работа на подхадачах
        list.addAll(subTasks.getSortedStartTimeSet(route));

        return list;
    }


    @Override
    public List<Task> getPrioritizedTasks(int route) {
        return tasks.getSortedStartTimeSet(route);
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
    public boolean getHasCrossAll(Task task) {
        return tasks.getHasCross(task) || subTasks.getHasCross(task);
    }

    @Override
    public boolean containsIndex(int id) {
        return tasks.containsKey(id) || epics.containsKey(id) || subTasks.containsKey(id);
    }

    @Override
    public void refreshSortedMap() {
        tasks.refreshSortedSets();
    }

    private int incrementNextNumber() {
        lastNumber++;
        return lastNumber;
    }
}
