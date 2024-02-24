package io.thomasvitale.langchain4j.autoconfigure.models.openai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.moderation.Moderation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import io.thomasvitale.langchain4j.spring.openai.OpenAIModerationModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiChatModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiEmbeddingModel;
import io.thomasvitale.langchain4j.spring.openai.OpenAiImageModel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAiAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
class OpenAiAutoConfigurationIT {

    private static final Logger log = LoggerFactory.getLogger(OpenAiAutoConfigurationIT.class);

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("langchain4j.openai.client.apiKey=" + System.getenv("LANGCHAIN4J_OPENAI_CLIENT_API_KEY"))
        .withConfiguration(AutoConfigurations.of(RestClientAutoConfiguration.class, OpenAiAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner.run(context -> {
            OpenAiChatModel model = context.getBean(OpenAiChatModel.class);
            String response = model.generate("What is the capital of Italy?");
            assertThat(response).containsIgnoringCase("Rome");
            log.info("Response: " + response);
        });
    }

    @Test
    void embedding() {
        contextRunner.run(context -> {
            OpenAiEmbeddingModel model = context.getBean(OpenAiEmbeddingModel.class);
            Embedding embedding = model.embed("hi").content();
            assertThat(embedding.dimension()).isEqualTo(1536);
        });
    }

    @Test
    void image() {
        contextRunner.withPropertyValues("langchain4j.openai.image.options.size=256x256").run(context -> {
            OpenAiImageModel model = context.getBean(OpenAiImageModel.class);
            Image image = model.generate("sun").content();
            assertThat(image.url()).isNotNull();
            log.info("URL: " + image.url());
        });
    }

    @Test
    void moderation() {
        contextRunner.run(context -> {
            OpenAIModerationModel model = context.getBean(OpenAIModerationModel.class);
            Moderation moderation = model.moderate("He wants to kill them").content();
            assertThat(moderation.flagged()).isTrue();
        });
    }

}
