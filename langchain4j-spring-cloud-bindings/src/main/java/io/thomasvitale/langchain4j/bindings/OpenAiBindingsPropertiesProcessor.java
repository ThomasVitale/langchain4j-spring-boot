package io.thomasvitale.langchain4j.bindings;

import java.util.Map;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

/**
 * An implementation of {@link BindingsPropertiesProcessor} that detects {@link Binding}s of type: {@value TYPE}.
 */
public class OpenAiBindingsPropertiesProcessor implements BindingsPropertiesProcessor {

    /**
     * The {@link Binding} type that this processor is interested in: {@value}.
     **/
    public static final String TYPE = "open-ai";

    @Override
    public void process(Environment environment, Bindings bindings, Map<String, Object> properties) {
        if (!BindingsValidator.isTypeEnabled(environment, TYPE)) {
            return;
        }

        bindings.filterBindings(TYPE).forEach(binding -> {
            properties.put("langchain4j.open-ai.client.api-key", binding.getSecret().get("api-key"));
            properties.put("langchain4j.open-ai.client.base-url", binding.getSecret().get("uri"));
            properties.put("langchain4j.open-ai.client.organization-id", binding.getSecret().get("organization"));
            properties.put("langchain4j.open-ai.client.user", binding.getSecret().get("user"));
        });
    }

}
