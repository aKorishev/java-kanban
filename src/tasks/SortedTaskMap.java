package tasks;

import enums.TaskType;

import java.time.LocalDateTime;
import java.util.*;

public class SortedTaskMap<T extends Task> extends HashMap<Integer, T> {
    private final Comparator<Optional<LocalDateTime>> comparatorStartTime = (d1, d2) -> d1
            .map(d -> d2.map(d::compareTo).orElse(-1))
            .orElse(d2.map(d -> 1).orElse(0));

    private final Set<T> sortedStartTimeSet = new TreeSet<>((t1, t2) ->
            comparatorStartTime.compare(t1.getStartTime(), t2.getStartTime()));
    private final Set<T> sortedDeskStartTimeSet = new TreeSet<>((t1, t2) ->
            -1 * comparatorStartTime.compare(t1.getStartTime(), t2.getStartTime()));

    public List<T> getSortedStartTimeSet(int route) {
        if (route == 1)
            return List.copyOf(sortedStartTimeSet);

        return List.copyOf(sortedDeskStartTimeSet);
    }

    public void refreshSortedSets(T element) {
        int id = element.getTaskId();
        sortedStartTimeSet.removeIf(i -> i.getTaskId() == id);
        sortedDeskStartTimeSet.removeIf(i -> i.getTaskId() == id);

        element.getStartTime()
                .ifPresent(i -> {
                    sortedStartTimeSet.add(element);
                    sortedDeskStartTimeSet.add(element);
                });
    }

    public void refreshSortedSets() {
        sortedStartTimeSet.clear();
        sortedDeskStartTimeSet.clear();

        for (var element : super.values()) {
            if (element.getStartTime().isPresent()) {
                sortedStartTimeSet.add(element);
                sortedDeskStartTimeSet.add(element);
            }
        }
    }

    public boolean getHasCross() {
        if (sortedStartTimeSet.size() < 2)
            return false;

        Optional<LocalDateTime> prevEndTimeOpt = Optional.empty();

        for (var item :sortedStartTimeSet) {
            if (item.getStartTime().isPresent()) {
                if (prevEndTimeOpt.isEmpty()) {
                    prevEndTimeOpt = item.getEndTime();
                    continue;
                }

                var prevEndTime = prevEndTimeOpt.get();
                var startTime = item.getStartTime().get();

                if (startTime.isBefore(prevEndTime))
                    return true;

                prevEndTimeOpt = item.getEndTime();
            }
        }

        return false;
    }

    public boolean getHasCross(Task element) {
        if (element.getTaskType() == TaskType.EPIC)
            return false;
        if (element.getStartTime().isEmpty() || element.getEndTime().isEmpty())
            return false;

        LocalDateTime elementStartTime = element.getStartTime().get();
        LocalDateTime elementEndTime = element.getEndTime().get(); //Подразумеваем, если есть startTime, endTime так же должен быть
        var elementId = element.getTaskId();

        for (var item :sortedStartTimeSet) {
            if (elementId == item.getTaskId())
                continue;

            if (item.getStartTime().isEmpty() || item.getEndTime().isEmpty())
                continue;

            LocalDateTime itemStartTime = item.getStartTime().get();

            if (itemStartTime.isAfter(elementStartTime) && itemStartTime.isBefore(elementEndTime))
                return true;

            LocalDateTime itemEndTime = item.getEndTime().get(); //Подразумеваем, если есть startTime, endTime так же должен быть

            if (itemEndTime.isAfter(elementStartTime) && itemEndTime.isBefore(elementEndTime))
                return true;
            }

        return false;
    }

    public List<T> getList() {
        return List.copyOf(super.values());
    }

    public Optional<Exception> put(T element) {
        if (element == null)
            return Optional.empty();

        var key = element.getTaskId();

        if (containsKey(key))
            return Optional.of(new Exception("Индекс уже есть в коллекции"));

        if (getHasCross(element))
            return Optional.of(new Exception("Новый элемент пересекается с другими"));

        super.put(key, element);

        element.getStartTime()
                        .ifPresent(time -> {
                            sortedStartTimeSet.add(element);
                            sortedDeskStartTimeSet.add(element);
                        });

        return Optional.empty();
    }

    public void remove(int key) {
        if (containsKey(key))
            remove(super.get(key));
    }

    public void remove(T element) {
        super.remove(element.getTaskId());

        element.getStartTime().ifPresent(i -> {
            var id = element.getTaskId();
            sortedStartTimeSet.removeIf(t -> t.getTaskId() == id);
            sortedDeskStartTimeSet.removeIf(t -> t.getTaskId() == id);
        });
    }

    @Override
    public T put(Integer key, T elenent)  {
        put(elenent);

        return null;
    }

    @Override
    public void clear() {
        super.clear();

        sortedStartTimeSet.clear();
        sortedDeskStartTimeSet.clear();
    }
}
