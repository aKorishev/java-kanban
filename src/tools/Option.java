package tools;

import java.util.Optional;
import java.util.function.Function;

public class Option <T, V> {
    private final Optional<T> value1;
    private final Optional<V> value2;

    private Option(Optional<T> value1, Optional<V> value2){
        this.value1 = value1;
        this.value2 = value2;
    }
    public static <T,V> Option<T, V> initValue1 (T value1){
        return new Option<>(Optional.of(value1), Optional.empty());
    }
    public static <T,V> Option<T, V> initValue2 (V value2){
        return new Option<>(Optional.empty(), Optional.of(value2));
    }

    public Optional<T> getValue1(){
        return value1;
    }

    public Optional<V> getValue2(){
        return value2;
    }

    public boolean hasValue1(){
        return value1.isPresent();
    }

    public boolean hasValue2(){
        return value2.isPresent();
    }

    public <T2, V2> Option<T2, V2> map(Function<T, T2> function1, Function<V, V2> function2){
        return new Option<>(value1.map(function1), value2.map(function2));
    }

    public <T2, V2> Option<T2, V2> choose(Function<T, Optional<T2>> function1, Function<V, Optional<V2>> function2){
        return value1
                .<Option<T2, V2>>map(t -> new Option<>(function1.apply(t), Optional.empty()))
                .orElseGet(() -> new Option<>(Optional.empty(), function2.apply(value2.get())));
    }

    public <S> S union(Function<T, S> function1, Function<V, S> function2){
        return value1
                .map(function1)
                .orElseGet(() -> function2.apply(value2.get()));
    }
}
