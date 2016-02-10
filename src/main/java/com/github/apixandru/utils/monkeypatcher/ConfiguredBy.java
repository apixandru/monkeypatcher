package com.github.apixandru.utils.monkeypatcher;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 10, 2016
 */
public @interface ConfiguredBy {

    Class<? extends ConfigParser<?>> value();

}
