package httpserver.httphandlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanagers.TaskManager;
import tasks.Task;
import tools.Option;
import tools.Pair;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BaseHandler<T extends Task> implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;


    protected BaseHandler(TaskManager taskManager) {
        this.taskManager = taskManager;

        var gsonBuilder = new GsonBuilder();

        this.gson = initAdapters(gsonBuilder)
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var commands = exchange.getRequestURI().getPath().split("/"); //["", <tasks>, index, ...]
        var method = exchange.getRequestMethod();


        Supplier<Pair<Integer, String>> initResponse = () -> switch (method) {
            case "GET" -> actionGet(commands);
            case "POST" -> actionPost(() -> readBody(exchange), commands); //Чтобы не передавать контекст exchange, сделал каррирование readBody
            case "DELETE" -> actionDelete(commands);
            default -> new Pair<>(500, "Некорректный запрос");
        };

        var response = initResponse.get();
        var code = response.value1();
        var body = response.map2(String::getBytes).value2();

        // устанавливаем код ответа и отправляем его вместе с заголовками по умолчанию
        exchange.sendResponseHeaders(code, body.length);

        // отправляем тело ответа, записывая строку в выходящий поток
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    protected Optional<Integer> parseInt(String value) {
        try {
            var i = Integer.parseInt(value);

            return Optional.of(i);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    protected <V> Pair<Integer, String> getSuccessGettingList(List<V> items) {
        var json = gson.toJson(items);
        return new Pair<>(200, json);
    }

    protected <V> Pair<Integer, String> getSuccessGettingItem(V item) {
        var json = gson.toJson(item);
        return new Pair<>(200, json);
    }

    protected Pair<Integer, String> updateElement(Function<T, Optional<Exception>> function, T task, String valueOfIndex) {
        return parseInt(valueOfIndex)
                .map(i -> function.apply(task)
                        .map(e -> new Pair<>(406, e.getMessage()))
                        .orElse(new Pair<>(201, "")))
                .orElse(new Pair<>(500, "Ожидался числовой индекс задачи"));
    }

    protected Pair<Integer, String> creteElement(Function<T, Optional<Exception>> function, T task) {
        return function.apply(task)
                .map(ex -> new Pair<>(406, ex.getMessage()))
                .orElse(new Pair<>(201, ""));
    }

    protected Pair<Integer, String> deleteElement(String[] commands, Consumer<Integer> consumer) {
        if (commands.length == 3) {
            return parseInt(commands[2])
                    .map(i -> {
                        if (taskManager.containsIndex(i)) {
                            consumer.accept(i);
                            return new Pair<>(201, "");
                        } else
                            return new Pair<>(404, "Не доступа к индексу");
                    })
                    .orElse(new Pair<>(500, "Ожидался числовой индекс задачи"));
        }

        return new Pair<>(500, "Некорректный запрос");
    }

    protected abstract Pair<Integer, String> getData(String[] commands);

    protected abstract Pair<Integer, String> actionPost(Supplier<Option<T, Exception>> readBody, String[] commands); //Чтобы не передавать контекст exchange, сделал каррирование readBody

    protected abstract Pair<Integer, String> actionDelete(String[] commands);

    protected abstract Class<T> getClassTask();

    protected abstract GsonBuilder initAdapters(GsonBuilder gsonBuilder);


    private Option<T, Exception> readBody(HttpExchange exchange) {
        try {
            var inputStream = exchange.getRequestBody();
            var body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            if (body.isEmpty())
                return Option.initValue2(new Exception("Пустое тело запроса"));

            var task = gson.fromJson(body, getClassTask());
            return Option.initValue1(task);
        } catch (Exception ex) {
            return Option.initValue2(ex);
        }
    }

    private Pair<Integer, String> actionGet(String[] commands) {
        try {
            return getData(commands);
        } catch (Exception ex) {
            return new Pair<>(500, ex.getMessage());
        }
    }
}
