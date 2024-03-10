package io.thomasvitale.langchain4j.bindings;

import java.util.HashMap;

/**
 * From https://github.com/spring-cloud/spring-cloud-bindings to simplify the tests.
 */
public final class FluentMap extends HashMap<String, String> {

    public FluentMap withEntry(String key, String value) {
        put(key, value);
        return this;
    }

}
