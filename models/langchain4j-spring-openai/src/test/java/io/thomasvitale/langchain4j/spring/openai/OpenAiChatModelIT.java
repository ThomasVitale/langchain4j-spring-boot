package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionFinishReason;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionRequest;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClientConfig;

import static dev.langchain4j.agent.tool.JsonSchemaProperty.INTEGER;
import static dev.langchain4j.data.message.UserMessage.userMessage;
import static dev.langchain4j.model.output.FinishReason.STOP;
import static dev.langchain4j.model.output.FinishReason.TOOL_EXECUTION;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAiChatModel}.
 * <p>
 * Adapted from OpenAiChatModelIT in the LangChain4j project.
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
class OpenAiChatModelIT {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiChatModelIT.class);

    public static final String MODEL_NAME = "gpt-3.5-turbo";

    private static OpenAiClient openAiClient;

    private ToolSpecification calculator = ToolSpecification.builder()
            .name("calculator")
            .description("returns a sum of two numbers")
            .addParameter("first", INTEGER)
            .addParameter("second", INTEGER)
            .build();

    @BeforeAll
    static void beforeAll() {
        var apiKey = System.getenv("LANGCHAIN4J_OPENAI_CLIENT_API_KEY");
        openAiClient = new OpenAiClient(OpenAiClientConfig.builder().apiKey(apiKey).build(), RestClient.builder());
    }

    @Test
    void generateText() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
            .client(openAiClient)
            .options(OpenAiChatOptions.builder().build())
            .build();

        UserMessage userMessage = UserMessage.from("What is the capital of Italy?");

        Response<AiMessage> response = chatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).contains("Rome");
        assertThat(aiMessage.toolExecutionRequests()).isNull();

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
            .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason().name()).isEqualToIgnoringCase(ChatCompletionFinishReason.STOP.name());
    }

    @Test
    void generateTextTooLong() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder()
                        .maxTokens(1).build())
                .build();

        UserMessage userMessage = UserMessage.from("What is the capital of Denmark?");

        Response<AiMessage> response = chatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).isNotBlank();

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isEqualTo(1);
        assertThat(tokenUsage.totalTokenCount())
                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason().name()).isEqualToIgnoringCase(ChatCompletionFinishReason.LENGTH.name());
    }

    @Test
    void generateTextWithFewShots() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().model(MODEL_NAME).build())
                .build();

        List<ChatMessage> messages = List.of(
                UserMessage.from("1 + 1 ="), AiMessage.from(">>> 2"),
                UserMessage.from("2 + 2 ="), AiMessage.from(">>> 4"),
                UserMessage.from("4 + 4 ="));

        Response<AiMessage> response = chatModel.generate(messages);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).startsWith(">>> 8");
    }

    @Test
    void generateTextWithSystemMessage() {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().model(MODEL_NAME).build())
                .build();

        SystemMessage systemMessage = SystemMessage.from("Start every answer with Ahoy");
        UserMessage userMessage = UserMessage.from("Hello, captain!");

        Response<AiMessage> response = chatModel.generate(systemMessage, userMessage);
        logger.info("Response: \n" + response);

        assertThat(response.content().text()).containsIgnoringCase("Ahoy");
    }

    @Test
    void generateTextWithNumPredict() {
        var maximumOutputTokens = 1;
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().model(MODEL_NAME).maxTokens(maximumOutputTokens).build())
                .build();

        UserMessage userMessage = UserMessage.from("What is the capital of Italy?");

        Response<AiMessage> response = chatModel.generate(userMessage);
        logger.info("Response: \n" + response);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).doesNotContain("Italy");
        assertThat(response.tokenUsage().outputTokenCount()).isEqualTo(maximumOutputTokens);
    }

    @Test
    void generateTextAsJson() throws JSONException {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().model(MODEL_NAME)
                        .responseFormat(new ChatCompletionRequest.ResponseFormat("json_object")).build())
                .build();

        String response = chatModel
            .generate("Return a JSON object with two fields: location is Jungle and name is Jumanji.");
        logger.info("Response: \n" + response);

        JSONAssert.assertEquals("""
                {
                  "name": "Jumanji",
                  "location": "Jungle"
                }
                """, response, JSONCompareMode.STRICT);
    }

    @Test
    void executeToolExplicitlyAndThenGenerateAnswer() throws JSONException {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().build())
                .build();

        // Execute tool

        UserMessage userMessage = userMessage("2+2=?");
        List<ToolSpecification> toolSpecifications = List.of(calculator);

        Response<AiMessage> response = chatModel.generate(List.of(userMessage), toolSpecifications);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).isNull();
        assertThat(aiMessage.toolExecutionRequests()).hasSize(1);

        ToolExecutionRequest toolExecutionRequest = aiMessage.toolExecutionRequests().get(0);
        assertThat(toolExecutionRequest.id()).isNotBlank();
        assertThat(toolExecutionRequest.name()).isEqualTo("calculator");
        JSONAssert.assertEquals("""
                {
                  "first": 2,
                  "second": 2
                }
                """, toolExecutionRequest.arguments(), JSONCompareMode.STRICT);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason()).isEqualTo(FinishReason.TOOL_EXECUTION);

        // Then answer

        ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, "4");
        List<ChatMessage> messages = List.of(userMessage, aiMessage, toolExecutionResultMessage);

        Response<AiMessage> secondResponse = chatModel.generate(messages);

        AiMessage secondAiMessage = secondResponse.content();
        assertThat(secondAiMessage.text()).contains("4");
        assertThat(secondAiMessage.toolExecutionRequests()).isNull();

        TokenUsage secondTokenUsage = secondResponse.tokenUsage();
        assertThat(secondTokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.totalTokenCount())
                .isEqualTo(secondTokenUsage.inputTokenCount() + secondTokenUsage.outputTokenCount());

        assertThat(secondResponse.finishReason()).isEqualTo(FinishReason.STOP);
    }

    @Test
    void executeToolImplicitlyAndThenGenerateAnswer() throws JSONException {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder().build())
                .build();

        // Execute tool

        UserMessage userMessage = userMessage("2+2=?");

        Response<AiMessage> response = chatModel.generate(List.of(userMessage), calculator);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).isNull();
        assertThat(aiMessage.toolExecutionRequests()).hasSize(1);

        ToolExecutionRequest toolExecutionRequest = aiMessage.toolExecutionRequests().get(0);
        assertThat(toolExecutionRequest.id()).isNotBlank();
        assertThat(toolExecutionRequest.name()).isEqualTo("calculator");
        JSONAssert.assertEquals("""
                {
                  "first": 2,
                  "second": 2
                }
                """, toolExecutionRequest.arguments(), JSONCompareMode.STRICT);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason()).isEqualTo(STOP); // Not sure if a bug in OpenAI or stop is expected here

        // Then answer

        ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, "4");
        List<ChatMessage> messages = List.of(userMessage, aiMessage, toolExecutionResultMessage);

        Response<AiMessage> secondResponse = chatModel.generate(messages);

        AiMessage secondAiMessage = secondResponse.content();
        assertThat(secondAiMessage.text()).contains("4");
        assertThat(secondAiMessage.toolExecutionRequests()).isNull();

        TokenUsage secondTokenUsage = secondResponse.tokenUsage();
        assertThat(secondTokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.totalTokenCount())
                .isEqualTo(secondTokenUsage.inputTokenCount() + secondTokenUsage.outputTokenCount());

        assertThat(secondResponse.finishReason()).isEqualTo(STOP);
    }

    @Test
    void executeMultipleToolsInParallelThenAnswer() throws JSONException {
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .client(openAiClient)
                .options(OpenAiChatOptions.builder()
                        .model("gpt-3.5-turbo-1106")
                        .temperature(0.0)
                        .build())
                .build();

        // Execute multiple tools

        UserMessage userMessage = userMessage("2+2=? 3+3=?");
        List<ToolSpecification> toolSpecifications = List.of(calculator);

        Response<AiMessage> response = chatModel.generate(List.of(userMessage), toolSpecifications);

        AiMessage aiMessage = response.content();
        assertThat(aiMessage.text()).isNull();
        assertThat(aiMessage.toolExecutionRequests()).hasSize(2);

        ToolExecutionRequest toolExecutionRequest1 = aiMessage.toolExecutionRequests().get(0);
        assertThat(toolExecutionRequest1.name()).isEqualTo("calculator");
        JSONAssert.assertEquals("""
                {
                  "first": 2,
                  "second": 2
                }
                """, toolExecutionRequest1.arguments(), JSONCompareMode.STRICT);

        ToolExecutionRequest toolExecutionRequest2 = aiMessage.toolExecutionRequests().get(1);
        assertThat(toolExecutionRequest2.name()).isEqualTo("calculator");
        JSONAssert.assertEquals("""
                {
                  "first": 3,
                  "second": 3
                }
                """, toolExecutionRequest2.arguments(), JSONCompareMode.STRICT);

        TokenUsage tokenUsage = response.tokenUsage();
        assertThat(tokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(tokenUsage.totalTokenCount())
                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());

        assertThat(response.finishReason()).isEqualTo(TOOL_EXECUTION);

        // Then answer

        ToolExecutionResultMessage toolExecutionResultMessage1 = ToolExecutionResultMessage.from(toolExecutionRequest1, "4");
        ToolExecutionResultMessage toolExecutionResultMessage2 = ToolExecutionResultMessage.from(toolExecutionRequest2, "6");

        List<ChatMessage> messages = List.of(userMessage, aiMessage, toolExecutionResultMessage1, toolExecutionResultMessage2);

        Response<AiMessage> secondResponse = chatModel.generate(messages);

        AiMessage secondAiMessage = secondResponse.content();
        assertThat(secondAiMessage.text()).contains("4", "6");
        assertThat(secondAiMessage.toolExecutionRequests()).isNull();

        TokenUsage secondTokenUsage = secondResponse.tokenUsage();
        assertThat(secondTokenUsage.inputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.outputTokenCount()).isGreaterThan(0);
        assertThat(secondTokenUsage.totalTokenCount())
                .isEqualTo(secondTokenUsage.inputTokenCount() + secondTokenUsage.outputTokenCount());

        assertThat(secondResponse.finishReason()).isEqualTo(STOP);
    }

}
