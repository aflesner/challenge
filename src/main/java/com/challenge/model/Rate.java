package com.challenge.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Rate {
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");

    private DayOfWeek dayOfWeek;
    private LocalTime begin;
    private LocalTime end;
    private int price;

    public Rate(String day, String begin, String end, int price) {
        switch (day) {
            case "mon":
                this.dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "tues":
                this.dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "wed":
                this.dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "thurs":
                this.dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "fri":
                this.dayOfWeek = DayOfWeek.FRIDAY;
                break;
            case "sat":
                this.dayOfWeek = DayOfWeek.SATURDAY;
                break;
            case "sun":
                this.dayOfWeek = DayOfWeek.SUNDAY;
                break;
            default:
                throw new RuntimeException("Invalid day of week");
        }

        this.begin = LocalTime.parse(begin, DATE_TIME_FORMATTER);
        this.end = LocalTime.parse(end, DATE_TIME_FORMATTER);
        this.price = price;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getBegin() {
        return begin;
    }

    public LocalTime getEnd() {
        return end;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "dayOfWeek=" + dayOfWeek +
                ", begin=" + begin +
                ", end=" + end +
                ", price=" + price +
                '}';
    }
}
