package com.itacademy.zakharenkov;

import com.itacademy.zakharenkov.entity.Schedule;
import com.itacademy.zakharenkov.util.ReportUtil;
import com.itacademy.zakharenkov.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final String INPUT_FILE = "Schedule.log";
    private static final String TIME_INTERVAL_REPORT_FILE_NAME = "TimeIntervalReport.txt";
    private static final String WORKLOAD_DAY_REPORT_FILE_NAME = "WorkloadDayReport.txt";

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Path pathInput = Paths.get(INPUT_FILE);
        File file = pathInput.toFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            list = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Schedule schedule = Util.buildSchedule(list);

        Path pathTimeInterval = Paths.get(TIME_INTERVAL_REPORT_FILE_NAME);
        ReportUtil.generateTimeIntervalReport(schedule, pathTimeInterval);

        Path pathWorkloadDay = Paths.get(WORKLOAD_DAY_REPORT_FILE_NAME);
        ReportUtil.generateWorkloadDayReport(schedule, pathWorkloadDay);
    }
}