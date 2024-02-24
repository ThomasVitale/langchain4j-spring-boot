package io.thomasvitale.langchain4j.spring.openai.api.chat;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Log probability information for the choice.
 *
 * @param content A list of message content tokens with log probability information.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LogProbs(
        List<Content> content
) {

    /**
     * Message content tokens with log probability information.
     *
     * @param token The token.
     * @param logprob The log probability of this token.
     * @param bytes A list of integers representing the UTF-8 bytes representation of the token.
     *              Useful in instances where characters are represented by multiple tokens
     *              and their byte representations must be combined to generate the correct
     *              text representation. Can be 'null' if there is no bytes representation
     *              for the token.
     * @param topLogprobs List of the most likely tokens and their log probability, at this token
     *                    position. In rare cases, there may be fewer than the number of requested
     *                    'top_logprobs' returned.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Content(
            String token,
            Double logprob,
            List<Integer> bytes,
            List<TopLogProbs> topLogprobs
    ) {

        /**
         * The most likely tokens and their log probability, at this token position.
         *
         * @param token The token.
         * @param logprob The log probability of this token.
         * @param bytes A list of integers representing the UTF-8 bytes representation of the token.
         *              Useful in instances where characters are represented by multiple tokens
         *              and their byte representations must be combined to generate the correct
         *              text representation. Can be null if there is no bytes representation
         *              for the token.
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public record TopLogProbs(
                String token,
                Double logprob,
                List<Integer> bytes
        ) {}

    }

}
