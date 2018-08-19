package com.itacademy.zakharenkov.util;

import com.itacademy.zakharenkov.entity.Activity;
import com.itacademy.zakharenkov.entity.Day;
import com.itacademy.zakharenkov.entity.EventType;
import com.itacademy.zakharenkov.entity.Lecture;
import com.itacademy.zakharenkov.entity.Schedule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportUtil {

    public static void generateTimeIntervalReport(Schedule schedule, Path path) {
        File file = path.toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Day day : schedule.getDays()) {
                for (int i = 0; i < day.getActivities().size() - 1; i++) {
                    writer.write(buildTimeIntervalReportLine(day.getActivities().get(i), day.getActivities().get(i + 1)));
                }
                writer.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildTimeIntervalReportLine(Activity current, Activity next) {
        StringBuilder line = new StringBuilder();
        line.append(formatTime(current.getHour()))
                .append(":")
                .append(formatTime(current.getMinute()))
                .append("-")
                .append(formatTime(next.getHour()))
                .append(":")
                .append(formatTime(next.getMinute()))
                .append(" ")
                .append(getActivityName(current))
                .append(System.lineSeparator());
        return line.toString();
    }

    private static String formatTime(int value) {
        return value > 9 ? String.valueOf(value) : "0" + String.valueOf(value);
    }

    private static String getActivityName(Activity activity) {
        String result;
        if (activity.getEventType().equals(EventType.LECTURE)) {
            result = ((Lecture)activity).getLectureName();
        } else {
            result = activity.getEventType().getName();
        }
        return result;
    }

    public static void generateWorkloadDayReport(Schedule schedule, Path path) {
        File file = path.toFile();
        Map<EventType, Integer> workload = new EnumMap<>(EventType.class);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Day day : schedule.getDays()) {
                Map<Lecture, Integer> lectures = new HashMap<>();
                for (EventType type : EventType.values()) {
                    workload.put(type, 0);
                }
                for (int i = 0; i < day.getActivities().size() - 1; i++) {
                    Activity current = day.getActivities().get(i);
                    Activity next = day.getActivities().get(i + 1);
                    int minutes = calculateMinutes(current, next);
                    workload.computeIfPresent(current.getEventType(), (eventType, value) -> value + minutes);
                    if (current.getEventType().equals(EventType.LECTURE)) {
                        lectures.put((Lecture)current, minutes);
                    }
                }
                int totalMinutes = calculateTotalMinutes(workload.values());
                for (Map.Entry<EventType, Integer> entry : workload.entrySet()) {
                    if (!entry.getKey().equals(EventType.END)) {
                        writer.write(buildWorkloadDayReportLine(entry, totalMinutes));
                    }
                }
                writer.write(System.lineSeparator());
                writer.write("Лекции:");
                writer.write(System.lineSeparator());
                for (Map.Entry<Lecture, Integer> entry : lectures.entrySet()) {
                    writer.write(buildDetailedReportLine(entry, calculateTotalMinutes(lectures.values())));
                }
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildDetailedReportLine(Map.Entry<Lecture, Integer> entry, int totalMinutes) {
        StringBuilder line = new StringBuilder();
        line.append(entry.getKey().getLectureName())
                .append(": ")
                .append(String.valueOf(entry.getValue()))
                .append(" мин. ")
                .append(String.valueOf(entry.getValue() * 100 / totalMinutes))
                .append("%")
                .append(System.lineSeparator());
        return line.toString();
    }

    private static String buildWorkloadDayReportLine(Map.Entry<EventType, Integer> entry, int totalMinutes) {
        StringBuilder line = new StringBuilder();
        line.append(entry.getKey().getName())
                .append(": ")
                .append(String.valueOf(entry.getValue()))
                .append(" мин. ")
                .append(String.valueOf(entry.getValue() * 100 / totalMinutes))
                .append("%")
                .append(System.lineSeparator());
        return line.toString();
    }

    private static int calculateMinutes(Activity current, Activity next) {
        return (next.getHour() * 60 + next.getMinute()) - (current.getHour() * 60 + current.getMinute());
    }

    private static int calculateTotalMinutes(Collection<Integer> values) {
        return values.stream().mapToInt(Integer::intValue).sum();
    }
}
