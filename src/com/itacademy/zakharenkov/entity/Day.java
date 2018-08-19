package com.itacademy.zakharenkov.entity;

import com.itacademy.zakharenkov.entity.Activity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Day {

    private List<Activity> activities;

}
