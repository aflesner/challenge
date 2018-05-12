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
    private ParkingRater() {
        InputStream inputStream = ParkingRater.class.getClassLoader().getResourceAsStream("rates.json");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String ratesJson = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));

        try {
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            //ignore can't do anything about this.
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser jsonParser = null;
        try {
            jsonParser = jsonFactory.createParser(ratesJson);
            JsonNode jsonNode;
            try {
                jsonNode = mapper.readTree(jsonParser);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read rates JSON", e);
            }

            JsonNode ratesNode = jsonNode.get("rates");

            for (JsonNode rateNode: ratesNode) {
                String[] times = rateNode.get("times").asText().split("-");
                for(String day: rateNode.get("days").asText().split(",")) {
                    rates.add(new Rate(day, times[0], times[1], rateNode.get("price").asInt()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create parser for rates JSON", e);
        } finally {
            if (jsonParser != null) {
                try {
                    jsonParser.close();
                } catch (IOException e) {
                    //ignore can't do anything about this.
                }
            }
        }
    }

    // avoiding very small chance of race condition with thread safe singleton pattern
    private static class LazyRater {
        public static final ParkingRater INSTANCE = new ParkingRater();
    }

    public static ParkingRater getInstance() {
        return LazyRater.INSTANCE;
    }

    private List<Rate> rates = new ArrayList<>();

    public Rate getRate(RateRequest rateRequest) {
        return getInstance().rates.stream().filter(r -> r.contains(rateRequest)).findFirst().orElse(null);
    }
}
