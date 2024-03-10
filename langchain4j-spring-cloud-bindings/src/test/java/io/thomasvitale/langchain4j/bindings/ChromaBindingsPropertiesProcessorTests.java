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
 * Unit tests for {@link ChromaBindingsPropertiesProcessor}.
 */
class ChromaBindingsPropertiesProcessorTests {

    private final Bindings bindings = new Bindings(
            new Binding("test-name", Paths.get("test-path"),
            new FluentMap()
                    .withEntry(Binding.TYPE, ChromaBindingsPropertiesProcessor.TYPE)
                    .withEntry("uri", "https://kadras.io/chroma:8000")
                    .withEntry("username", "itsme")
                    .withEntry("password", "youknowit")
            ));

    private final MockEnvironment environment = new MockEnvironment();

    private final Map<String, Object> properties = new HashMap<>();

    @Test
    void propertiesAreContributed() {
        new ChromaBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).containsEntry("langchain4j.vectorstore.chroma.client.url", "https://kadras.io/chroma:8000");
        assertThat(properties).containsEntry("langchain4j.vectorstore.chroma.client.username", "itsme");
        assertThat(properties).containsEntry("langchain4j.vectorstore.chroma.client.password", "youknowit");
    }

    @Test
    void whenDisabledThenPropertiesAreNotContributed() {
        environment.setProperty("%s.chroma.enabled".formatted(CONFIG_PATH), "false");

        new ChromaBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).isEmpty();
    }

}
