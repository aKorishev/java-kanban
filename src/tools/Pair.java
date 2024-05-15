package tools;

import java.util.function.Function;

public class Pair <T, V> {
    private final T value1;
    private final V value2;

    public Pair(T value1, V value2){
        this.value1 = value1;
        this.value2 = value2;
    }

    public T getValue1(){
        return value1;
    }

    public V getValue2(){
        return value2;
    }

    public <T2> Pair<T2, V> map1(Function<T, T2> map){
        return new Pair<>(map.apply(value1), value2);
    }

    public <V2> Pair<T, V2> map2(Function<V, V2> map){
        return new Pair<>(value1, map.apply(value2));
    }

    public <T2, V2> Pair<T2, V2> map(Function<T, T2> map1, Function<V, V2> map2){
        return new Pair<>(
                map1.apply(value1),
                map2.apply(value2)
        );
    }
}
