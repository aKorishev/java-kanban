package taskmanagers;

import enums.TaskStatus;
import enums.TaskType;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tools.ManagerLoadException;
import tools.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public int createTask(Task task) {
        int result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public int createEpic(Epic epic) {
        int result = super.createEpic(epic);
        save();
        return result;
    }

    @Override
    public int createSubTask(SubTask subTask) throws Exception {
        int result = super.createSubTask(subTask);
        save();
        return result;
    }

    void reload(){
        tasks.clear();
        epics.clear();
        subTasks.clear();

        if (!file.exists())
            return;

        Map<Integer, ArrayList<SubTask>> notAddedSubTasks = new HashMap<>();

        try (FileReader fr = new FileReader(file);
             BufferedReader buffer = new BufferedReader(fr)) {

            buffer.readLine(); //пропуск строки с заголовками
            while (buffer.ready()) {
                String line = buffer.readLine();
                String[] values = line.split(";"); //"id;name;description;taskType;taskStatus;epicId"

                int id = Integer.parseInt(values[0]);
                String name = values[1];
                String desc = values[2];
                TaskType type = TaskType.valueOf(values[3]);

                if (type == TaskType.TASK) {
                    TaskStatus status = TaskStatus.valueOf(values[4]);
                    Task task = new Task(name, desc, status);
                    task.setTaskId(id);

                    tasks.put(id, task);

                    continue;
                }

                if (type == TaskType.EPIC) {
                    Epic epic = new Epic(name, desc);
                    epic.setTaskId(id);

                    if (notAddedSubTasks.containsKey(id)) {
                        for (SubTask subTask : notAddedSubTasks.get(id)) {
                            epic.putSubTask(subTask);
                            subTasks.put(subTask.getTaskId(), subTask);
                        }

                        notAddedSubTasks.remove(id);
                    }

                    epics.put(id, epic);

                    continue;
                }

                if (type == TaskType.SUBTASK) {
                    TaskStatus status = TaskStatus.valueOf(values[4]);
                    int epicId = Integer.parseInt(values[5]);

                    SubTask subTask = new SubTask(name, desc, status, epicId);
                    subTask.setTaskId(id);

                    if (epics.containsKey(epicId)) {
                        epics.get(epicId).putSubTask(subTask);
                        subTasks.put(id, subTask);
                    } else if (notAddedSubTasks.containsKey(epicId)) {
                        notAddedSubTasks.get(epicId).add(subTask);
                    } else {
                        notAddedSubTasks.put(epicId, new ArrayList<>(List.of(subTask)));
                    }
                }
            }
        } catch (Exception ex) {
            throw new ManagerLoadException(ex.getMessage(), ex);
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id;name;description;taskType;taskStatus;epicId\n");
            //writer.flush();

            for (Task task : getTasks()) {
                String line = String.format("%d;%s;%s;%s;%s;\n", task.getTaskId(), task.getName(), task.getDescription(), task.getTaskType(), task.getTaskStatus());
                writer.append(line);
            }

            for (Epic epic : getEpics()) {
                String line = String.format("%d;%s;%s;%s;;\n", epic.getTaskId(), epic.getName(), epic.getDescription(), epic.getTaskType());
                writer.append(line);
            }

            for (SubTask subTask : getSubTasks()) {
                String line = String.format("%d;%s;%s;%s;%s;%d\n", subTask.getTaskId(), subTask.getName(), subTask.getDescription(), subTask.getTaskType(), subTask.getTaskStatus(), subTask.getEpicId());
                writer.append(line);
            }
        } catch (IOException ex) {
            throw new ManagerSaveException(ex.getMessage(), ex);
        }
    }
}
