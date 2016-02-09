package com.github.apixandru.utils.monkeypatcher.reimpl;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.ConfigParser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.xml.xpath.XPathConstants.NODESET;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public class SimpleMethodBodyReplacerConfigParser implements ConfigParser<MonkeyConfig> {

    private static final String XPATH_CLASSES = "classes/class";

    @Override
    public MonkeyConfig parse(final Element element) throws XPathExpressionException {
        final XPath xpath = XPathFactory.newInstance().newXPath();
        final NodeList classes = (NodeList) xpath.evaluate(XPATH_CLASSES, element, NODESET);
        return new MonkeyConfig(parseClasses(classes, xpath));
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
        final String name = item.getAttributes().getNamedItem("name").getTextContent();

        final List<String> stubs = XmlUtil.stream(item, "class-pool/stub")
                .map(Node::getTextContent)
                .collect(Collectors.toList());
        ;
        return new ClassToPatch(name,
                parseMethods((NodeList) xpath.evaluate("methods/method", item, NODESET), xpath),
                stubs);
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
