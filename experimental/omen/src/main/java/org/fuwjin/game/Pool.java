package org.fuwjin.game;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Created by fuwjax on 2/28/15.
 */
public class Pool<T> {
    public static <T> PoolEmpty<T> remaining(){
        return (a, p) -> p.draw(p.remaining, remaining());
    }

    public interface PoolEmpty<T>{
        Pool<T> drawInstead(int amount, Pool<T> pool);
    }

    private int remaining;
    private final T instance;

    public Pool(int count, T instance) {
        assert count >= 0;
        assert instance != null;
        this.remaining = count;
        this.instance = instance;
    }

    public Pool<T> draw(int amount, PoolEmpty<T> onPoolEmpty) {
        assert amount >= 0;
        if(amount > remaining){
            return onPoolEmpty.drawInstead(amount, this);
        }
        remaining -= amount;
        return new Pool<>(amount, instance);
    }
}
