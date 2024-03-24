package io.thomasvitale.langchain4j.spring.ollama;

import java.net.URI;
import java.util.List;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.spring.ollama.api.Options;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaChatModel}.
 * <p>
 * Adapted from OllamaChatModelIT in the LangChain4j project.
 */
@Testcontainers
class OllamaChatModelIT {

    private static final Logger logger = LoggerFactory.getLogger(OllamaChatModelIT.class);

    private static final String MODEL_NAME = "orca-mini";

    @Container
    static OllamaContainer ollama = new OllamaContainer(DockerImageName
            .parse("ghcr.io/thomasvitale/ollama-%s".formatted(MODEL_NAME))
            .asCompatibleSubstituteFor("ollama/ollama"));

    private static OllamaClient ollamaClient;

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(ollama.getHost(), ollama.getMappedPort(11434));
    }

    @BeforeAll
    static void beforeAll() {
        ollamaClient = new OllamaClient(OllamaClientConfig.builder().baseUrl(URI.create(getBaseUrl())).build(),
                RestClient.builder());
    }

    @Test
    void generateText() {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().build())
            .build();

        var userMessage = UserMessage.from("What is the capital of Italy?");

        var response = ollamaChatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        var aiMessage = response.content();
        assertThat(aiMessage.text()).contains("Italy");
        assertThat(aiMessage.toolExecutionRequests()).isNull();

        var tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
            .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason()).isNull();
    }

    @Test
    void generateTextWithFewShots() {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().build())
            .build();

        var messages = List.of(UserMessage.from("1 + 1 ="), AiMessage.from(">>> 2"),

                UserMessage.from("2 + 2 ="), AiMessage.from(">>> 4"),

                UserMessage.from("4 + 4 ="));

        var response = ollamaChatModel.generate(messages);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).startsWith(">>> 8");
    }

    @Test
    void generateTextWithSystemMessage() {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().build())
            .build();

        var systemMessage = SystemMessage.from("Start every answer with Ahoy");
        var userMessage = UserMessage.from("Hello, captain!");

        var response = ollamaChatModel.generate(systemMessage, userMessage);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).containsIgnoringCase("Ahoy");
    }

    @Test
    void generateTextWithNumPredict() {
        var maximumOutputTokens = 1;
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .options(Options.builder().numPredict(maximumOutputTokens).build())
            .build();

        var userMessage = UserMessage.from("What is the capital of Italy?");

        var response = ollamaChatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        var aiMessage = response.content();
        assertThat(aiMessage.text()).doesNotContain("Italy");
        assertThat(response.tokenUsage().outputTokenCount()).isEqualTo(maximumOutputTokens);
    }

    @Test
    void generateTextAsJson() throws JSONException {
        var ollamaChatModel = OllamaChatModel.builder()
            .client(ollamaClient)
            .model(MODEL_NAME)
            .format("json")
            .options(Options.builder().build())
            .build();

        var response = ollamaChatModel
            .generate("Return a JSON object with two fields: location is Jungle and name is Jumanji.");
        logger.info("Response: \n" + response);

        JSONAssert.assertEquals("""
                {
                  "name": "Jumanji",
                  "location": "Jungle"
                }
                """, response, JSONCompareMode.STRICT);
    }

}
