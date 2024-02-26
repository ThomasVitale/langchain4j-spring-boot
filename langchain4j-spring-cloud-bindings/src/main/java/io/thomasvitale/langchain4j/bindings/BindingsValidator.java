package io.thomasvitale.langchain4j.bindings;

import org.springframework.core.env.Environment;

/**
 * From https://github.com/spring-cloud/spring-cloud-bindings to switch on/off the bindings.
 */
final class BindingsValidator {

    static final String CONFIG_PATH = "langchain4j.cloud.bindings";

    /**
     * Where the given binding type should be used to contribute properties.
     */
    static boolean isTypeEnabled(Environment environment, String type) {
        return environment.getProperty(
                "%s.%s.enable".formatted(CONFIG_PATH, type),
                Boolean.class, true);
    }

}
