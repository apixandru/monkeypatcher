package com.github.apixandru.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public final class XmlUtil {

    // not thread safe
    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private XmlUtil() {

    }

    /**
     * @param nodeList
     * @return
     */
    private static Stream<Node> streamNodes(NodeList nodeList) {
        final List<Node> nodes = new ArrayList<>();
        for (int i = 0, to = nodeList.getLength(); i < to; i++) {
            nodes.add(nodeList.item(i));
        }
        return nodes.stream();
    }

    public static Stream<Node> stream(final Node element, final String xPath) {
        try {
            final NodeList nodeList = (NodeList) XPATH.evaluate(xPath, element, XPathConstants.NODESET);
            return streamNodes(nodeList);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("XPath failure", e);
        }
    }

}
