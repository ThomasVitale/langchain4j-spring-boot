package io.thomasvitale.langchain4j.spring.openai.api.moderation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

/**
 * Represents policy compliance report by OpenAI's content moderation model against a given input.
 *
 * @param id The unique identifier for the moderation request.
 * @param model The model used to generate the moderation results.
 * @param results A list of moderation objects.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ModerationResponse(
        String id,
        String model,
        List<ModerationData> results
) {

    /**
     * Represents the moderation result for a given input.
     *
     * @param flagged Whether the content violates OpenAI's usage policies.
     * @param categories A list of the categories, and whether they are flagged or not.
     * @param categoryScores A list of the categories along with their scores as predicted by model.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ModerationData(
            Boolean flagged,
            Categories categories,
            CategoryScores categoryScores
    ) {}

}
