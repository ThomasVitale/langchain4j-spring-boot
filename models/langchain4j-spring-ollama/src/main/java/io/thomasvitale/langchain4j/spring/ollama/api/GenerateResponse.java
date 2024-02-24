package io.thomasvitale.langchain4j.spring.ollama.api;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Represents the response from the Ollama API for completion requests to /api/generate.
 *
 * @param model the model name
 * @param createdAt when the response was created
 * @param response empty if the response was streamed, if not streamed, this will contain
 *                 the full response
 * @param done whether it's the final response. If it is, the response also includes
 *             additional data about the generation
 * @param context an encoding of the conversation used in this response, this can be sent
 *                in the next request to keep a conversational memory
 * @param totalDuration time spent generating the response
 * @param loadDuration time spent in nanoseconds loading the model
 * @param promptEvalCount number of tokens in the prompt
 * @param promptEvalDuration time spent in nanoseconds evaluating the prompt
 * @param evalCount number of tokens the response
 * @param evalDuration time in nanoseconds spent generating the response
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GenerateResponse(
        String model,
        Instant createdAt,
        String response,
        Boolean done,
        List<Integer> context,
        Duration totalDuration,
        Duration loadDuration,
        Integer promptEvalCount,
        Duration promptEvalDuration,
        Integer evalCount,
        Duration evalDuration
) {}
