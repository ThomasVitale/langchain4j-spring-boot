package io.thomasvitale.langchain4j.autoconfigure.ollama;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OllamaAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
class OllamaAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(OllamaAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaChatModel.class);
        });
    }

    @Test
    void chatStreaming() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaStreamingChatModel.class);
        });
    }

    @Test
    void embedding() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OllamaEmbeddingModel.class);
        });
    }

}
