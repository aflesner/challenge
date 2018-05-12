package com.challenge.handler;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import com.challenge.model.Rate;
import com.challenge.model.RateRequest;
import com.challenge.model.RateResponse;
import com.challenge.model.de.json.JsonRateRequestDeserializer;
import com.challenge.model.de.xml.XmlRateRequestDeserializer;
import com.challenge.model.ser.json.JsonRateResponseSerializer;
import com.challenge.model.ser.xml.XmlRateResponseSerializer;
import com.challenge.service.ParkingRater;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Handler extends AbstractHandler {
    private static String JSON_500 = "{\"error\":\"Internal Server Error\"}";
    private static String XML_500 = "<?xml version='1.0' encoding='UTF-8'?><error>Internal Server Error</error>";

    private static ObjectMapper JSON_MAPPER;
    private static ObjectReader JSON_READER;
    private static ObjectWriter JSON_WRITER;

    private static ParkingRater PARKING_RATER;

    public Handler() {
        JSON_MAPPER = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(RateRequest.class, new JsonRateRequestDeserializer());
        module.addSerializer(RateResponse.class, new JsonRateResponseSerializer());
        JSON_MAPPER.registerModule(module);
        JSON_READER = JSON_MAPPER.readerFor(RateRequest.class);
        JSON_WRITER = JSON_MAPPER.writerFor(RateResponse.class);

        PARKING_RATER = new ParkingRater();
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        switch (request.getHeader("Content-Type")) {
            case "application/xml":
                try {
                    this.handleXml(request, response);
                } catch (IOException | XMLStreamException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/xml");
                    try {
                        response.getWriter().write(XML_500);
                        response.getWriter().flush();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } finally {
                    try {
                        response.getWriter().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "application/json":
                try {
                    this.handleJson(request, response);
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    try {
                        response.getWriter().write(JSON_500);
                        response.getWriter().flush();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                } finally {
                    try {
                        response.getWriter().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain; charset=utf-8");
                try {
                    response.getWriter().write("Request content type not supported must be either application/xml or application/json");
                    response.getWriter().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        response.getWriter().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        baseRequest.setHandled(true);
    }

    private void handleJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        RateRequest rateRequest = JSON_READER.readValue(
                request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

        Rate rate = PARKING_RATER.getRate(rateRequest);
        response.getWriter().write(JSON_WRITER.writeValueAsString(
                new RateResponse(rateRequest.getBegin(), rateRequest.getEnd(), rate == null ? null : rate.getPrice())));
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void handleXml(HttpServletRequest request, HttpServletResponse response) throws IOException, XMLStreamException {
        response.setContentType("application/xml");
        response.setStatus(HttpServletResponse.SC_OK);

        RateRequest rateRequest = XmlRateRequestDeserializer
                .deserialize(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));

        Rate rate = PARKING_RATER.getRate(rateRequest);
        String xml = XmlRateResponseSerializer.serialize(
                new RateResponse(rateRequest.getBegin(), rateRequest.getEnd(), rate == null ? null : rate.getPrice()));
        response.getWriter().write(xml);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
