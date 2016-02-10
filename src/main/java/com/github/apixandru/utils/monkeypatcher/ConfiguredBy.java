package com.github.apixandru.utils.monkeypatcher;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 10, 2016
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConfiguredBy {

    Class<? extends ConfigParser<?>> value();

}
