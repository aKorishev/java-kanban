package tools.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.util.Optional;


public class EpicTypeAdapter extends TypeAdapter<Epic> {
    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        var epic = new Epic("","");

        var res = JsonHelper.taskFromJson(
                jsonReader,
                epic,
                Optional.of(title -> {
                    if (title.equals("SubTasks")){
                        try {
                            jsonReader.beginArray();

                            while (jsonReader.hasNext()) {
                                var subTask = new SubTask("","", epic.getTaskId());
                                var resItem = JsonHelper.taskFromJson(
                                        jsonReader,
                                        subTask,
                                        Optional.empty());

                                if (resItem.isPresent())
                                    return resItem;

                                epic.putSubTask(subTask);
                            }

                            jsonReader.endArray();
                        } catch (IOException ex) {
                            return Optional.of(ex);
                        }
                    }

                    return Optional.empty();
                }));

        if (res.isPresent())
            throw res.get();

        return epic;
    }

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        var result = JsonHelper.taskToJson(jsonWriter, epic, Optional.of(() -> {
            var subTasks = epic.getSubTasks();

            if (!subTasks.isEmpty()){
                try {
                    jsonWriter.name("SubTasks").beginArray();

                    for (var subTask : subTasks) {
                        var resultIteration = JsonHelper.taskToJson(jsonWriter, subTask, Optional.empty());

                        if (resultIteration.isPresent())
                            return resultIteration;
                    }

                    jsonWriter.endArray();
                } catch (IOException e) {
                    return Optional.of(e);
                }
            }

            return Optional.empty();
        }));

        if (result.isPresent())
            throw result.get();
    }
}


