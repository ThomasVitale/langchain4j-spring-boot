package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

import java.net.URI;

import dev.langchain4j.data.embedding.Embedding;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.spring.ollama.OllamaChatModel;
import io.thomasvitale.langchain4j.spring.ollama.OllamaEmbeddingModel;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaAutoConfiguration}.
 */
@Testcontainers
class OllamaAutoConfigurationIT {

    private static final Logger logger = LoggerFactory.getLogger(OllamaAutoConfigurationIT.class);

    private static final String MODEL_NAME = "orca-mini";

    @Container
    static OllamaContainer ollama = new OllamaContainer(DockerImageName
            .parse("ghcr.io/thomasvitale/ollama-%s".formatted(MODEL_NAME))
            .asCompatibleSubstituteFor("ollama/ollama"));

    private static URI getBaseUrl() {
        return URI.create("http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434)));
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withPropertyValues("langchain4j.ollama.chat.model=%s".formatted(MODEL_NAME))
        .withPropertyValues("langchain4j.ollama.embedding.model=%s".formatted(MODEL_NAME))
        .withConfiguration(AutoConfigurations.of(OllamaAutoConfiguration.class, RestClientAutoConfiguration.class));

    @Test
    void chat() {
        contextRunner.withPropertyValues("langchain4j.ollama.client.base-url=%s".formatted(getBaseUrl()))
            .run(context -> {
                OllamaChatModel model = context.getBean(OllamaChatModel.class);
                String response = model.generate("What is the capital of Italy?");
                logger.info("Response: " + response);
                assertThat(response).containsIgnoringCase("Rome");
            });
    }

    @Test
    void embedding() {
        contextRunner.withPropertyValues("langchain4j.ollama.client.base-url=%s".formatted(getBaseUrl()))
            .run(context -> {
                OllamaEmbeddingModel model = context.getBean(OllamaEmbeddingModel.class);
                Embedding embedding = model.embed("hi").content();
                assertThat(embedding.dimension()).isEqualTo(3200);
            });
    }

}
