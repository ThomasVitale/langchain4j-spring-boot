package io.thomasvitale.langchain4j.spring.ollama;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;

import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaChatModel}.
 * <p>
 * Adapted from OllamaChatModeVisionModellIT in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
@Testcontainers
class OllamaChatModelVisionIT {

    private static final Logger logger = LoggerFactory.getLogger(OllamaChatModelVisionIT.class);

    private static final String MODEL_NAME = "llava";

    @Container
    static GenericContainer<?> ollama = new GenericContainer<>("ghcr.io/thomasvitale/ollama-%s".formatted(MODEL_NAME))
        .withExposedPorts(11434);

    private static OllamaClient ollamaClient;

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434));
    }

    @BeforeAll
    static void beforeAll() {
        ollamaClient = new OllamaClient(OllamaClientConfig.builder()
            .baseUrl(URI.create(getBaseUrl()))
            .readTimeout(Duration.ofMinutes(5))
            .build(), RestClient.builder());
    }

    @Test
    void generateTextWithImageFromHttp() {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().temperature(0.0).build())
            .build();

        var userMessage = UserMessage.from(TextContent.from("What's in the picture? Answer in a short sentence."),
                ImageContent
                    .from("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png"));

        var response = ollamaChatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).containsIgnoringCase("dice");
    }

    @Test
    void generateTextWithImageFromFile() throws IOException {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().temperature(0.0).build())
            .build();

        var imageFile = ResourceUtils.getFile("classpath:images/tabby-cat.png");

        var userMessage = UserMessage.from(TextContent.from("What's in the picture? Answer in a short sentence."),
                ImageContent.from("file:///" + imageFile.toPath()));

        var response = ollamaChatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).containsIgnoringCase("cat");
    }

}
