package com.github.apixandru.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public final class XmlUtil {

    private XmlUtil() {

    }

    /**
     * @param nodeList
     * @return
     */
    public static Stream<Node> streamNodes(NodeList nodeList) {
        final List<Node> nodes = new ArrayList<>();
        for (int i = 0, to = nodeList.getLength(); i < to; i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes.stream();
    }

}
