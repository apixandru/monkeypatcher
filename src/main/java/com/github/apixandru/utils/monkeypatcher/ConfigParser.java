package com.github.apixandru.utils.monkeypatcher;

import org.w3c.dom.Node;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public interface ConfigParser<T> {

    /**
     * @param node
     * @return
     */
    T parse(Node node);

}
