package com.challenge.model.de.json;

import com.challenge.model.RateRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonRateRequestDeserializer extends StdDeserializer<RateRequest> {
    public JsonRateRequestDeserializer() {
        this(RateRequest.class);
    }

    public JsonRateRequestDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RateRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        // parse ZonedDateTime with "Z" zone id to LocalDateTime
        return new RateRequest(LocalDateTime.parse(node.get("begin").asText(), DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse(node.get("end").asText(), DateTimeFormatter.ISO_DATE_TIME));
    }
}
