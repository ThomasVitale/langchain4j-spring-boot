package io.thomasvitale.langchain4j.autoconfigure.openai;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.openai.*;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAiAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_API_KEY", matches = ".*")
class OpenAiAutoConfigurationIT {

    private static final Logger log = LoggerFactory.getLogger(OpenAiAutoConfigurationIT.class);

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("langchain4j.openai.apiKey=" + System.getenv("LANGCHAIN4J_OPENAI_API_KEY"))
        .withConfiguration(AutoConfigurations.of(OpenAiAutoConfiguration.class));

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
    void chatStreaming() {
        contextRunner.run(context -> {
            OpenAiStreamingChatModel model = context.getBean(OpenAiStreamingChatModel.class);
            CompletableFuture<Response<AiMessage>> future = new CompletableFuture<>();
            model.generate("What is the capital of Italy?", new StreamingResponseHandler<AiMessage>() {
                @Override
                public void onNext(String token) {
                }

                @Override
                public void onComplete(Response<AiMessage> response) {
                    future.complete(response);
                }

                @Override
                public void onError(Throwable error) {
                }
            });

            Response<AiMessage> response = future.get(30, SECONDS);
            assertThat(response.content().text()).containsIgnoringCase("Rome");
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
        contextRunner.withPropertyValues("langchain4j.openai.image.size=256x256").run(context -> {
            OpenAiImageModel model = context.getBean(OpenAiImageModel.class);
            Image image = model.generate("sun").content();
            assertThat(image.url()).isNotNull();
            log.info("URL: " + image.url());
        });
    }

    @Test
    void moderation() {
        contextRunner.run(context -> {
            OpenAiModerationModel model = context.getBean(OpenAiModerationModel.class);
            Moderation moderation = model.moderate("He wants to kill them").content();
            assertThat(moderation.flagged()).isTrue();
        });
    }

}
