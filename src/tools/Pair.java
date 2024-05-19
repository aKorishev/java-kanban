package tools;

import java.util.function.Function;

public record Pair<T, V>(T value1, V value2) {

    public <T2> Pair<T2, V> map1(Function<T, T2> map) {
        return new Pair<>(map.apply(value1), value2);
    }

    public <V2> Pair<T, V2> map2(Function<V, V2> map) {
        return new Pair<>(value1, map.apply(value2));
    }

    public <T2, V2> Pair<T2, V2> map(Function<T, T2> map1, Function<V, V2> map2) {
        return new Pair<>(
                map1.apply(value1),
                map2.apply(value2)
        );
    }
}
