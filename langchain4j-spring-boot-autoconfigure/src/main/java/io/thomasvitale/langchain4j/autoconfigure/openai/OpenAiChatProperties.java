package io.thomasvitale.langchain4j.autoconfigure.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for OpenAI chat clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OpenAiChatProperties.CONFIG_PREFIX)
public class OpenAiChatProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.chat";

    /**
     * Name of the model to use.
     */
    private String model = "gpt-3.5-turbo";

    /**
     * What sampling temperature to use, with values between 0 and 2.
     * <p>
     * Higher values will make the output more random, while lower values will make it
     * more focused and deterministic.
     * <p>
     * It's recommended to alter this or {@code topP} but not both.
     */
    private Double temperature = 1.0;

    /**
     * Alternative to sampling with temperature, called nucleus sampling, where the
     * model considers the results of the tokens with top_p probability mass.
     * <p>
     * 0.1 means only the tokens comprising the top 10% probability mass are considered.
     * <p>
     * It's recommended to alter this or {@code temperature} but not both.
     */
    private Double topP = 1.0;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    private List<String> stop;

    /**
     * Maximum number of tokens that can be generated in the completion.
     * <p>
     * The total length of input tokens and generated tokens is limited by the model's
     * context length.
     */
    private Integer maxTokens;

    /**
     * Number between -2.0 and 2.0.
     * <p>
     * Positive values penalize new tokens based on whether they appear in the text so
     * far, increasing the model's likelihood to talk about new topics.
     */
    private Double presencePenalty = 0.0;

    /**
     * Number between -2.0 and 2.0.
     * <p>
     * Positive values penalize new tokens based on their existing frequency in the text
     * so far, decreasing the model's likelihood to repeat the same line verbatim.
     */
    private Double frequencyPenalty = 0.0;

    /**
     * Sets the random number seed to use for generation. Setting this to a specific
     * number will make the model generate the same text for the same prompt.
     */
    private Integer seed;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public Double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(Double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public Double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(Double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

}
