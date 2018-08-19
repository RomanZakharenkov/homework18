package com.itacademy.zakharenkov.entity;

import lombok.Getter;

@Getter
public enum EventType {
    EXERCISE ("Упражнения"),
    BREAK ("Перерыв"),
    LUNCH ("Обеденный перерыв"),
    SOLUTION ("Решения"),
    END ("Конец"),
    LECTURE ("Лекция");

    private String name;

    EventType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
