package tasks;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;

public class SortedTaskMap <T extends Task> extends TreeMap<Integer, T> {
    private final Comparator<LocalDateTime> comparatorStartTime = (d1, d2) -> {
        if (d1 == null && d2 == null)
            return 0;

        if (d1 != null && d2 == null)
            return -1;

        if (d1 == null)
            return 1;

        return d1.compareTo(d2);
    };

    private final Set<T> sortedStartTimeSet = new TreeSet<>((t1, t2) ->
            comparatorStartTime.compare(t1.getStartTime(), t2.getStartTime()));
    private final Set<T> sortedDeskStartTimeSet = new TreeSet<>((t1, t2) ->
            -1 * comparatorStartTime.compare(t1.getStartTime(), t2.getStartTime()));

    public List<T> getSortedStartTimeSet(int route){
        if (route == 1)
            return List.copyOf(sortedStartTimeSet);

        return List.copyOf(sortedDeskStartTimeSet);
    }
    public void put(T element){
        if (element == null)
            return;

        put(element.getTaskId(), element);
    }

    public void setStartTime (Integer key, LocalDateTime newStartTime){
        if (!super.containsKey(key))
            return;

        T element = super.get(key);
        LocalDateTime oldStartTime = element.getStartTime();

        if (oldStartTime == null && newStartTime == null)
            return;

        if (oldStartTime != null && newStartTime == null){
            sortedStartTimeSet.remove(element);
            sortedDeskStartTimeSet.remove(element);

            element.setStartTime(null);
        } else if (oldStartTime == null) {
            element.setStartTime(newStartTime);

            sortedStartTimeSet.add(element);
            sortedDeskStartTimeSet.add(element);
        } else if (!oldStartTime.isEqual(newStartTime)) {
            sortedStartTimeSet.remove(element);
            sortedDeskStartTimeSet.remove(element);

            element.setStartTime(newStartTime);

            sortedStartTimeSet.add(element);
            sortedDeskStartTimeSet.add(element);
        }
    }

    public boolean getHasCross(){
        LocalDateTime[] prevPeriod = new LocalDateTime[1];

        if (sortedStartTimeSet.size() < 2)
            return false;

        T firstValue = sortedStartTimeSet.stream().toList().getFirst();
        prevPeriod[0] = firstValue.getEndTime();

        return sortedStartTimeSet.stream()
                .skip(1)
                .anyMatch(i -> {
                    if (prevPeriod[0].isAfter(i.getStartTime()))
                        return true;

                    prevPeriod[0] = i.getEndTime();

                    return false;
                });
    }

    public boolean getHasCross(T element){
        LocalDateTime startTime = element.getStartTime();

        if (startTime == null || sortedStartTimeSet.isEmpty())
            return false;

        LocalDateTime endTime = element.getEndTime();

        return sortedStartTimeSet.stream()
                .anyMatch( i -> (i.getStartTime().isBefore(startTime) && i.getEndTime().isAfter(startTime))
                            || (i.getStartTime().isBefore(endTime) && i.getEndTime().isAfter(endTime)));
    }

    public List<T> getList() {
        return List.copyOf(super.values());
    }

    @Override
    public T put(Integer key, T elenent)  {
        if (elenent == null)
            return null;

        T oldValue = super.get(key);

        if (oldValue != null){
            LocalDateTime oldStartTime = oldValue.getStartTime();

            if (oldStartTime != null) {
                sortedStartTimeSet.remove(oldValue);
                sortedDeskStartTimeSet.remove(oldValue);
            }
        }

        if (getHasCross(elenent))
            //Поднадобилось необрабатываемое исключение, т.к. это переопределяемый метод
            throw new Error("Новый элемент пересекается с другими");

        //Из ТЗ, задачи без времени не должны попадать в выдачу сортированных
        if (elenent.getStartTime() != null) {
            sortedStartTimeSet.add(elenent);
            sortedDeskStartTimeSet.add(elenent);
        }


        return super.put(key, elenent);
    }

    @Override
    public void clear() {
        super.clear();

        sortedStartTimeSet.clear();
        sortedDeskStartTimeSet.clear();
    }
}
