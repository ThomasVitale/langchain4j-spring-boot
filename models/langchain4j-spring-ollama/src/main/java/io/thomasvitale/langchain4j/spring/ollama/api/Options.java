package io.thomasvitale.langchain4j.spring.ollama.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.thomasvitale.langchain4j.spring.core.json.jackson.LangChain4jJacksonProvider;

/**
 * Ollama options for chat, image, and embedding clients.
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/modelfile.md">Ollama Model File</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Options {

    // Options API from https://github.com/ollama/ollama/blob/main/api/types.go

    /**
     * NumKeep
     */
    private Integer numKeep = 0;

    /**
     * Sets the random number seed to use for generation. Setting this to a
     * specific number will make the model generate the same text for the same prompt.
     */
    private Integer seed = -1;

    /**
     * Maximum number of tokens to predict when generating text.
     * (-1 = infinite generation, -2 = fill context)
     */
    private Integer numPredict = 128;

    /**
     * Reduces the probability of generating nonsense.
     * A higher value (e.g. 100) will give more diverse answers,
     * while a lower value (e.g. 10) will be more conservative.
     */
    private Integer topK = 40;

    /**
     * Works together with top-k. A higher value (e.g., 0.95) will lead to more diverse text,
     * while a lower value (e.g., 0.5) will generate more focused and conservative text.
     */
    private Double topP = 0.9;

    /**
     * Tail free sampling is used to reduce the impact of less probable tokens from the output.
     * A higher value (e.g., 2.0) will reduce the impact more, while a value of 1.0 disables this setting.
     */
    private Double tfsZ = 1.0;

    /**
     * TypicalP
     */
    private Double typicalP = 1.0;

    /**
     * Sets how far back for the model to look back to prevent repetition.
     * (0 = disabled, -1 = num_ctx)
     */
    private Integer repeatLastN = 64;

    /**
     * The temperature of the model. Increasing the temperature
     * will make the model answer more creatively.
     */
    private Double temperature = 0.8;

    /**
     * Sets how strongly to penalize repetitions. A higher value (e.g., 1.5)
     * will penalize repetitions more strongly, while a lower value (e.g., 0.9)
     * will be more lenient.
     */
    private Double repeatPenalty = 1.1;

    /**
     * PresencePenalty
     */
    private Double presencePenalty = 0.0;

    /**
     * FrequencyPenalty
     */
    private Double frequencyPenalty = 0.0;

    /**
     * Enable Mirostat sampling for controlling perplexity.
     * (0 = disabled, 1 = Mirostat, 2 = Mirostat 2.0)
     */
    private Integer mirostat = 0;

    /**
     * Controls the balance between coherence and diversity of the output.
     * A lower value will result in more focused and coherent text.
     */
   private Double mirostatTau = 5.0;

    /**
     * Influences how quickly the algorithm responds to feedback from the generated text.
     * A lower learning rate will result in slower adjustments, while a higher learning rate
     * will make the algorithm more responsive.
     */
    private Double mirostatEta = 0.1;

    /**
     * PenalizeNewline
     */
    private Boolean penalizeNewline = true;

    /**
     * Sets the stop sequences to use. When this pattern is encountered the LLM
     * will stop generating text and return. Multiple stop patterns may be set
     * by specifying multiple separate stop parameters in a modelfile.
     */
    private List<String> stop;

    // Runner API from https://github.com/ollama/ollama/blob/main/api/types.go

    /**
     * UseNUMA
     */
    @JsonProperty("numa") private Boolean useNUMA = false;

    /**
     * Sets the size of the context window used to generate the next token.
     */
    @JsonProperty("num_ctx") private Integer numCtx = 2048;

    /**
     * NumBatch
     */
    @JsonProperty("num_batch") private Integer numBatch = 512;

    /**
     * The number of GQA groups in the transformer layer.
     * Required for some models, for example it is 8 for llama2:70b
     */
    @JsonProperty("num_gqa") private Integer numGQA = 1;

    /**
     * The number of layers to send to the GPU(s).
     * On macOS it defaults to 1 to enable metal support, 0 to disable.
     * When -1, NumGPU will be set dynamically.
     */
    @JsonProperty("num_gpu") private Integer numGPU = -1;

    /**
     * MainGPU
     */
    @JsonProperty("main_gpu") private Integer mainGPU;

    /**
     * LowVRAM
     */
    @JsonProperty("low_vram") private Boolean lowVRAM = false;

    /**
     * F16KV
     */
    @JsonProperty("f16_kv") private Boolean f16KV = true;

    /**
     * LogitsAll
     */
    @JsonProperty("logits_all") private Boolean logitsAll;

    /**
     * VocabOnly
     */
    @JsonProperty("vocab_only") private Boolean vocabOnly;

    /**
     * UseMMap
     */
    @JsonProperty("use_mmap") private Boolean useMMap = true;

    /**
     * UseMLock
     */
    @JsonProperty("use_mlock") private Boolean useMLock = false;

    /**
     * RopeFrequencyBase
     */
    @JsonProperty("rope_frequency_base") private Double ropeFrequencyBase = 10000.0;

    /**
     * RopeFrequencyScale
     */
    @JsonProperty("rope_frequency_scale") private Double ropeFrequencyScale = 1.0;

    /**
     * Sets the number of threads to use during computation. By default, Ollama will detect
     * this for optimal performance. It is recommended to set this value to the number
     * of physical CPU cores your system has (as opposed to the logical number of cores).
     */
    @JsonProperty("num_thread") private Integer numThread = 0;

    // Builders

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Options options = new Options();

        private Builder() {}

        public Builder numKeep(Integer numKeep) {
            this.options.numKeep = numKeep;
            return this;
        }

        public Builder seed(Integer seed) {
            this.options.seed = seed;
            return this;
        }

        public Builder numPredict(Integer numPredict) {
            this.options.numPredict = numPredict;
            return this;
        }

        public Builder topK(Integer topK) {
            this.options.topK = topK;
            return this;
        }

        public Builder topP(Double topP) {
            this.options.topP = topP;
            return this;
        }

        public Builder tfsZ(Double tfsZ) {
            this.options.tfsZ = tfsZ;
            return this;
        }

        public Builder typicalP(Double typicalP) {
            this.options.typicalP = typicalP;
            return this;
        }

        public Builder repeatLastN(Integer repeatLastN) {
            this.options.repeatLastN = repeatLastN;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.options.temperature = temperature;
            return this;
        }

        public Builder repeatPenalty(Double repeatPenalty) {
            this.options.repeatPenalty = repeatPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.options.presencePenalty = presencePenalty;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.options.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder mirostat(Integer mirostat) {
            this.options.mirostat = mirostat;
            return this;
        }

        public Builder mirostatTau(Double mirostatTau) {
            this.options.mirostatTau = mirostatTau;
            return this;
        }

        public Builder mirostatEta(Double mirostatEta) {
            this.options.mirostatEta = mirostatEta;
            return this;
        }

        public Builder penalizeNewline(Boolean penalizeNewline) {
            this.options.penalizeNewline = penalizeNewline;
            return this;
        }

        public Builder stop(List<String> stop) {
            this.options.stop = stop;
            return this;
        }

        public Builder useNUMA(Boolean useNUMA) {
            this.options.useNUMA = useNUMA;
            return this;
        }

        public Builder numCtx(Integer numCtx) {
            this.options.numCtx = numCtx;
            return this;
        }

        public Builder numBatch(Integer numBatch) {
            this.options.numBatch = numBatch;
            return this;
        }

        public Builder numGQA(Integer numGQA) {
            this.options.numGQA = numGQA;
            return this;
        }

        public Builder numGPU(Integer numGPU) {
            this.options.numGPU = numGPU;
            return this;
        }

        public Builder mainGPU(Integer mainGPU) {
            this.options.mainGPU = mainGPU;
            return this;
        }

        public Builder lowVRAM(Boolean lowVRAM) {
            this.options.lowVRAM = lowVRAM;
            return this;
        }

        public Builder f16KV(Boolean f16KV) {
            this.options.f16KV = f16KV;
            return this;
        }

        public Builder logitsAll(Boolean logitsAll) {
            this.options.logitsAll = logitsAll;
            return this;
        }

        public Builder vocabOnly(Boolean vocabOnly) {
            this.options.vocabOnly = vocabOnly;
            return this;
        }

        public Builder useMMap(Boolean useMMap) {
            this.options.useMMap = useMMap;
            return this;
        }

        public Builder useMLock(Boolean useMLock) {
            this.options.useMLock = useMLock;
            return this;
        }

        public Builder ropeFrequencyBase(Double ropeFrequencyBase) {
            this.options.ropeFrequencyBase = ropeFrequencyBase;
            return this;
        }

        public Builder ropeFrequencyScale(Double ropeFrequencyScale) {
            this.options.ropeFrequencyScale = ropeFrequencyScale;
            return this;
        }

        public Builder numThread(Integer numThread) {
            this.options.numThread = numThread;
            return this;
        }

        public Options build() {
            return options;
        }
    }

    // Getters and Setters

    public Integer getNumKeep() {
        return numKeep;
    }

    public void setNumKeep(Integer numKeep) {
        this.numKeep = numKeep;
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

    public Double getTfsZ() {
        return tfsZ;
    }

    public void setTfsZ(Double tfsZ) {
        this.tfsZ = tfsZ;
    }

    public Double getTypicalP() {
        return typicalP;
    }

    public void setTypicalP(Double typicalP) {
        this.typicalP = typicalP;
    }

    public Integer getRepeatLastN() {
        return repeatLastN;
    }

    public void setRepeatLastN(Integer repeatLastN) {
        this.repeatLastN = repeatLastN;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getRepeatPenalty() {
        return repeatPenalty;
    }

    public void setRepeatPenalty(Double repeatPenalty) {
        this.repeatPenalty = repeatPenalty;
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

    public Integer getMirostat() {
        return mirostat;
    }

    public void setMirostat(Integer mirostat) {
        this.mirostat = mirostat;
    }

    public Double getMirostatTau() {
        return mirostatTau;
    }

    public void setMirostatTau(Double mirostatTau) {
        this.mirostatTau = mirostatTau;
    }

    public Double getMirostatEta() {
        return mirostatEta;
    }

    public void setMirostatEta(Double mirostatEta) {
        this.mirostatEta = mirostatEta;
    }

    public Boolean getPenalizeNewline() {
        return penalizeNewline;
    }

    public void setPenalizeNewline(Boolean penalizeNewline) {
        this.penalizeNewline = penalizeNewline;
    }

    public List<String> getStop() {
        return stop;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public Boolean getUseNUMA() {
        return useNUMA;
    }

    public void setUseNUMA(Boolean useNUMA) {
        this.useNUMA = useNUMA;
    }

    public Integer getNumCtx() {
        return numCtx;
    }

    public void setNumCtx(Integer numCtx) {
        this.numCtx = numCtx;
    }

    public Integer getNumBatch() {
        return numBatch;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public Integer getNumGQA() {
        return numGQA;
    }

    public void setNumGQA(Integer numGQA) {
        this.numGQA = numGQA;
    }

    public Integer getNumGPU() {
        return numGPU;
    }

    public void setNumGPU(Integer numGPU) {
        this.numGPU = numGPU;
    }

    public Integer getMainGPU() {
        return mainGPU;
    }

    public void setMainGPU(Integer mainGPU) {
        this.mainGPU = mainGPU;
    }

    public Boolean getLowVRAM() {
        return lowVRAM;
    }

    public void setLowVRAM(Boolean lowVRAM) {
        this.lowVRAM = lowVRAM;
    }

    public Boolean getF16KV() {
        return f16KV;
    }

    public void setF16KV(Boolean f16KV) {
        this.f16KV = f16KV;
    }

    public Boolean getLogitsAll() {
        return logitsAll;
    }

    public void setLogitsAll(Boolean logitsAll) {
        this.logitsAll = logitsAll;
    }

    public Boolean getVocabOnly() {
        return vocabOnly;
    }

    public void setVocabOnly(Boolean vocabOnly) {
        this.vocabOnly = vocabOnly;
    }

    public Boolean getUseMMap() {
        return useMMap;
    }

    public void setUseMMap(Boolean useMMap) {
        this.useMMap = useMMap;
    }

    public Boolean getUseMLock() {
        return useMLock;
    }

    public void setUseMLock(Boolean useMLock) {
        this.useMLock = useMLock;
    }

    public Double getRopeFrequencyBase() {
        return ropeFrequencyBase;
    }

    public void setRopeFrequencyBase(Double ropeFrequencyBase) {
        this.ropeFrequencyBase = ropeFrequencyBase;
    }

    public Double getRopeFrequencyScale() {
        return ropeFrequencyScale;
    }

    public void setRopeFrequencyScale(Double ropeFrequencyScale) {
        this.ropeFrequencyScale = ropeFrequencyScale;
    }

    public Integer getNumThread() {
        return numThread;
    }

    public void setNumThread(Integer numThread) {
        this.numThread = numThread;
    }

    // Utilities

    public Map<String, Object> toMap() {
        try {
            var json = new ObjectMapper().writeValueAsString(this);
            return LangChain4jJacksonProvider.getObjectMapper().readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
