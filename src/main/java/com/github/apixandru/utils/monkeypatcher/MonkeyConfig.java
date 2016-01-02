package com.github.apixandru.utils.monkeypatcher;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

import static javax.xml.xpath.XPathConstants.STRING;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
final class MonkeyConfig {

    private static final String LOGGING_PATTERN = "/agent-config/properties/logging.pattern";

    final String loggingPattern;

    /**
     * @param loggingPattern
     */
    MonkeyConfig(final String loggingPattern) {
        this.loggingPattern = loggingPattern;
    }


    /**
     * @param filename
     * @return
     */
    static MonkeyConfig parse(String filename) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(new File(filename));

            final XPath xpath = XPathFactory.newInstance().newXPath();
            final String loggingPattern = (String) xpath.evaluate(LOGGING_PATTERN, doc, STRING);
            return new MonkeyConfig(loggingPattern);
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException("Bad agent configuration", e);
        }
    }

}
