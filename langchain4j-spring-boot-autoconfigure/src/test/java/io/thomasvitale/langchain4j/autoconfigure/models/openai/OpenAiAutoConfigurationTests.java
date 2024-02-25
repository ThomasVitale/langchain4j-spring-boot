package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.thomasvitale.langchain4j.spring.openai.OpenAIModerationModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiChatModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiEmbeddingModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiImageModel;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link OpenAiAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
class OpenAiAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("langchain4j.openai.client.api-key=demo")
        .withConfiguration(AutoConfigurations.of(RestClientAutoConfiguration.class, OpenAiAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAiChatModel.class);
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
            assertThat(context).hasSingleBean(OpenAIModerationModel.class);
        });
    }

    @Test
    void disabled() {
        contextRunner.withPropertyValues("langchain4j.openai.enabled=false").run(context -> {
            assertThat(context).doesNotHaveBean(OpenAiClient.class);
        });
    }

}
