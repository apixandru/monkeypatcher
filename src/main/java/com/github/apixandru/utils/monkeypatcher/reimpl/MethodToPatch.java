package com.github.apixandru.utils.monkeypatcher.reimpl;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
class MethodToPatch {

    final String longName;
    final String body;

    MethodToPatch(final String longName, final String body) {
        this.longName = longName;
        this.body = body;
    }

    String getLongName() {
        return longName;
    }

}
