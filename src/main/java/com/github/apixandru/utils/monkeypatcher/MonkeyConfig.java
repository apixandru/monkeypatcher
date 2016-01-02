package com.github.apixandru.utils.monkeypatcher;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.STRING;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
final class MonkeyConfig {

    private static final String LOGGING_PATTERN = "/agent-config/properties/logging.pattern";
    private static final String CLASSES_PATTERN = "/agent-config/classes/class";

    final String loggingPattern;
    final Map<String, ClassToPatch> classes;

    MonkeyConfig(final String loggingPattern, final Map<String, ClassToPatch> classes) {
        this.loggingPattern = loggingPattern;
        this.classes = Collections.unmodifiableMap(classes);
    }

    static class ClassToPatch {
        final String name;

        ClassToPatch(final String name) {
            this.name = name;
        }
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

            final NodeList classes = (NodeList) xpath.evaluate(CLASSES_PATTERN, doc, NODESET);
            return new MonkeyConfig(loggingPattern, parseClasses(classes));
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException("Bad agent configuration", e);
        }
    }

    private static Map<String, ClassToPatch> parseClasses(final NodeList classes) {
        Map<String, ClassToPatch> map = new HashMap<>();
        for (int i = 0; i < classes.getLength(); i++) {
            final ClassToPatch parse = parse(classes.item(i));
            map.put(parse.name.replace('.', '/'), parse);
        }
        return map;
    }

    private static ClassToPatch parse(final Node item) {
        System.out.println(item);
        return new ClassToPatch(item.getAttributes().getNamedItem("name").getTextContent());
    }

}
