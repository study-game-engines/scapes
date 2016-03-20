package org.tobi29.scapes.engine.utils;

import java8.util.function.Function;
import java8.util.function.Supplier;

public final class ThreadLocalUtil {
    private ThreadLocalUtil() {
    }

    public static <T> ThreadLocal<T> of(Supplier<? extends T> supplier) {
        return new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                return supplier.get();
            }
        };
    }

    public static <T> ThreadLocal<T> ofThread(
            Function<Thread, ? extends T> supplier) {
        return new ThreadLocal<T>() {
            @Override
            protected T initialValue() {
                return supplier.apply(Thread.currentThread());
            }
        };
    }
}
