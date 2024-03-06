package tools;

import java.util.ArrayList;
import java.util.Iterator;

public class ReadOnlyCollection<T> implements Iterable<T> {
    private final Iterable<T> iterable;

    public ReadOnlyCollection(Iterable<T> iterable){
        this.iterable = iterable;
    }

    public ReadOnlyCollection(){
        iterable = new ArrayList<T>();
    }
    @Override
    public Iterator<T> iterator() {
        return iterable.iterator();
    }

    public ArrayList<T> toArray(){
        ArrayList<T> result = new ArrayList<>();

        for(T i : iterable)
            result.add(i);

        return result;
    }
}
