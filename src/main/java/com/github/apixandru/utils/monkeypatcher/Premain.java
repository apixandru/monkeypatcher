package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.reimpl.MonkeyConfig;
import com.github.apixandru.utils.monkeypatcher.reimpl.SimpleMethodBodyReplacer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class Premain {

    private static final String XPATH_CONFIGS = "/agent-config/transformer-configs/transformer-config";

    public static void premain(final String agentArgument, final Instrumentation instrumentation)
            throws IOException, SAXException, ParserConfigurationException {

        if (null == agentArgument) {
            System.err.println("Missing javaagent argument!");
            System.exit(1);
        }

        final Document document = XmlUtil.loadDocument(agentArgument);
        final Map<String, Object> configs = parseConfigs(document);

        final MonkeyConfig parse = (MonkeyConfig) configs.values().iterator().next();
        instrumentation.addTransformer(new SimpleMethodBodyReplacer(parse));
    }


    /**
     * @param doc
     * @return
     */
    public static Map<String, Object> parseConfigs(final Document doc) {
        return XmlUtil.stream(doc, XPATH_CONFIGS)
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
