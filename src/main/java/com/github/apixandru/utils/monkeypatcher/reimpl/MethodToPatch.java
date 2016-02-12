package com.github.apixandru.utils.monkeypatcher.reimpl;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 09, 2016
 */
class MethodToPatch {

    final String longName;
    final String body;
    final String before;
    final String after;

    MethodToPatch(final String longName, final String before, final String body, final String after) {
        this.longName = longName;
        this.body = body;
        this.before = before;
        this.after = after;
    }

    String getLongName() {
        return longName;
    }

}
