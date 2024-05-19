package tools.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Task;

import java.io.IOException;
import java.util.Optional;


public class TaskTypeAdapter extends TypeAdapter<Task> {
    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        var task = new Task("", "");
        var res = JsonHelper.taskFromJson(jsonReader, task, Optional.empty());

        if (res.isPresent())
            throw  res.get();

        return task;
    }

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        var result = JsonHelper.taskToJson(jsonWriter, task, Optional.empty());

        if (result.isPresent())
            throw result.get();
    }
}



