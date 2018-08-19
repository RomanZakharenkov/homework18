package com.itacademy.zakharenkov.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Lecture extends Activity {

    private String lectureName;

    public Lecture(int timeHour, int timeMinute, EventType eventType, String lectureName) {
        super(timeHour, timeMinute, eventType);
        this.lectureName = lectureName;
    }
}
