package io.thomasvitale.langchain4j.spring.openai.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Usage statistics for the completion request.
 *
 * @param completionTokens Number of tokens in the generated completion.
 * @param promptTokens Number of tokens in the prompt.
 * @param totalTokens Total number of tokens used in the request (prompt + completion).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Usage(
        Integer completionTokens,
        Integer promptTokens,
        Integer totalTokens
) {}
