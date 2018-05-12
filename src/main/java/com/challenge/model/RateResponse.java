package com.challenge.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class RateResponse {
    private ZonedDateTime begin;
    private ZonedDateTime end;
    private Integer rate;

    public RateResponse(LocalDateTime begin, LocalDateTime end, Integer rate) {
        this.begin = begin.atZone(ZoneOffset.ofHours(0));
        this.end = end.atZone(ZoneOffset.ofHours(0));
        this.rate = rate;
    }

    public ZonedDateTime getBegin() {
        return begin;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public Integer getRate() {
        return rate;
    }
}
