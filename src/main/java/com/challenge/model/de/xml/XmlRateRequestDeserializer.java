package com.challenge.model.de.xml;

import com.challenge.model.RateRequest;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XmlRateRequestDeserializer {
    private static XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newFactory();

    public static RateRequest deserialize(String xml) throws XMLStreamException {
        XMLStreamReader xmlStreamReader = XML_INPUT_FACTORY.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        xmlStreamReader.next();
        xmlStreamReader.next();
        String begin = xmlStreamReader.getElementText();

        xmlStreamReader.next();
        String end = xmlStreamReader.getElementText();

        // parse ZonedDateTime with "Z" zone id to LocalDateTime
        RateRequest rateRequest = new RateRequest(LocalDateTime.parse(begin, DateTimeFormatter.ISO_DATE_TIME),
            LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME));

        xmlStreamReader.close();

        return rateRequest;
    }
}
