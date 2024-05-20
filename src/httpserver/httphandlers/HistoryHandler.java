package httpserver.httphandlers;

import com.google.gson.GsonBuilder;
import taskmanagers.TaskManager;
import tasks.Task;
import tools.Option;
import tools.Pair;
import tools.json.TaskTypeAdapter;

import java.util.function.Supplier;


public class HistoryHandler extends BaseHandler<Task> {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);

    }

    @Override
    protected Pair<Integer, String> getData(String[] commands) {
        //:/history
        if (commands.length == 2) return getSuccessGettingList(taskManager.getHistory());

        return new Pair<>(500,
                String.join("\n", "Некорректный запрос",
                        "Ожидаются подобные команды",
                        "/tasks",
                        "/tasks/id",
                        "/tasks/prioritized",
                        "/tasks/prioritized/route"));
    }

    @Override
    protected Pair<Integer, String> actionPost(Supplier<Option<Task, Exception>> readBody, String[] commands) {
        return new Pair<>(500, "Некорректный запрос");
    }

    @Override
    protected Pair<Integer, String> actionDelete(String[] commands) {
        return new Pair<>(500, "Некорректный запрос");
    }

    @Override
    protected Class<Task> getClassTask() {
        return Task.class;
    }

    @Override
    protected GsonBuilder initAdapters(GsonBuilder gsonBuilder) {
        return gsonBuilder
                .registerTypeAdapter(Task.class, new TaskTypeAdapter());
    }
}
