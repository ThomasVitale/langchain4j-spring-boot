package io.thomasvitale.langchain4j.spring.openai.api.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A list of the categories along with their scores as predicted by model.
 *
 * @param hate The score for the category 'hate'.
 * @param hateThreatening The score for the category 'hate/threatening'.
 * @param harassment The score for the category 'harassment'.
 * @param harassmentThreatening The score for the category 'harassment/threatening'.
 * @param selfHarm The score for the category 'self-harm'.
 * @param selfHarmIntent The score for the category 'self-harm/intent'.
 * @param selfHarmInstructions The score for the category 'self-harm/instructions'.
 * @param sexual The score for the category 'sexual'.
 * @param sexualMinors The score for the category 'sexual/minors'.
 * @param violence The score for the category 'violence'.
 * @param violenceGraphic The score for the category 'violence/graphic'.
 */
public record CategoryScores(
        Double hate,

        @JsonProperty("hate/threatening")
        Double hateThreatening,

        Double harassment,

        @JsonProperty("harassment/threatening")
        Double harassmentThreatening,

        @JsonProperty("self-harm")
        Double selfHarm,

        @JsonProperty("self-harm/intent")
        Double selfHarmIntent,

        @JsonProperty("self-harm/instructions")
        Double selfHarmInstructions,

        Double sexual,

        @JsonProperty("sexual/minors")
        Double sexualMinors,

        Double violence,

        @JsonProperty("violence/graphic")
        Double violenceGraphic
) {}
