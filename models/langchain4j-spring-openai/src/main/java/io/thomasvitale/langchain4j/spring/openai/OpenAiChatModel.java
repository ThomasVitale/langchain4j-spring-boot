package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatModelObservationContext;
import io.thomasvitale.langchain4j.spring.core.chat.observation.ChatModelObservationConvention;
import io.thomasvitale.langchain4j.spring.core.chat.observation.DefaultChatModelObservationConvention;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionRequest;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

import static java.util.Collections.singletonList;

/**
 * Model for chat completions using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 */
public class OpenAiChatModel implements ChatLanguageModel {

    private final OpenAiClient openAiClient;

    private final OpenAiChatOptions options;

    private final ObservationRegistry observationRegistry;

    private final ChatModelObservationConvention observationConvention = new DefaultChatModelObservationConvention();

    private OpenAiChatModel(OpenAiClient openAiClient, OpenAiChatOptions options, ObservationRegistry observationRegistry) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");
        Assert.notNull(observationRegistry, "observationRegistry cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages) {
        return generate(messages, null, null);
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages, List<ToolSpecification> toolSpecifications) {
        return generate(messages, toolSpecifications, null);
    }

    @Override
    public Response<AiMessage> generate(List<ChatMessage> messages, ToolSpecification toolSpecification) {
        return generate(messages, singletonList(toolSpecification), toolSpecification);
    }

    private Response<AiMessage> generate(List<ChatMessage> messages, @Nullable List<ToolSpecification> toolSpecifications, @Nullable ToolSpecification toolThatMustBeExecuted) {
        ChatCompletionRequest.Builder chatCompletionRequestBuilder = ChatCompletionRequest.builder()
                .messages(OpenAiAdapters.toOpenAiMessages(messages))
                .model(options.getModel())
                .frequencyPenalty(options.getFrequencyPenalty())
                .logitBias(options.getLogitBias())
                .logprobs(options.getLogprobs())
                .topLogprobs(options.getTopLogprobs())
                .maxTokens(options.getMaxTokens())
                .n(options.getN())
                .presencePenalty(options.getPresencePenalty())
                .responseFormat(options.getResponseFormat())
                .seed(options.getSeed())
                .stop(options.getStop())
                .temperature(options.getTemperature())
                .topP(options.getTopP())
                .user(options.getUser());

        if (!CollectionUtils.isEmpty(toolSpecifications)) {
            chatCompletionRequestBuilder.tools(OpenAiAdapters.toOpenAiTools(toolSpecifications));
        }

        if (toolThatMustBeExecuted != null) {
            chatCompletionRequestBuilder.toolChoice(OpenAiAdapters.toOpenAiToolChoice(toolThatMustBeExecuted));
        }

        ChatCompletionRequest request = chatCompletionRequestBuilder.build();

        ChatModelObservationContext observationContext = new ChatModelObservationContext("openai");
        observationContext.setModel(options.getModel());
        observationContext.setMessages(messages);
        observationContext.setTemperature(options.getTemperature());

        Response<AiMessage> modelResponse = Observation.createNotStarted(observationConvention, () -> observationContext, this.observationRegistry).observe(() -> {
            ChatCompletionResponse response = openAiClient.chatCompletion(request);

            if (response == null) {
                return null;
            }

            TokenUsage tokenUsage = OpenAiAdapters.toTokenUsage(response.usage());
            FinishReason finishReason = OpenAiAdapters.toFinishReason(response.choices().get(0).finishReason());

            observationContext.setTokenUsage(tokenUsage);
            observationContext.setFinishReason(finishReason);

            return Response.from(OpenAiAdapters.toAiMessage(response), tokenUsage, finishReason);
        });

        if (modelResponse == null) {
            throw new IllegalStateException("Model response is empty");
        }

        return modelResponse;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiChatOptions options;
        private ObservationRegistry observationRegistry = ObservationRegistry.NOOP;

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiChatOptions options) {
            this.options = options;
            return this;
        }

        public Builder observationRegistry(ObservationRegistry observationRegistry) {
            this.observationRegistry = observationRegistry;
            return this;
        }

        public OpenAiChatModel build() {
            return new OpenAiChatModel(openAiClient, options, observationRegistry);
        }
    }

}
