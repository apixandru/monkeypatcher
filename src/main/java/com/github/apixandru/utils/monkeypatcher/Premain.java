package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.ReflectionUtil;
import com.github.apixandru.utils.XmlUtil;
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

    private static final String XPATH_CONFIGS = "/agent-config/transformers/transformer";

    public static void premain(final String agentArgument, final Instrumentation instrumentation)
            throws IOException, SAXException, ParserConfigurationException {

        if (null == agentArgument) {
            System.err.println("Missing javaagent argument!");
            System.exit(1);
        }

        final Document document = XmlUtil.loadDocument(agentArgument);
        final Map<String, AbstractMonkeyPatcher> configs = parseTransformers(document);

        final AbstractMonkeyPatcher parse = configs.values().iterator().next();
        instrumentation.addTransformer(parse);
    }

    /**
     * @param doc
     * @return
     */
    public static Map<String, AbstractMonkeyPatcher> parseTransformers(final Document doc) {
        return XmlUtil.stream(doc, XPATH_CONFIGS)
                .collect(Collectors.toMap(
                        node -> XmlUtil.getAttribute(node, "id"),
                        Premain::loadTransformer));
    }

    @SuppressWarnings("unchecked")
    private static <T, C extends ConfigParser<T>, E extends AbstractMonkeyPatcher<C>> E loadTransformer(final Node node) {
        final Class<E> transformerClass = ReflectionUtil.<E>loadClass(XmlUtil.getAttribute(node, "class"));

        final Class<C> configParserClass = (Class<C>) transformerClass.getAnnotation(ConfiguredBy.class).value();

        final T config = ReflectionUtil.newInstance(configParserClass).parse(node);
        return ReflectionUtil.newInstance(transformerClass, config);
    }

    private static <T, C extends ConfigParser<T>> T loadConfig(final Node node, final Class<C> transformerClass) {
        return ReflectionUtil.newInstance(transformerClass).parse(node);
    }

}
