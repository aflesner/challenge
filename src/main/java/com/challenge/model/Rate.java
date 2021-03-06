package com.challenge.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
    Each {@code Rate} object represents a block of time that parking is available at a given price
 */
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

    public boolean contains(RateRequest rateRequest) {
        boolean contains = false;

        if (rateRequest.getBegin().getDayOfWeek().equals(this.getDayOfWeek())
                && rateRequest.getEnd().getDayOfWeek().equals(this.getDayOfWeek())) {
            if (this.getBegin().isBefore(rateRequest.getBegin().toLocalTime())
                    && this.getEnd().isAfter(rateRequest.getEnd().toLocalTime())) {
                contains = true;
            }
        }

        return contains;
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
