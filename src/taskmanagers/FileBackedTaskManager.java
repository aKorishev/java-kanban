package taskmanagers;

import enums.TaskStatus;
import enums.TaskType;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tools.ManagerLoadException;
import tools.ManagerSaveException;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {

        this.file = file;
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubTasks() {
        super.clearAllSubTasks();
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubTask(int subTaskId) {
        super.removeSubTask(subTaskId);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public Optional<Integer> createTask(Task task) {
        var result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public Optional<Integer> createEpic(Epic epic) {
        var result = super.createEpic(epic);
        save();
        return result;
    }

    @Override
    public Optional<Integer> createSubTask(SubTask subTask) {
        var result = super.createSubTask(subTask);
        save();
        return result;
    }
    void reload(){
        tasks.clear();
        epics.clear();
        subTasks.clear();

        if (!file.exists())
            return;

        Map<Integer, List<SubTask>> notAddedSubTasks = new HashMap<>();

        try (FileReader fr = new FileReader(file);
             BufferedReader buffer = new BufferedReader(fr)) {

            buffer.readLine(); //пропуск строки с заголовками
            while (buffer.ready()) {
                String line = buffer.readLine();

                String[] values = line.split(";");
                //0 id;1 name;2 description;3 taskType;4 duration;5 startTime;6 taskStatus;7 epicId

                int id = Integer.parseInt(values[0]);
                String name = values[1];
                String desc = values[2];
                TaskType type = TaskType.valueOf(values[3]);

                Duration duration = Duration.parse(values[4]);
                Supplier<LocalDateTime> getStartTime = () -> {
                    String value = values[5];
                    if (value == null || value.equals("null"))
                        return null;

                    return LocalDateTime.parse(value);
                };

                Supplier<Task> getTask = () -> {
                    TaskStatus status = TaskStatus.valueOf(values[6]);

                    Task task = new Task(name, desc, status);

                    task.setTaskId(id);
                    task.setDuration(duration);
                    task.setStartTime(getStartTime.get());

                    return task;
                };

                Supplier<Epic> getEpic = () -> {
                    Epic epic = new Epic(name, desc);

                    epic.setTaskId(id);
                    epic.setDuration(duration);
                    epic.setStartTime(getStartTime.get());

                    return epic;
                };

                Supplier<SubTask> getSubTask = () -> {
                    TaskStatus status = TaskStatus.valueOf(values[6]);
                    int epicId = Integer.parseInt(values[7]);

                    SubTask subTask = new SubTask(name, desc, status, epicId);

                    subTask.setTaskId(id);
                    subTask.setDuration(duration);
                    subTask.setStartTime(getStartTime.get());

                    return subTask;
                };

                if (type == TaskType.TASK) {
                    tasks.put(id, getTask.get());
                } else if (type == TaskType.EPIC) {
                    Epic epic = getEpic.get();

                    if (notAddedSubTasks.containsKey(id)) {
                        for (SubTask subTask : notAddedSubTasks.get(id)) {
                            epic.putSubTask(subTask);
                            subTasks.put(subTask.getTaskId(), subTask);
                        }

                        notAddedSubTasks.remove(id);
                    }

                    epics.put(id, epic);
                } else if (type == TaskType.SUBTASK) {
                    SubTask subTask = getSubTask.get();
                    int epicId = subTask.getEpicId();

                    if (epics.containsKey(epicId)) {
                        epics.get(epicId).putSubTask(subTask);
                        subTasks.put(id, subTask);
                    } else if (notAddedSubTasks.containsKey(epicId)) {
                        notAddedSubTasks.get(epicId).add(subTask);
                    } else {
                        notAddedSubTasks.put(epicId, List.of(subTask));
                    }
                } else {
                    throw new Exception("Неизвестный тип задачи");
                }
            }
        } catch (Exception ex) {
            throw new ManagerLoadException(ex.getMessage(), ex);
        }
    }
    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id;name;description;taskType;duration;startTime;taskStatus;epicId\n"); //0 id;1 name;2 description;3 taskType;4 duration;5 startTime;6 taskStatus;7 epicId
            //writer.flush();

            for (Task task : getTasks()) {
                String line = String.format("%d;%s;%s;%s;%s;%s;%s;\n", task.getTaskId(), task.getName(), task.getDescription(), task.getTaskType(), task.getDuration(), Objects.requireNonNullElse(task.getStartTime(), "null"), task.getTaskStatus());
                writer.append(line);
            }

            for (Epic epic : getEpics()) {
                epic.calcTaskDuration();

                String line = String.format("%d;%s;%s;%s;%s;%s;;\n", epic.getTaskId(), epic.getName(), epic.getDescription(), epic.getTaskType(), epic.getDuration(), Objects.requireNonNullElse(epic.getStartTime(), "null"));
                writer.append(line);
            }

            for (SubTask subTask : getSubTasks()) {
                String line = String.format("%d;%s;%s;%s;%s;%s;%s;%d\n", subTask.getTaskId(), subTask.getName(), subTask.getDescription(), subTask.getTaskType(), subTask.getDuration(), Objects.requireNonNullElse(subTask.getStartTime(), "null"), subTask.getTaskStatus(), subTask.getEpicId());
                writer.append(line);
            }
        } catch (IOException ex) {
            throw new ManagerSaveException(ex.getMessage(), ex);
        }
    }
}
