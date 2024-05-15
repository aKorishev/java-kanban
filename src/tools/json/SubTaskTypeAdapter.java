package tools.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.SubTask;
import tools.json.JsonHelper;

import java.io.IOException;
import java.util.Optional;

public class SubTaskTypeAdapter extends TypeAdapter<SubTask> {
    @Override
    public SubTask read(JsonReader jsonReader) throws IOException {
        var subTask = new SubTask("", "", 0);
        var resItem = JsonHelper.taskFromJson(
                jsonReader,
                subTask,
                Optional.of(title -> {
                    try {
                        if (title.equals("epicId")) {
                            subTask.setEpicId(jsonReader.nextInt());
                        }
                    } catch (IOException ex) {
                        return Optional.of(ex);
                    }

                    return Optional.empty();
                }));

        if (resItem.isPresent())
            throw resItem.get();

        return subTask;
    }

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
        var res = JsonHelper.taskToJson(jsonWriter, subTask, Optional.of(() -> {
            try {
                jsonWriter.name("epicId").value(subTask.getEpicId());
            } catch (IOException e) {
                return Optional.of(e);
            }
            return Optional.empty();
        }));

        if (res.isPresent())
            throw res.get();
    }
}
