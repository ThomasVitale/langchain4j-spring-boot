package io.thomasvitale.langchain4j.spring.openai.client;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents an error response from the OpenAI API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseError(Error error) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Error(
            String message,
            String type,
            String param,
            String code
    ) {}
}
