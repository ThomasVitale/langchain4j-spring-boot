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
 * Unit tests for {@link OllamaBindingsPropertiesProcessor}.
 *
 * @author Thomas Vitale
 */
class OllamaBindingsPropertiesProcessorTests {

    private final Bindings bindings = new Bindings(
            new Binding("test-name", Paths.get("test-path"),
            new FluentMap()
                    .withEntry(Binding.TYPE, OllamaBindingsPropertiesProcessor.TYPE)
                    .withEntry("uri", "https://kadras.io/ollama:11434")));

    private final MockEnvironment environment = new MockEnvironment();

    private final Map<String, Object> properties = new HashMap<>();

    @Test
    void propertiesAreContributed() {
        new OllamaBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).containsEntry("langchain4j.ollama.client.base-url", "https://kadras.io/ollama:11434");
    }

    @Test
    void whenDisabledThenPropertiesAreNotContributed() {
        environment.setProperty("%s.ollama.enabled".formatted(CONFIG_PATH), "false");

        new OllamaBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).isEmpty();
    }

}
