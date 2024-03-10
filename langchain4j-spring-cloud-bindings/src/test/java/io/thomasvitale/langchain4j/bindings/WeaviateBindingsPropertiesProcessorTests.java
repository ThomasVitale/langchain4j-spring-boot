package io.thomasvitale.langchain4j.bindings;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.bindings.Binding;
import org.springframework.cloud.bindings.Bindings;
import org.springframework.mock.env.MockEnvironment;

import static io.thomasvitale.langchain4j.bindings.BindingsValidator.CONFIG_PATH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link WeaviateBindingsPropertiesProcessor}.
 */
class WeaviateBindingsPropertiesProcessorTests {

    private final Bindings bindings = new Bindings(
            new Binding("test-name", Paths.get("test-path"),
            new FluentMap()
                    .withEntry(Binding.TYPE, WeaviateBindingsPropertiesProcessor.TYPE)
                    .withEntry("uri", "https://kadras.io/weaviate:8000")
                    .withEntry("api-key", "demo")
            ));

    private final MockEnvironment environment = new MockEnvironment();

    private final Map<String, Object> properties = new HashMap<>();

    @Test
    void propertiesAreContributed() {
        new WeaviateBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).containsEntry("langchain4j.vectorstore.weaviate.client.api-key", "demo");
        assertThat(properties).containsEntry("langchain4j.vectorstore.weaviate.client.url", "https://kadras.io/weaviate:8000");
    }

    @Test
    void whenDisabledThenPropertiesAreNotContributed() {
        environment.setProperty("%s.weaviate.enabled".formatted(CONFIG_PATH), "false");

        new WeaviateBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).isEmpty();
    }

}
