package com.challenge.service;

import com.challenge.model.Rate;
import com.challenge.model.RateRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingRater {
    private static String RATE_SOURCE;
    private static List<Rate> RATES = new ArrayList<>();

    static {
        InputStream inputStream = ParkingRater.class.getClassLoader().getResourceAsStream("rates.json");
        RATE_SOURCE = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = null;
        try {
            jsonParser = jsonFactory.createParser(RATE_SOURCE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(jsonParser);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode ratesNode = jsonNode.get("rates");

        for (JsonNode rateNode: ratesNode) {
            String[] times = rateNode.get("times").asText().split("-");
            for(String day: rateNode.get("days").asText().split(",")) {
                RATES.add(new Rate(day, times[0], times[1], rateNode.get("price").asInt()));
            }
        }
    }

    public Rate getRate(RateRequest rateRequest) {
        Rate toReturn = null;

        for (Rate rate : RATES) {
            if (rateRequest.getBegin().getDayOfWeek().equals(rate.getDayOfWeek())
                    && rateRequest.getEnd().getDayOfWeek().equals(rate.getDayOfWeek())) {
                if (rate.getBegin().isBefore(rateRequest.getBegin().toLocalTime())
                        && rate.getEnd().isAfter(rateRequest.getEnd().toLocalTime())) {
                    toReturn = rate;
                    break;
                }
            }
        }

        return toReturn;
    }
}
