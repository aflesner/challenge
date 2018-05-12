package com.challenge.model;

import java.time.LocalDateTime;

public class RateRequest {
    private LocalDateTime begin;
    private LocalDateTime end;

    public RateRequest() {
    }

    public RateRequest(LocalDateTime begin, LocalDateTime end) {
        this.begin = begin;
        this.end = end;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "RateRequest{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}
