package HttpServer.HttpHandlers;

import com.google.gson.GsonBuilder;
import taskmanagers.TaskManager;
import tasks.Task;
import tools.Option;
import tools.Pair;
import tools.json.TaskTypeAdapter;

import java.util.function.Supplier;


public class TaskHandler extends BaseHandler<Task> {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected Pair<Integer, String> getData(String[] commands) {
        //:/tasks
        //:/tasks/id
        //:/tasks/prioritized
        //:/tasks/prioritized/route
        if (commands.length == 2) return getSuccessGettingList(taskManager.getTasks());

        if (commands[2].equalsIgnoreCase("prioritized") && commands.length < 5) {
            if (commands.length == 3) return getSuccessGettingList(taskManager.getPrioritizedTasks(1));

            return getSuccessGettingList(taskManager.getPrioritizedTasks(parseInt(commands[3]).orElse(1)));
        }

        if (commands.length == 3) {
            return parseInt(commands[2])
                    .map(index ->
                        taskManager.getTask(index)
                                .map(this::getSuccessGettingItem)
                                .orElse(new Pair<>(404, "Не доступа к индексу"))
                    )
                    .orElse(new Pair<>(500, "Ожидался числовой индекс задачи"));
        }

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
        return readBody.get()
                .union(task -> switch (commands.length) {
                    case 3 -> updateElement(taskManager::updateTask, task, commands[2]);
                    case 2 -> creteElement(taskManager::createTask, task);
                    default -> new Pair<>(500, "Некорректный запрос");
                }, ex -> new Pair<>(500, ex.getMessage()));
    }

    @Override
    protected Pair<Integer, String> actionDelete(String[] commands) {
        return deleteElement(commands, taskManager::removeTask);
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
