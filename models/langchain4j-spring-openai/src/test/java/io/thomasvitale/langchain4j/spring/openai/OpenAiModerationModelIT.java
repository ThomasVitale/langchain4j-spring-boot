package io.thomasvitale.langchain4j.spring.openai;

import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.output.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.openai.api.moderation.ModerationModels;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAIModerationModel}.
 * <p>
 * Adapted from OpenAiModerationModelIT in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
class OpenAiModerationModelIT {

    private static OpenAiClient openAiClient;

    @BeforeAll
    static void beforeAll() {
        var apiKey = System.getenv("LANGCHAIN4J_OPENAI_CLIENT_API_KEY");
        openAiClient = new OpenAiClient(OpenAiClientConfig.builder().apiKey(apiKey).build(), RestClient.builder());
    }

    @Test
    void moderateTextNotToFlag() {
        OpenAIModerationModel moderationModel = OpenAIModerationModel.builder()
                .client(openAiClient)
                .options(OpenAiModerationOptions.builder()
                        .model(ModerationModels.TEXT_MODERATION_LATEST.toString()).build())
                .build();

        Response<Moderation> response = moderationModel.moderate("Welcome to the jungle");

        assertThat(response.content().flagged()).isFalse();
    }

    @Test
    void moderateTextToFlag() {
        OpenAIModerationModel moderationModel = OpenAIModerationModel.builder()
                .client(openAiClient)
                .options(OpenAiModerationOptions.builder()
                        .model(ModerationModels.TEXT_MODERATION_LATEST.toString()).build())
                .build();

        Response<Moderation> response = moderationModel.moderate("He wants to kill them");

        assertThat(response.content().flagged()).isTrue();
    }

}
