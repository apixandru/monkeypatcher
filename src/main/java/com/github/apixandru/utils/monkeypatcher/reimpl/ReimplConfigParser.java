package com.github.apixandru.utils.monkeypatcher.reimpl;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.ConfigParser;
import org.w3c.dom.Node;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
class ReimplConfigParser implements ConfigParser<ReimplConfig> {

    private static ClassToPatch parseClass(final Node item) {
        final String name = XmlUtil.getAttribute(item, "name");

        final Map<String, MethodToPatch> methods = XmlUtil.stream(item, "methods/method")
                .map(node -> new MethodToPatch(
                        XmlUtil.getAttribute(node, "longname"),
                        XmlUtil.evaluate(node, "insert-before"),
                        XmlUtil.evaluate(node, "body"),
                        XmlUtil.evaluate(node, "insert-after")))
                .collect(Collectors.toMap(
                        MethodToPatch::getLongName,
                        Function.identity()));

        return new ClassToPatch(name, methods);
    }

    @Override
    public ReimplConfig parse(final Node node) {

        final Map<String, ClassToPatch> classMap = XmlUtil.stream(node, "classes/class")
                .map(ReimplConfigParser::parseClass)
                .collect(Collectors.toMap(
                        clasz -> clasz.name.replace('.', '/'),
                        Function.identity()));
        ;
        return new ReimplConfig(classMap);
    }

}
