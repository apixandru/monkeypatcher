package com.github.apixandru.utils.monkeypatcher.reimpl;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
public class MethodToPatch {

    public final String longName;
    public final String body;

    public MethodToPatch(final String longName, final String body) {
        this.longName = longName;
        this.body = body;
    }

}
