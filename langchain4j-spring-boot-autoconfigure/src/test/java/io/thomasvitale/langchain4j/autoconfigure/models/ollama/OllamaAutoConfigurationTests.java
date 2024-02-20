package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.thomasvitale.langchain4j.spring.ollama.OllamaChatModel;
import io.thomasvitale.langchain4j.spring.ollama.OllamaEmbeddingModel;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OllamaAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
class OllamaAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OllamaAutoConfiguration.class, RestClientAutoConfiguration.class));

    @Test
    void connectionDetails() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaConnectionDetails.class);
        });
    }

    @Test
    void chat() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaClient.class);
        });
    }

    @Test
    void chatStreaming() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaChatModel.class);
        });
    }

    @Test
    void embedding() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaEmbeddingModel.class);
        });
    }

    @Test
    void disabled() {
        contextRunner.withPropertyValues("langchain4j.ollama.enabled=false").run(context -> {
            assertThat(context).doesNotHaveBean(OllamaClient.class);
        });
    }

}
