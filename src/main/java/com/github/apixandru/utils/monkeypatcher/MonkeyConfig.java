package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.monkeypatcher.reimpl.ClassToPatch;
import com.github.apixandru.utils.monkeypatcher.reimpl.MethodToPatch;
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

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
final class MonkeyConfig {

    private static final String LOGGING_PATTERN = "/agent-config/properties/logging.pattern";
    private static final String CLASSES_PATTERN = "/agent-config/transformer/classes/class";

    final String loggingPattern;
    final Map<String, ClassToPatch> classes;

    MonkeyConfig(final String loggingPattern, final Map<String, ClassToPatch> classes) {
        this.loggingPattern = loggingPattern;
        this.classes = Collections.unmodifiableMap(classes);
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
            final String loggingPattern = xpath.evaluate(LOGGING_PATTERN, doc);

            final NodeList classes = (NodeList) xpath.evaluate(CLASSES_PATTERN, doc, NODESET);
            return new MonkeyConfig(loggingPattern, parseClasses(classes, xpath));
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException("Bad agent configuration", e);
        }
    }

    private static Map<String, ClassToPatch> parseClasses(final NodeList classes, final XPath xpath) throws XPathExpressionException {
        Map<String, ClassToPatch> map = new HashMap<>();
        for (int i = 0; i < classes.getLength(); i++) {
            final ClassToPatch parse = parse(classes.item(i), xpath);
            map.put(parse.name.replace('.', '/'), parse);
        }
        return map;
    }

    private static ClassToPatch parse(final Node item, final XPath xpath) throws XPathExpressionException {
        String name = item.getAttributes().getNamedItem("name").getTextContent();
        return new ClassToPatch(name,
                parseMethods((NodeList) xpath.evaluate("methods/method", item, NODESET), xpath),
                parseStubs((NodeList) xpath.evaluate("class-pool/stub", item, NODESET), xpath));
    }

    private static List<String> parseStubs(final NodeList stubs, final XPath xpath) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < stubs.getLength(); i++) {
            list.add(stubs.item(i).getTextContent());
        }
        return list;
    }

    private static Map<String, MethodToPatch> parseMethods(final NodeList methods, final XPath xpath) throws XPathExpressionException {
        Map<String, MethodToPatch> map = new HashMap<>();
        for (int i = 0; i < methods.getLength(); i++) {
            final MethodToPatch parse = parseMethod(methods.item(i), xpath);
            map.put(parse.longName, parse);
        }
        return map;
    }

    private static MethodToPatch parseMethod(final Node item, final XPath xpath) throws XPathExpressionException {
        String name = item.getAttributes().getNamedItem("longname").getTextContent();
        return new MethodToPatch(name, xpath.evaluate("body", item));
    }

}
