package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.ReflectionUtil;
import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.reimpl.ReimplConfig;
import com.github.apixandru.utils.monkeypatcher.reimpl.SimpleMethodBodyReplacer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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

        final ReimplConfig parse = (ReimplConfig) configs.values().iterator().next();
        instrumentation.addTransformer(new SimpleMethodBodyReplacer(parse));
    }

    /**
     * @param doc
     * @return
     */
    public static Map<String, Object> parseConfigs(final Document doc) {
        return XmlUtil.stream(doc, XPATH_CONFIGS)
                .collect(Collectors.toMap(
                        node -> XmlUtil.getAttribute(node, "id"),
                        Premain::parseConfig));
    }

    private static <T, C extends ConfigParser<T>> T parseConfig(final Node node) {
        final String clasz = XmlUtil.getAttribute(node, "class");
        return ReflectionUtil.<C>newInstance(clasz).parse(node);
    }

}
