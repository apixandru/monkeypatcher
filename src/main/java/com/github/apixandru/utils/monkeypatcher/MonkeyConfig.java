package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.reimpl.ClassToPatch;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.xml.xpath.XPathConstants.NODESET;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class MonkeyConfig {

    private static final String XPATH_CONFIGS = "/agent-config/transformer-configs/transformer-config";

    public final Map<String, ClassToPatch> classes;

    public MonkeyConfig(final Map<String, ClassToPatch> classes) {
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

            final NodeList configParserNodes = (NodeList) xpath.evaluate(XPATH_CONFIGS, doc, NODESET);
            final Map<String, Object> configs = parseConfigs(configParserNodes);
            return (MonkeyConfig) configs.values().iterator().next();
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException("Bad agent configuration", e);
        }
    }

    private static Map<String, Object> parseConfigs(final NodeList configParserNodes) {
        return XmlUtil.streamNodes(configParserNodes)
                .map(Element.class::cast)
                .collect(Collectors.toMap(
                        e -> e.getAttribute("id"),
                        MonkeyConfig::parseConfig));
    }

    @SuppressWarnings("unchecked")
    private static <T, C extends ConfigParser<T>> T parseConfig(final Element element) {
        try {
            return ((C) Class.forName(element.getAttribute("class")).newInstance()).parse(element);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
