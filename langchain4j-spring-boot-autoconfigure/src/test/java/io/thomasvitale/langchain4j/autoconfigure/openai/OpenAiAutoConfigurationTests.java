package io.thomasvitale.langchain4j.autoconfigure.openai;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

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
            OpenAiChatModel model = context.getBean(OpenAiChatModel.class);
            assertThat(model).isInstanceOf(OpenAiChatModel.class);
        });
    }

    @Test
    void chatStreaming() {
        contextRunner.run(context -> {
            OpenAiStreamingChatModel model = context.getBean(OpenAiStreamingChatModel.class);
            assertThat(model).isInstanceOf(OpenAiStreamingChatModel.class);
        });
    }

    @Test
    void embedding() {
        contextRunner.run(context -> {
            OpenAiEmbeddingModel model = context.getBean(OpenAiEmbeddingModel.class);
            assertThat(model).isInstanceOf(OpenAiEmbeddingModel.class);
        });
    }

    @Test
    void image() {
        contextRunner.run(context -> {
            OpenAiImageModel model = context.getBean(OpenAiImageModel.class);
            assertThat(model).isInstanceOf(OpenAiImageModel.class);
        });
    }

    @Test
    void moderation() {
        contextRunner.run(context -> {
            OpenAiModerationModel model = context.getBean(OpenAiModerationModel.class);
            assertThat(model).isInstanceOf(OpenAiModerationModel.class);
        });
    }

}
