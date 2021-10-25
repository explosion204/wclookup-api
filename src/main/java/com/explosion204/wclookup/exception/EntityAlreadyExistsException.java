package com.explosion204.wclookup.exception;

public class EntityAlreadyExistsException extends RuntimeException {
    private final Class<?> causeEntity;

    public EntityAlreadyExistsException(Class<?> causeEntity) {
        this.causeEntity = causeEntity;
    }

    public Class<?> getCauseEntity() {
        return causeEntity;
    }
}
