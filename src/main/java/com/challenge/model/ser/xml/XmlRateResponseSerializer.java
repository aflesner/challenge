package com.challenge.model.ser.xml;

import com.challenge.model.RateResponse;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;

public class XmlRateResponseSerializer {
    private static XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newFactory();

    public static String serialize(RateResponse rateResponse) throws XMLStreamException, IOException {
        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter xmlStreamWriter = XML_OUTPUT_FACTORY.createXMLStreamWriter(stringWriter);

        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement("RateResponse");
        xmlStreamWriter.writeStartElement("begin");
        // write out dates as ISO date string with ZonedDateTime "Z"
        xmlStreamWriter.writeCharacters(rateResponse.getBegin().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("end");
        xmlStreamWriter.writeCharacters(rateResponse.getEnd().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeStartElement("price");
        // if Rate object is null parking spot was unavailable
        xmlStreamWriter.writeCharacters(
                rateResponse.getRate() == null ? "unavailable" : String.valueOf(rateResponse.getRate()));
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        stringWriter.flush();

        String xml = stringWriter.getBuffer().toString();
        stringWriter.close();

        return xml;
    }
}
