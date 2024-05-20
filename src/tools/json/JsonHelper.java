package tools.json;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import enums.TaskStatus;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class JsonHelper {
    public static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static <T extends Task> Optional<IOException> taskToJson(JsonWriter jsonWriter, T task, Optional<Supplier<Optional<IOException>>> appendObjects) {
        try {
            jsonWriter.beginObject();

            jsonWriter.name("name").value(task.getName());
            jsonWriter.name("desc").value(task.getDescription());
            jsonWriter.name("id").value(task.getTaskId());

            var status = task.getTaskStatus();
            if (status != TaskStatus.NEW)
                jsonWriter.name("status").value(task.getTaskStatus().name());

            if (task.getDuration().toSeconds() > 0)
                jsonWriter.name("duration").value(task.getDuration().toSeconds());

            if (task.getStartTime().isPresent()) {
                var time = task.getStartTime().get();
                jsonWriter.name("startTime").value(time.format(JsonHelper.DTF));
            }

            if (appendObjects.isPresent()) {
                var result = appendObjects.get().get();

                if (result.isPresent())
                    return result;
            }

            jsonWriter.endObject();
        } catch (IOException ex) {
            return Optional.of(ex);
        }

        return Optional.empty();
    }

    public static <T extends Task> Optional<IOException> taskToJson(JsonWriter jsonWriter, T task) {
        return taskToJson(jsonWriter, task, Optional.empty());
    }

    public static <T extends Task> Optional<IOException> taskFromJson(JsonReader reader, T item, Optional<Function<String, Optional<IOException>>> readOtherFields) {
        try {
            reader.beginObject();

            while (reader.hasNext()) {
                var title = reader.nextName();
                switch (title) {
                    case "name" -> item.setName(reader.nextString());
                    case "desc" -> item.setDescription(reader.nextString());
                    case "status" -> {
                        switch (TaskStatus.valueOf(reader.nextString())) {
                            case TaskStatus.IN_PROGRESS -> item.doProgress();
                            case TaskStatus.DONE -> item.doDone();
                        }
                    }
                    case "id" -> item.setTaskId(reader.nextInt());
                    case "duration" -> item.setDuration(Duration.ofSeconds(reader.nextLong()));
                    case "startTime" -> item.setStartTime(LocalDateTime.parse(reader.nextString(), DTF));
                }

                if (readOtherFields.isPresent()) {
                    var res = readOtherFields.get().apply(title);
                    if (res.isPresent())
                        return res;
                }
            }

            reader.endObject();
        } catch (IOException ex) {
            return Optional.of(ex);
        }

        return Optional.empty();
    }
}
