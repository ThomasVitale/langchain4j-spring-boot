package io.thomasvitale.langchain4j.bindings;

import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.cloud.bindings.boot.BindingsPropertiesProcessor;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * An implementation of {@link BindingsPropertiesProcessor} that detects {@link Binding}s of type: {@value TYPE}.
 */
public class ChromaBindingsPropertiesProcessor implements BindingsPropertiesProcessor {

    /**
     * The {@link Binding} type that this processor is interested in: {@value}.
     **/
    public static final String TYPE = "chroma";

    @Override
    public void process(Environment environment, Bindings bindings, Map<String, Object> properties) {
        if (!BindingsValidator.isTypeEnabled(environment, TYPE)) {
            return;
        }

        bindings.filterBindings(TYPE).forEach(binding -> {
            properties.put("langchain4j.vectorstore.chroma.client.url", binding.getSecret().get("uri"));
            properties.put("langchain4j.vectorstore.chroma.client.username", binding.getSecret().get("username"));
            properties.put("langchain4j.vectorstore.chroma.client.password", binding.getSecret().get("password"));
        });
    }

}
