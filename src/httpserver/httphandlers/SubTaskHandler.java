package httpserver.httphandlers;

import com.google.gson.GsonBuilder;
import taskmanagers.TaskManager;
import tasks.SubTask;
import tools.Option;
import tools.Pair;
import tools.json.SubTaskTypeAdapter;

import java.util.function.Supplier;

public class SubTaskHandler extends BaseHandler<SubTask> {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected Pair<Integer, String> getData(String[] commands) {
        //:/subtasks
        //:/subtasks/id
        //:/subtasks/prioritized
        //:/subtasks/prioritized/route
        if (commands.length == 2) return getSuccessGettingList(taskManager.getSubTasks());

        if (commands[2].equalsIgnoreCase("prioritized") && commands.length < 5) {
            if (commands.length == 3) return getSuccessGettingList(taskManager.getPrioritizedSubTasks(1));

            return getSuccessGettingList(taskManager.getPrioritizedSubTasks(parseInt(commands[3]).orElse(1)));
        }

        if (commands.length == 3) {
            return parseInt(commands[2])
                    .map(index ->
                            taskManager.getSubTask(index)
                                    .map(this::getSuccessGettingItem)
                                    .orElse(new Pair<>(404, "Не доступа к индексу"))
                    )
                    .orElse(new Pair<>(500, "Ожидался числовой индекс задачи"));
        }

        return new Pair<>(500,
                String.join("\n", "Некорректный запрос",
                        "Ожидаются подобные команды",
                        "/subtasks",
                        "/subtasks/id",
                        "/subtasks/prioritized",
                        "/subtasks/prioritized/route"));
    }

    @Override
    protected Pair<Integer, String> actionPost(Supplier<Option<SubTask, Exception>> readBody, String[] commands) {
        return readBody.get()
                .union(task -> switch (commands.length) {
                    case 3 -> updateElement(taskManager::updateSubTask, task, commands[2]);
                    case 2 -> creteElement(taskManager::createSubTask, task);
                    default -> new Pair<>(500, "Некорректный запрос");
                }, ex -> new Pair<>(500, ex.getMessage()));
    }

    @Override
    protected Pair<Integer, String> actionDelete(String[] commands) {
        return deleteElement(commands, taskManager::removeSubTask);
    }

    @Override
    protected Class<SubTask> getClassTask() {
        return SubTask.class;
    }

    @Override
    protected GsonBuilder initAdapters(GsonBuilder gsonBuilder) {
        return gsonBuilder
                .registerTypeAdapter(SubTask.class, new SubTaskTypeAdapter());
    }
}
