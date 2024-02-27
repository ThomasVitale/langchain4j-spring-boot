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
 * Unit tests for {@link OpenAiBindingsPropertiesProcessor}.
 *
 * @author Thomas Vitale
 */
class OpenAiBindingsPropertiesProcessorTests {

    private final Bindings bindings = new Bindings(
            new Binding("test-name", Paths.get("test-path"),
            new FluentMap()
                    .withEntry(Binding.TYPE, OpenAiBindingsPropertiesProcessor.TYPE)
                    .withEntry("api-key", "demo")
                    .withEntry("organization", "why-do-you-care")
                    .withEntry("user", "magical-me")
                    .withEntry("uri", "https://api.openai.com")
            ));

    private final MockEnvironment environment = new MockEnvironment();

    private final Map<String, Object> properties = new HashMap<>();

    @Test
    void propertiesAreContributed() {
        new OpenAiBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).containsEntry("langchain4j.open-ai.client.api-key", "demo");
        assertThat(properties).containsEntry("langchain4j.open-ai.client.base-url", "https://api.openai.com");
        assertThat(properties).containsEntry("langchain4j.open-ai.client.organization-id", "why-do-you-care");
        assertThat(properties).containsEntry("langchain4j.open-ai.client.user", "magical-me");
    }

    @Test
    void whenDisabledThenPropertiesAreNotContributed() {
        environment.setProperty("%s.open-ai.enabled".formatted(CONFIG_PATH), "false");

        new OpenAiBindingsPropertiesProcessor().process(environment, bindings, properties);
        assertThat(properties).isEmpty();
    }

}
