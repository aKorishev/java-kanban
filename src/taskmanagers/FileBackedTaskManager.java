package taskmanagers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tools.ManagerLoadException;
import tools.ManagerSaveException;
import tools.json.EpicTypeAdapter;
import tools.json.TaskTypeAdapter;

import java.io.*;
import java.util.*;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File file;
    private final Gson gson;

    public FileBackedTaskManager(File file) {
        this.file = file;

        gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskTypeAdapter())
                .registerTypeAdapter(Epic.class, new EpicTypeAdapter())
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
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
    public Optional<Exception> updateTask(Task task) {
        var result = super.updateTask(task);
        save();

        return result;
    }

    @Override
    public Optional<Exception> updateEpic(Epic epic) {
        var result = super.updateEpic(epic);
        save();

        return result;
    }

    @Override
    public Optional<Exception> updateSubTask(SubTask subTask) {
        var result = super.updateSubTask(subTask);
        save();

        return result;
    }

    @Override
    public Optional<Exception> createTask(Task task) {
        var result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public Optional<Exception> createEpic(Epic epic) {
        var result = super.createEpic(epic);
        save();
        return result;
    }

    @Override
    public Optional<Exception> createSubTask(SubTask subTask) {
        var result = super.createSubTask(subTask);
        save();
        return result;
    }


    public void reload(){
        tasks.clear();
        epics.clear();
        subTasks.clear();

        if (!file.exists())
            return;
        if (file.length() == 0)
            return;

        try (FileReader fileReader = new FileReader(file)) {
            var jsonReader = new JsonReader(fileReader);

            var tasks = new ArrayList<Task>();
            var epics = new ArrayList<Epic>();

            jsonReader.beginObject();

            while(jsonReader.hasNext()){
                switch (jsonReader.nextName()){
                    case "tasks" -> tasks = gson.fromJson(jsonReader, new TypeToken<List<Task>>() {}.getType());
                    case "epics" -> epics = gson.fromJson(jsonReader, new TypeToken<List<Epic>>() {}.getType());
                }
            }

            jsonReader.endObject();

            //Todo здесь совершил ошибку, тем что history держу в отдельном объекте. Сейчас не критично, но нужно объединить taskManager и history в одном объекте и восстанавливать историю из файла
            //var historyList = taskManager.getHistory();

            for(var task : tasks)
                this.tasks.put(task);
            for(var epic : epics)
                this.epics.put(epic);
        } catch (Exception ex) {
            throw new ManagerLoadException(ex.getMessage(), ex);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            var jsonWriter = new JsonWriter(writer);

            jsonWriter.beginObject();

            if (!tasks.isEmpty())
                jsonWriter.name("tasks").jsonValue(gson.toJson(tasks.values()));

            if (!epics.isEmpty())
                jsonWriter.name("epics").jsonValue(gson.toJson(epics.values()));

            jsonWriter.endObject();

            writer.flush();
        } catch (Exception ex) {
            throw new ManagerSaveException(ex.getMessage(), ex);
        }
    }
}
