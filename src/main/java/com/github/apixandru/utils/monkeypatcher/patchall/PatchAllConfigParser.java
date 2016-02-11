package com.github.apixandru.utils.monkeypatcher.patchall;

import com.github.apixandru.utils.XmlUtil;
import com.github.apixandru.utils.monkeypatcher.ConfigParser;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 11, 2016
 */
class PatchAllConfigParser implements ConfigParser<PatchAllConfig> {

    @Override
    public PatchAllConfig parse(final Node node) {

        final List<String> includes = XmlUtil.stream(node, "includes/include")
                .map(Node::getTextContent)
                .map(s -> s.replace(".", "/"))
                .collect(Collectors.toList());

        final List<String> excludes = XmlUtil.stream(node, "excludes/exclude")
                .map(Node::getTextContent)
                .map(s -> s.replace(".", "/"))
                .collect(Collectors.toList());

        return new PatchAllConfig(includes, excludes);
    }

}
