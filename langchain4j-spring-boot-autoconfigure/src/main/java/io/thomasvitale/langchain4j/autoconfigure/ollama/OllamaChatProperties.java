package io.thomasvitale.langchain4j.autoconfigure.ollama;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Configuration properties for Ollama chat.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OllamaChatProperties.CONFIG_PREFIX)
public class OllamaChatProperties {

    public static final String CONFIG_PREFIX = "langchain4j.ollama.chat";

    /**
     * The name of the model to use.
     */
    private String model = "llama2";

    /**
     * The temperature of the model.
     * Increasing the temperature will make the model answer more creatively.
     */
    private Double temperature = 0.8;

    /**
     * Reduces the probability of generating nonsense. A higher value (e.g. 100)
     * will give more diverse answers, while a lower value (e.g. 10) will be more conservative.
     */
    private Integer topK = 40;

    /**
     * Works together with top-k. A higher value (e.g., 0.95) will lead to more diverse text,
     * while a lower value (e.g., 0.5) will generate more focused and conservative text.
     */
    private Double topP = 0.9;

    /**
     * Sets how strongly to penalize repetitions. A higher value (e.g., 1.5) will penalize
     * repetitions more strongly, while a lower value (e.g., 0.9) will be more lenient.
     */
    private Double repeatPenalty = 1.1;

    /**
     * Sets the random number seed to use for generation. Setting this to a specific number
     * will make the model generate the same text for the same prompt.
     */
    private Integer seed = 0;

    /**
     * Maximum number of tokens to predict when generating text.
     * (-1 = infinite generation, -2 = fill context)
     */
    private Integer numPredict = 128;

    /**
     * Sets the stop sequences to use. When this pattern is encountered
     * the LLM will stop generating text and return. Multiple stop patterns
     * may be set by specifying multiple separate stop parameters in a list.
     */
    private List<String> stop;

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

    public Integer getTopK() {
        return topK;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Double getRepeatPenalty() {
        return repeatPenalty;
    }

    public void setRepeatPenalty(Double repeatPenalty) {
        this.repeatPenalty = repeatPenalty;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getNumPredict() {
        return numPredict;
    }

    public void setNumPredict(Integer numPredict) {
        this.numPredict = numPredict;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

}
