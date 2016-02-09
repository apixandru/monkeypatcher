package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.reimpl.MonkeyConfig;
import com.github.apixandru.utils.monkeypatcher.reimpl.SimpleMethodBodyReplacer;
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
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.xml.xpath.XPathConstants.NODESET;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class Premain {

    private static final String XPATH_CONFIGS = "/agent-config/transformer-configs/transformer-config";

    public static void premain(final String agentArgument, final Instrumentation instrumentation) {
        if (null == agentArgument) {
            System.err.println("Missing javaagent argument!");
            System.exit(1);
        }
        final Map<String, Object> configs = parseConfigs(agentArgument);
        final MonkeyConfig parse = (MonkeyConfig) configs.values().iterator().next();
        instrumentation.addTransformer(new SimpleMethodBodyReplacer(parse));
    }


    /**
     * @param filename
     * @return
     */
    public static Map<String, Object> parseConfigs(String filename) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document doc = builder.parse(new File(filename));

            final XPath xpath = XPathFactory.newInstance().newXPath();

            final NodeList configParserNodes = (NodeList) xpath.evaluate(XPATH_CONFIGS, doc, NODESET);
            return parseConfigs(configParserNodes);
        } catch (final ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new IllegalStateException("Bad agent configuration", e);
        }
    }

    private static Map<String, Object> parseConfigs(final NodeList configParserNodes) {
        return XmlUtil.streamNodes(configParserNodes)
                .map(Element.class::cast)
                .collect(Collectors.toMap(
                        e -> e.getAttribute("id"),
                        Premain::parseConfig));
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
