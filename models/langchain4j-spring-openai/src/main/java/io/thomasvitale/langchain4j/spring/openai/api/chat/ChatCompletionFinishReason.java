package io.thomasvitale.langchain4j.spring.openai.api.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The reason the model stopped generating tokens.
 */
public enum ChatCompletionFinishReason {
    /**
     * The model hit a natural stop point or a provided stop sequence.
     */
    @JsonProperty("stop") STOP,
    /**
     * The maximum number of tokens specified in the request was reached.
     */
    @JsonProperty("length") LENGTH,
    /**
     * The content was omitted due to a flag from our content filters.
     */
    @JsonProperty("content_filter") CONTENT_FILTER,
    /**
     * The model called a tool.
     */
    @JsonProperty("tool_calls") TOOL_CALLS
}
