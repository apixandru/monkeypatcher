package com.github.apixandru.utils.monkeypatcher;

import org.w3c.dom.Element;

import javax.xml.xpath.XPathExpressionException;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public interface ConfigParser<T> {

    /**
     * @param element the config element
     * @return the config object
     */
    T parse(Element element) throws XPathExpressionException;

}
