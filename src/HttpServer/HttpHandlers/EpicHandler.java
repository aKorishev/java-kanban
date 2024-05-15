package HttpServer.HttpHandlers;

import com.google.gson.GsonBuilder;
import taskmanagers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tools.Option;
import tools.Pair;
import tools.json.EpicTypeAdapter;
import tools.json.SubTaskTypeAdapter;

import java.util.function.Supplier;

public class EpicHandler extends BaseHandler<Epic> {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected Pair<Integer, String> getData(String[] commands) {
        //:/epics
        //:/epics/id
        //:/epics/id/subtasks
        //:/epics/prioritized
        //:/epics/prioritized/route
        Supplier<Pair<Integer, String>> getNotCorrectRequest = () ->
                new Pair<>(500,
                        String.join("\n", "Некорректный запрос",
                                "Ожидаются подобные команды",
                                "/tasks",
                                "/tasks/id",
                                "/tasks/prioritized",
                                "/tasks/prioritized/route"));

        if (commands.length == 2) return getSuccessGettingList(taskManager.getEpics());

        if (commands[2].equalsIgnoreCase("prioritized") && commands.length < 5) {
            if (commands.length == 3) return getSuccessGettingList(taskManager.getPrioritizedEpics(1));

            return getSuccessGettingList(taskManager.getPrioritizedEpics(parseInt(commands[3]).orElse(1)));
        }

        return parseInt(commands[2])
                .map(i -> taskManager.getEpic(i)
                        .map(epic -> {
                            if (commands.length == 4 && commands[3].equalsIgnoreCase("subtasks"))
                                return getSuccessGettingList(epic.getSubTasks());
                            if (commands.length == 3)
                                return getSuccessGettingItem(epic);

                            return getNotCorrectRequest.get();
                        })
                        .orElse(new Pair<>(404, "Не доступа к индексу")))
                .orElse(new Pair<>(500, "Ожидался числовой индекс задачи"));
    }

    @Override
    protected Pair<Integer, String> actionPost(Supplier<Option<Epic, Exception>> readBody, String[] commands) {
        return readBody.get()
                .union(task -> switch (commands.length) {
                    case 3 -> updateElement(taskManager::updateEpic, task, commands[2]);
                    case 2 -> creteElement(taskManager::createEpic, task);
                    default -> new Pair<>(500, "Некорректный запрос");
                }, ex -> new Pair<>(500, ex.getMessage()));
    }

    @Override
    protected Pair<Integer, String> actionDelete(String[] commands) {
        return deleteElement(commands, taskManager::removeEpic);
    }

    @Override
    protected Class<Epic> getClassTask() {
        return Epic.class;
    }
    @Override
    protected GsonBuilder initAdapters(GsonBuilder gsonBuilder) {
        return gsonBuilder
                .registerTypeAdapter(Epic.class, new EpicTypeAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskTypeAdapter());
    }
}
