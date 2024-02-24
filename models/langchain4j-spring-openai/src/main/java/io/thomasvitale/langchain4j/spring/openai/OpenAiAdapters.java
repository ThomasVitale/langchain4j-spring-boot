package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolParameters;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ContentType;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionFinishReason;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionMessage;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionRequest;
import io.thomasvitale.langchain4j.spring.openai.api.chat.ChatCompletionResponse;
import io.thomasvitale.langchain4j.spring.openai.api.chat.Tool;
import io.thomasvitale.langchain4j.spring.openai.api.embedding.EmbeddingResponse;
import io.thomasvitale.langchain4j.spring.openai.api.image.ImageGenerationResponse;
import io.thomasvitale.langchain4j.spring.openai.api.shared.Usage;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Utility class to convert between OpenAI and Langchain4j types.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public final class OpenAiAdapters {

    /**
     * Converts from a list of LangChain4J ChatMessage to a list of OpenAI ChatCompletionMessage.
     */
    public static List<ChatCompletionMessage> toOpenAiMessages(List<ChatMessage> messages) {
        return messages.stream()
                .map(OpenAiAdapters::toOpenAiMessage)
                .toList();
    }

    /**
     * Converts from LangChain4J ChatMessage to ChatCompletionMessage.
     */
    private static ChatCompletionMessage toOpenAiMessage(ChatMessage message) {
        if (message instanceof SystemMessage systemMessage) {
            return ChatCompletionMessage.builder()
                    .role(ChatCompletionMessage.Role.SYSTEM)
                    .content(systemMessage.text())
                    .build();
        }

        if (message instanceof UserMessage userMessage) {
            if (userMessage.hasSingleText()) {
                return ChatCompletionMessage.builder()
                        .role(ChatCompletionMessage.Role.USER)
                        .content(userMessage.text())
                        .name(userMessage.name())
                        .build();
            } else {
                return ChatCompletionMessage.builder()
                        .content(userMessage.contents().stream()
                                .map(OpenAiAdapters::toOpenAiContent)
                                .collect(toList()))
                        .name(userMessage.name())
                        .build();
            }
        }

        if (message instanceof AiMessage aiMessage) {
            if (!aiMessage.hasToolExecutionRequests()) {
                return ChatCompletionMessage.builder()
                        .role(ChatCompletionMessage.Role.ASSISTANT)
                        .content(aiMessage.text())
                        .build();
            }

            return ChatCompletionMessage.builder()
                    .role(ChatCompletionMessage.Role.ASSISTANT)
                    .toolCalls(toOpenAiToolCalls(aiMessage.toolExecutionRequests()))
                    .build();
        }

        if (message instanceof ToolExecutionResultMessage toolExecutionResultMessage) {
            return ChatCompletionMessage.builder()
                    .role(ChatCompletionMessage.Role.TOOL)
                    .content(toolExecutionResultMessage.text())
                    .toolCallId(toolExecutionResultMessage.id())
                    .build();
        }

        throw new IllegalArgumentException("Unknown message type: " + message.type());
    }

    /**
     * Converts from LangChain4J Content to OpenAI Content.
     */
    public static ChatCompletionMessage.Content toOpenAiContent(Content content) {
        if (ContentType.TEXT.equals(content.type())) {
            var textContent = (TextContent) content;
            return ChatCompletionMessage.Content.builder()
                    .type(ChatCompletionMessage.ContentType.TEXT)
                    .text(textContent.text())
                    .build();
        } else if (ContentType.IMAGE.equals(content.type())) {
            var imageContent = (ImageContent) content;
            return ChatCompletionMessage.Content.builder()
                    .type(ChatCompletionMessage.ContentType.IMAGE_URL)
                    .imageUrl(new ChatCompletionMessage.ImageUrl(
                            toOpenAiUrl(imageContent.image()),
                            toOpenAiDetail(imageContent.detailLevel())))
                    .build();
        } else {
            throw new IllegalArgumentException("Unknown content type: " + content.type());
        }
    }

    /**
     * Converts from LangChain4J Image to OpenAI Image URL.
     */
    private static String toOpenAiUrl(Image image) {
        if (image.url() != null) {
            return image.url().toString();
        }
        return format("data:%s;base64,%s", image.mimeType(), image.base64Data());
    }

    /**
     * Converts from LangChain4J DetailLevel to OpenAI DetailLevel.
     */
    private static String toOpenAiDetail(ImageContent.DetailLevel detailLevel) {
        if (detailLevel == null) {
            return null;
        }
        return detailLevel.name();
    }

    /**
     * Converts from a list of LangChain4J ToolExecutionRequest to a list of OpenAI ToolCall.
     */
    private static List<ChatCompletionMessage.ToolCall> toOpenAiToolCalls(List<ToolExecutionRequest> toolExecutionRequests) {
        return toolExecutionRequests.stream()
                .map(OpenAiAdapters::toOpenAiToolCall)
                .toList();
    }

    /**
     * Converts from LangChain4J ToolExecutionRequest to OpenAI ToolCall.
     */
    private static ChatCompletionMessage.ToolCall toOpenAiToolCall(ToolExecutionRequest toolExecutionRequest) {
        var functionCall = new ChatCompletionMessage.ChatCompletionFunction(
                toolExecutionRequest.name(),
                toolExecutionRequest.arguments());
        return new ChatCompletionMessage.ToolCall(toolExecutionRequest.id(), functionCall);
    }

    /**
     * Converts from LangChain4J ToolSpecification to OpenAI ToolChoice.
     */
    public static ChatCompletionRequest.ToolChoice toOpenAiToolChoice(ToolSpecification toolSpecification) {
        return new ChatCompletionRequest.ToolChoice(toolSpecification.name());
    }

    /**
     * Converts from a list of LangChain4J ToolSpecification to a list of OpenAI Tool.
     */
    public static List<Tool> toOpenAiTools(List<ToolSpecification> toolSpecifications) {
        return toolSpecifications.stream()
                .map(OpenAiAdapters::toOpenAiTool)
                .toList();
    }

    /**
     * Converts from LangChain4J ToolSpecification to OpenAI Tool.
     */
    private static Tool toOpenAiTool(ToolSpecification toolSpecification) {
        var function = Tool.Function.builder()
                .description(toolSpecification.description())
                .name(toolSpecification.name())
                .parameters(OpenAiAdapters.toOpenAiParameters(toolSpecification.parameters()))
                .build();
        return new Tool(function);
    }

    /**
     * Converts from LangChain4J ToolParameters to OpenAI Tool.Parameters.
     */
    private static Tool.Parameters toOpenAiParameters(@Nullable ToolParameters toolParameters) {
        if (toolParameters == null) {
            return Tool.Parameters.builder().build();
        }
        return Tool.Parameters.builder()
                .properties(toolParameters.properties())
                .required(toolParameters.required())
                .build();
    }

    /**
     * Converts from OpenAI Usage to LangChain4J Usage.
     */
    public static TokenUsage toTokenUsage(Usage usage) {
        return new TokenUsage(usage.promptTokens(), usage.completionTokens(), usage.totalTokens());
    }

    /**
     * Converts from OpenAI ChatCompletionResponse to LangChain4J AiMessage.
     */
    public static AiMessage toAiMessage(ChatCompletionResponse response) {
        var assistantMessage = response.choices().get(0).message();
        var toolCalls = assistantMessage.toolCalls();

        if (!(CollectionUtils.isEmpty(toolCalls))) {
            List<ToolExecutionRequest> toolExecutionRequests = toolCalls.stream()
                    .filter(toolCall -> "function".equals(toolCall.type()))
                    .map(OpenAiAdapters::toToolExecutionRequest)
                    .toList();
            return AiMessage.from(toolExecutionRequests);
        }

        return AiMessage.from((String) assistantMessage.content());
    }

    /**
     * Converts from OpenAI ToolCall to LangChain4J ToolExecutionRequest.
     */
    private static ToolExecutionRequest toToolExecutionRequest(ChatCompletionMessage.ToolCall toolCall) {
        return ToolExecutionRequest.builder()
                .id(toolCall.id())
                .name(toolCall.function().name())
                .arguments(toolCall.function().arguments())
                .build();
    }

    /**
     * Converts from OpenAI ChatCompletionFinishReason to LangChain4J FinishReason.
     */
    public static FinishReason toFinishReason(ChatCompletionFinishReason finishReason) {
        return switch (finishReason) {
            case STOP -> FinishReason.STOP;
            case LENGTH -> FinishReason.LENGTH;
            case TOOL_CALLS -> FinishReason.TOOL_EXECUTION;
            case CONTENT_FILTER -> FinishReason.CONTENT_FILTER;
        };
    }

    /**
     * Converts from OpenAI EmbeddingData to LangChain4J Embedding.
     */
    public static Embedding toEmbedding(EmbeddingResponse.EmbeddingData embeddingData) {
        var floatVectors = embeddingData.embedding().stream()
                .map(Double::floatValue)
                .toList();
        return Embedding.from(floatVectors);
    }

    /**
     * Converts from OpenAI ImageData to LangChain4J Image.
     */
    public static Image toImage(ImageGenerationResponse.ImageData imageData) {
        return Image.builder()
                .url(imageData.url())
                .base64Data(imageData.b64Json())
                .revisedPrompt(imageData.revisedPrompt())
                .build();
    }

}
