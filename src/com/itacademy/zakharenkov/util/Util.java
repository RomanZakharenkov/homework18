package com.itacademy.zakharenkov.util;

import com.itacademy.zakharenkov.entity.Activity;
import com.itacademy.zakharenkov.entity.Day;
import com.itacademy.zakharenkov.entity.EventType;
import com.itacademy.zakharenkov.entity.Lecture;
import com.itacademy.zakharenkov.entity.Schedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Util {

    private static final String HOUR_GROUP_NAME = "hour";
    private static final String MIN_GROUP_NAME = "min";
    private static final String ACTIVITY_GROUP_NAME = "activity";
    private static final String REGEX = "^(?<" + HOUR_GROUP_NAME + ">\\d{2}):(?<" + MIN_GROUP_NAME + ">\\d{2})\\s(?<" + ACTIVITY_GROUP_NAME + ">[а-яА-ЯёЁ ]+)$";

    public static Schedule buildSchedule(List<String> list) {
        List<Day> days = new ArrayList<>();
        List<Activity> activities = new ArrayList<>();
        for (String line : list) {
            if (!line.isEmpty()) {
                activities.add(buildActivity(line));
            } else {
                Day day = new Day(activities);
                days.add(day);
                activities = new ArrayList<>();
            }
        }
        Day day = new Day(activities);
        days.add(day);
        return new Schedule(days);
    }

    private static Activity buildActivity(String line) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(line);
        Activity activity = new Activity();
        if (matcher.matches()) {
            int hour = Integer.parseInt(matcher.group(HOUR_GROUP_NAME));
            int min = Integer.parseInt(matcher.group(MIN_GROUP_NAME));
            String activityName = matcher.group(ACTIVITY_GROUP_NAME);
            boolean isLecture = true;

            for (EventType type : EventType.values()) {
                if (activityName.equalsIgnoreCase(type.getName())) {
                    activity = new Activity(hour, min, type);
                    isLecture = false;
                    break;
                }
            }
            if (isLecture) {
                activity = new Lecture(hour, min, EventType.LECTURE, activityName);
            }
        }
        return activity;
    }
}
