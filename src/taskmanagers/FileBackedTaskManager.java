package taskmanagers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;

public class FileBackedTaskManager implements TaskManager {
    @Override
    public Collection<Task> getHistory() {
        return null;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return null;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return null;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return null;
    }

    @Override
    public ArrayList<SubTask> getSubTasks(int epicId) {
        return null;
    }

    @Override
    public Task getTask(int taskId) {
        return null;
    }

    @Override
    public Epic getEpic(int epicId) {
        return null;
    }

    @Override
    public SubTask getSubTask(int subTaskId) {
        return null;
    }

    @Override
    public void clearAllTasks() {

    }

    @Override
    public void clearAllEpics() {

    }

    @Override
    public void clearAllSubTasks() {

    }

    @Override
    public void removeTask(int taskId) {

    }

    @Override
    public void removeEpic(int epicId) {

    }

    @Override
    public void removeSubTask(int subTaskId) {

    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void updateEpic(Epic epic) {

    }

    @Override
    public void updateSubTask(SubTask subTask) {

    }

    @Override
    public int createTask(Task task) {
        return 0;
    }

    @Override
    public int createEpic(Epic epic) {
        return 0;
    }

    @Override
    public int createSubTask(SubTask subTask) throws Exception {
        return 0;
    }
}
