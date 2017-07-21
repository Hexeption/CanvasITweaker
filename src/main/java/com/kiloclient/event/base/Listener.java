package com.kiloclient.event.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Listener<T extends Event> {
    private final Class<T> type;

    public Listener() {
        final Type type = this.getClass().getGenericSuperclass();
        this.type = (Class<T>) (type instanceof ParameterizedType?((ParameterizedType) type).getActualTypeArguments()[0]:Event.class);
    }

    public Listener(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return this.type;
    }

    public byte getPriority() {
        return 0;
    }

    public abstract void call(T event);
}
