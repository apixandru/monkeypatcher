package com.github.apixandru.utils.monkeypatcher.patchall;

import com.github.apixandru.utils.monkeypatcher.ConfigParser;
import org.w3c.dom.Node;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 11, 2016
 */
public class PatchAllConfigParser implements ConfigParser<PatchAllConfig> {

    @Override
    public PatchAllConfig parse(final Node node) {
        return new PatchAllConfig();
    }

}
