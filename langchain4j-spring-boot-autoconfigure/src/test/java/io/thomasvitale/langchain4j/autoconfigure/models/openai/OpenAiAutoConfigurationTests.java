package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import io.thomasvitale.langchain4j.autoconfigure.models.openai.OpenAiAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OpenAiAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
class OpenAiAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("langchain4j.openai.apiKey=demo")
        .withConfiguration(AutoConfigurations.of(OpenAiAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiChatModel.class);
        });
    }

    @Test
    void chatStreaming() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiStreamingChatModel.class);
        });
    }

    @Test
    void embedding() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiEmbeddingModel.class);
        });
    }

    @Test
    void image() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiImageModel.class);
        });
    }

    @Test
    void moderation() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiModerationModel.class);
        });
    }

}
