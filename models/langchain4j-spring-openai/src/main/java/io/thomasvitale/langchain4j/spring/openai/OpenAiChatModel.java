package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionRequest;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionResponse;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;

import static java.util.Collections.singletonList;

/**
 * Model for chat completions using OpenAI.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class OpenAiChatModel implements ChatLanguageModel {

    private final OpenAiClient openAiClient;

    private final OpenAiChatOptions options;

    private OpenAiChatModel(OpenAiClient openAiClient, OpenAiChatOptions options) {
        Assert.notNull(openAiClient, "openAiClient cannot be null");
        Assert.notNull(options, "options cannot be null");

        this.openAiClient = openAiClient;
        this.options = options;
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

        ChatCompletionResponse response = openAiClient.chatCompletion(request);

        if (response == null) {
            throw new IllegalStateException("Chat completion response is empty");
        }

        return Response.from(
                OpenAiAdapters.toAiMessage(response),
                OpenAiAdapters.toTokenUsage(response.usage()),
                OpenAiAdapters.toFinishReason(response.choices().get(0).finishReason())
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private OpenAiClient openAiClient;
        private OpenAiChatOptions options;

        public Builder client(OpenAiClient openAiClient) {
            this.openAiClient = openAiClient;
            return this;
        }

        public Builder options(OpenAiChatOptions options) {
            this.options = options;
            return this;
        }

        public OpenAiChatModel build() {
            return new OpenAiChatModel(openAiClient, options);
        }
    }

}
