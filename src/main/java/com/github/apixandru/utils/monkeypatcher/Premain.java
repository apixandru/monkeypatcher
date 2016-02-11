package com.github.apixandru.utils.monkeypatcher;

import com.github.apixandru.utils.ReflectionUtil;
import com.github.apixandru.utils.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 02, 2016
 */
public final class Premain {

    public static void premain(final String agentArgument, final Instrumentation instrumentation)
            throws IOException, SAXException, ParserConfigurationException {

        if (null == agentArgument) {
            System.err.println("Missing javaagent argument!");
            System.exit(1);
        }

        Log.setFile(agentArgument.replace(".xml", "." + System.currentTimeMillis()));

        final Document document = XmlUtil.loadDocument(agentArgument);

        final AbstractMonkeyPatcher parse = parseTransformer(document);
        instrumentation.addTransformer(parse);
    }

    public static AbstractMonkeyPatcher parseTransformer(final Document doc) {
        return loadTransformer(doc.getDocumentElement());
    }

    @SuppressWarnings("unchecked")
    private static <T, C extends ConfigParser<T>, E extends AbstractMonkeyPatcher<C>> E loadTransformer(final Node node) {
        final Class<E> transformerClass = ReflectionUtil.<E>loadClass(node.getNodeName());

        final Class<C> configParserClass = (Class<C>) transformerClass.getAnnotation(ConfiguredBy.class).value();

        final T config = ReflectionUtil.newInstance(configParserClass).parse(node);
        return ReflectionUtil.newInstance(transformerClass, config);
    }

}
