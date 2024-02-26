package io.thomasvitale.langchain4j.bindings;

import java.util.Map;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

/**
 * An implementation of {@link BindingsPropertiesProcessor} that detects {@link Binding}s of type: {@value TYPE}.
 */
public class OllamaBindingsPropertiesProcessor implements BindingsPropertiesProcessor {

    /**
     * The {@link Binding} type that this processor is interested in: {@value}.
     **/
    public static final String TYPE = "ollama";

    @Override
    public void process(Environment environment, Bindings bindings, Map<String, Object> properties) {
        if (!BindingsValidator.isTypeEnabled(environment, TYPE)) {
            return;
        }

        bindings.filterBindings(TYPE).forEach(binding -> {
            properties.put("langchain4j.ollama.client.base-url", binding.getSecret().get("uri"));
        });
    }

}
