package com.explosion204.wclookup.exception;

public class EntityNotFoundException extends RuntimeException {
    private final Class<?> causeEntity;

    public EntityNotFoundException(Class<?> causeEntity) {
        this.causeEntity = causeEntity;
    }

    public Class<?> getCauseEntity() {
        return causeEntity;
    }
}
