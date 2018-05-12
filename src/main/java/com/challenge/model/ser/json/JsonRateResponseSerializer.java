package com.challenge.model.ser.json;

import com.challenge.model.RateResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class JsonRateResponseSerializer extends StdSerializer<RateResponse> {
    public JsonRateResponseSerializer() {
        this(RateResponse.class);
    }

    protected JsonRateResponseSerializer(Class<RateResponse> t) {
        super(t);
    }

    @Override
    public void serialize(RateResponse value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("begin", value.getBegin().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        gen.writeStringField("end", value.getEnd().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        if (value.getRate() == null) {
            gen.writeStringField("price", "unavailable");
        } else {
            gen.writeNumberField("price", value.getRate());
        }
        gen.writeEndObject();
    }
}