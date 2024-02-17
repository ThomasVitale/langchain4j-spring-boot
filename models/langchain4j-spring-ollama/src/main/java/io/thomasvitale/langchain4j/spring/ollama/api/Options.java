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

/**
 * Ollama options for chat, image, and embedding clients.
 *
 * @see <a href="https://github.com/ollama/ollama/blob/main/api/types.go">Ollama Types</a>
 * @see <a href="https://github.com/ollama/ollama/blob/main/docs/modelfile.md">Ollama
 * Model File</a>
 * <p>
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Options {

// @formatter:off

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
    private Float topP = 0.9F;

    /**
     * Tail free sampling is used to reduce the impact of less probable tokens from the output.
     * A higher value (e.g., 2.0) will reduce the impact more, while a value of 1.0 disables this setting.
     */
    private Float tfsZ = 1.0F;

    /**
     * TypicalP
     */
    private Float typicalP = 1.0F;

    /**
     * Sets how far back for the model to look back to prevent repetition.
     * (0 = disabled, -1 = num_ctx)
     */
    private Integer repeatLastN = 64;

    /**
     * The temperature of the model. Increasing the temperature
     * will make the model answer more creatively.
     */
    private Float temperature = 0.8F;

    /**
     * Sets how strongly to penalize repetitions. A higher value (e.g., 1.5)
     * will penalize repetitions more strongly, while a lower value (e.g., 0.9)
     * will be more lenient.
     */
    private Float repeatPenalty = 1.1F;

    /**
     * PresencePenalty
     */
    private Float presencePenalty = 0.0F;

    /**
     * FrequencyPenalty
     */
    private Float frequencyPenalty = 0.0F;

    /**
     * Enable Mirostat sampling for controlling perplexity.
     * (0 = disabled, 1 = Mirostat, 2 = Mirostat 2.0)
     */
    private Integer mirostat = 0;

    /**
     * Controls the balance between coherence and diversity of the output.
     * A lower value will result in more focused and coherent text.
     */
   private Float mirostatTau = 5.0F;

    /**
     * Influences how quickly the algorithm responds to feedback from the generated text.
     * A lower learning rate will result in slower adjustments, while a higher learning rate
     * will make the algorithm more responsive.
     */
    private Float mirostatEta = 0.1F;

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
     * EmbeddingOnly
     */
    @JsonProperty("embedding_only") private Boolean embeddingOnly = true;

    /**
     * RopeFrequencyBase
     */
    @JsonProperty("rope_frequency_base") private Float ropeFrequencyBase = 10000.0F;

    /**
     * RopeFrequencyScale
     */
    @JsonProperty("rope_frequency_scale") private Float ropeFrequencyScale = 1.0F;

    /**
     * Sets the number of threads to use during computation. By default, Ollama will detect
     * this for optimal performance. It is recommended to set this value to the number
     * of physical CPU cores your system has (as opposed to the logical number of cores).
     */
    @JsonProperty("num_thread") private Integer numThread = 0;

// @formatter:on

    // Constructors

    public Options(Builder builder) {
        this.numKeep = builder.numKeep;
        this.seed = builder.seed;
        this.numPredict = builder.numPredict;
        this.topK = builder.topK;
        this.topP = builder.topP;
        this.tfsZ = builder.tfsZ;
        this.typicalP = builder.typicalP;
        this.repeatLastN = builder.repeatLastN;
        this.temperature = builder.temperature;
        this.repeatPenalty = builder.repeatPenalty;
        this.presencePenalty = builder.presencePenalty;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.mirostat = builder.mirostat;
        this.mirostatTau = builder.mirostatTau;
        this.mirostatEta = builder.mirostatEta;
        this.penalizeNewline = builder.penalizeNewline;
        this.stop = builder.stop;
        this.useNUMA = builder.useNUMA;
        this.numCtx = builder.numCtx;
        this.numBatch = builder.numBatch;
        this.numGQA = builder.numGQA;
        this.numGPU = builder.numGPU;
        this.mainGPU = builder.mainGPU;
        this.lowVRAM = builder.lowVRAM;
        this.f16KV = builder.f16KV;
        this.logitsAll = builder.logitsAll;
        this.vocabOnly = builder.vocabOnly;
        this.useMMap = builder.useMMap;
        this.useMLock = builder.useMLock;
        this.embeddingOnly = builder.embeddingOnly;
        this.ropeFrequencyBase = builder.ropeFrequencyBase;
        this.ropeFrequencyScale = builder.ropeFrequencyScale;
        this.numThread = builder.numThread;
    }

    // Getters

    public Integer getNumKeep() {
        return numKeep;
    }

    public Integer getSeed() {
        return seed;
    }

    public Integer getNumPredict() {
        return numPredict;
    }

    public Integer getTopK() {
        return topK;
    }

    public Float getTopP() {
        return topP;
    }

    public Float getTfsZ() {
        return tfsZ;
    }

    public Float getTypicalP() {
        return typicalP;
    }

    public Integer getRepeatLastN() {
        return repeatLastN;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Float getRepeatPenalty() {
        return repeatPenalty;
    }

    public Float getPresencePenalty() {
        return presencePenalty;
    }

    public Float getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public Integer getMirostat() {
        return mirostat;
    }

    public Float getMirostatTau() {
        return mirostatTau;
    }

    public Float getMirostatEta() {
        return mirostatEta;
    }

    public Boolean getPenalizeNewline() {
        return penalizeNewline;
    }

    public List<String> getStop() {
        return stop;
    }

    public Boolean getUseNUMA() {
        return useNUMA;
    }

    public Integer getNumCtx() {
        return numCtx;
    }

    public Integer getNumBatch() {
        return numBatch;
    }

    public Integer getNumGQA() {
        return numGQA;
    }

    public Integer getNumGPU() {
        return numGPU;
    }

    public Integer getMainGPU() {
        return mainGPU;
    }

    public Boolean getLowVRAM() {
        return lowVRAM;
    }

    public Boolean getF16KV() {
        return f16KV;
    }

    public Boolean getLogitsAll() {
        return logitsAll;
    }

    public Boolean getVocabOnly() {
        return vocabOnly;
    }

    public Boolean getUseMMap() {
        return useMMap;
    }

    public Boolean getUseMLock() {
        return useMLock;
    }

    public Boolean getEmbeddingOnly() {
        return embeddingOnly;
    }

    public Float getRopeFrequencyBase() {
        return ropeFrequencyBase;
    }

    public Float getRopeFrequencyScale() {
        return ropeFrequencyScale;
    }

    public Integer getNumThread() {
        return numThread;
    }

    // Builders

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Integer numKeep;

        private Integer seed;

        private Integer numPredict;

        private Integer topK;

        private Float topP;

        private Float tfsZ;

        private Float typicalP;

        private Integer repeatLastN;

        private Float temperature;

        private Float repeatPenalty;

        private Float presencePenalty;

        private Float frequencyPenalty;

        private Integer mirostat;

        private Float mirostatTau;

        private Float mirostatEta;

        private Boolean penalizeNewline;

        private List<String> stop;

        private Boolean useNUMA;

        private Integer numCtx;

        private Integer numBatch;

        private Integer numGQA;

        private Integer numGPU;

        private Integer mainGPU;

        private Boolean lowVRAM;

        private Boolean f16KV;

        private Boolean logitsAll;

        private Boolean vocabOnly;

        private Boolean useMMap;

        private Boolean useMLock;

        private Boolean embeddingOnly;

        private Float ropeFrequencyBase;

        private Float ropeFrequencyScale;

        private Integer numThread;

        private Builder() {
        }

        public Builder withNumKeep(Integer numKeep) {
            this.numKeep = numKeep;
            return this;
        }

        public Builder withSeed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder withNumPredict(Integer numPredict) {
            this.numPredict = numPredict;
            return this;
        }

        public Builder withTopK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder withTopP(Float topP) {
            this.topP = topP;
            return this;
        }

        public Builder withTfsZ(Float tfsZ) {
            this.tfsZ = tfsZ;
            return this;
        }

        public Builder withTypicalP(Float typicalP) {
            this.typicalP = typicalP;
            return this;
        }

        public Builder withRepeatLastN(Integer repeatLastN) {
            this.repeatLastN = repeatLastN;
            return this;
        }

        public Builder withTemperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder withRepeatPenalty(Float repeatPenalty) {
            this.repeatPenalty = repeatPenalty;
            return this;
        }

        public Builder withPresencePenalty(Float presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder withFrequencyPenalty(Float frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder withMirostat(Integer mirostat) {
            this.mirostat = mirostat;
            return this;
        }

        public Builder withMirostatTau(Float mirostatTau) {
            this.mirostatTau = mirostatTau;
            return this;
        }

        public Builder withMirostatEta(Float mirostatEta) {
            this.mirostatEta = mirostatEta;
            return this;
        }

        public Builder withPenalizeNewline(Boolean penalizeNewline) {
            this.penalizeNewline = penalizeNewline;
            return this;
        }

        public Builder withStop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        public Builder withUseNUMA(Boolean useNUMA) {
            this.useNUMA = useNUMA;
            return this;
        }

        public Builder withNumCtx(Integer numCtx) {
            this.numCtx = numCtx;
            return this;
        }

        public Builder withNumBatch(Integer numBatch) {
            this.numBatch = numBatch;
            return this;
        }

        public Builder withNumGQA(Integer numGQA) {
            this.numGQA = numGQA;
            return this;
        }

        public Builder withNumGPU(Integer numGPU) {
            this.numGPU = numGPU;
            return this;
        }

        public Builder withMainGPU(Integer mainGPU) {
            this.mainGPU = mainGPU;
            return this;
        }

        public Builder withLowVRAM(Boolean lowVRAM) {
            this.lowVRAM = lowVRAM;
            return this;
        }

        public Builder withF16KV(Boolean f16KV) {
            this.f16KV = f16KV;
            return this;
        }

        public Builder withLogitsAll(Boolean logitsAll) {
            this.logitsAll = logitsAll;
            return this;
        }

        public Builder withVocabOnly(Boolean vocabOnly) {
            this.vocabOnly = vocabOnly;
            return this;
        }

        public Builder withUseMMap(Boolean useMMap) {
            this.useMMap = useMMap;
            return this;
        }

        public Builder withUseMLock(Boolean useMLock) {
            this.useMLock = useMLock;
            return this;
        }

        public Builder withEmbeddingOnly(Boolean embeddingOnly) {
            this.embeddingOnly = embeddingOnly;
            return this;
        }

        public Builder withRopeFrequencyBase(Float ropeFrequencyBase) {
            this.ropeFrequencyBase = ropeFrequencyBase;
            return this;
        }

        public Builder withRopeFrequencyScale(Float ropeFrequencyScale) {
            this.ropeFrequencyScale = ropeFrequencyScale;
            return this;
        }

        public Builder withNumThread(Integer numThread) {
            this.numThread = numThread;
            return this;
        }

        public Options build() {
            return new Options(this);
        }

    }

    // Utilities

    public Map<String, Object> toMap() {
        try {
            var json = new ObjectMapper().writeValueAsString(this);
            return new ObjectMapper().readValue(json, new TypeReference<>() {
            });
        }
        catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
