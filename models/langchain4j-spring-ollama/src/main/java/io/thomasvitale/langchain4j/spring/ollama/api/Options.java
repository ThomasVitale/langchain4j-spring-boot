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

    // Setters

    public void setNumKeep(Integer numKeep) {
        this.numKeep = numKeep;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public void setNumPredict(Integer numPredict) {
        this.numPredict = numPredict;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }

    public void setTopP(Float topP) {
        this.topP = topP;
    }

    public void setTfsZ(Float tfsZ) {
        this.tfsZ = tfsZ;
    }

    public void setTypicalP(Float typicalP) {
        this.typicalP = typicalP;
    }

    public void setRepeatLastN(Integer repeatLastN) {
        this.repeatLastN = repeatLastN;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public void setRepeatPenalty(Float repeatPenalty) {
        this.repeatPenalty = repeatPenalty;
    }

    public void setPresencePenalty(Float presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public void setFrequencyPenalty(Float frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public void setMirostat(Integer mirostat) {
        this.mirostat = mirostat;
    }

    public void setMirostatTau(Float mirostatTau) {
        this.mirostatTau = mirostatTau;
    }

    public void setMirostatEta(Float mirostatEta) {
        this.mirostatEta = mirostatEta;
    }

    public void setPenalizeNewline(Boolean penalizeNewline) {
        this.penalizeNewline = penalizeNewline;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public void setUseNUMA(Boolean useNUMA) {
        this.useNUMA = useNUMA;
    }

    public void setNumCtx(Integer numCtx) {
        this.numCtx = numCtx;
    }

    public void setNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
    }

    public void setNumGQA(Integer numGQA) {
        this.numGQA = numGQA;
    }

    public void setNumGPU(Integer numGPU) {
        this.numGPU = numGPU;
    }

    public void setMainGPU(Integer mainGPU) {
        this.mainGPU = mainGPU;
    }

    public void setLowVRAM(Boolean lowVRAM) {
        this.lowVRAM = lowVRAM;
    }

    public void setF16KV(Boolean f16KV) {
        this.f16KV = f16KV;
    }

    public void setLogitsAll(Boolean logitsAll) {
        this.logitsAll = logitsAll;
    }

    public void setVocabOnly(Boolean vocabOnly) {
        this.vocabOnly = vocabOnly;
    }

    public void setUseMMap(Boolean useMMap) {
        this.useMMap = useMMap;
    }

    public void setUseMLock(Boolean useMLock) {
        this.useMLock = useMLock;
    }

    public void setEmbeddingOnly(Boolean embeddingOnly) {
        this.embeddingOnly = embeddingOnly;
    }

    public void setRopeFrequencyBase(Float ropeFrequencyBase) {
        this.ropeFrequencyBase = ropeFrequencyBase;
    }

    public void setRopeFrequencyScale(Float ropeFrequencyScale) {
        this.ropeFrequencyScale = ropeFrequencyScale;
    }

    public void setNumThread(Integer numThread) {
        this.numThread = numThread;
    }

    // Builders

    public static Options create() {
        return new Options();
    }

    public Options withNumKeep(Integer numKeep) {
        this.numKeep = numKeep;
        return this;
    }

    public Options withSeed(Integer seed) {
        this.seed = seed;
        return this;
    }

    public Options withNumPredict(Integer numPredict) {
        this.numPredict = numPredict;
        return this;
    }

    public Options withTopK(Integer topK) {
        this.topK = topK;
        return this;
    }

    public Options withTopP(Float topP) {
        this.topP = topP;
        return this;
    }

    public Options withTfsZ(Float tfsZ) {
        this.tfsZ = tfsZ;
        return this;
    }

    public Options withTypicalP(Float typicalP) {
        this.typicalP = typicalP;
        return this;
    }

    public Options withRepeatLastN(Integer repeatLastN) {
        this.repeatLastN = repeatLastN;
        return this;
    }

    public Options withTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public Options withRepeatPenalty(Float repeatPenalty) {
        this.repeatPenalty = repeatPenalty;
        return this;
    }

    public Options withPresencePenalty(Float presencePenalty) {
        this.presencePenalty = presencePenalty;
        return this;
    }

    public Options withFrequencyPenalty(Float frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }

    public Options withMirostat(Integer mirostat) {
        this.mirostat = mirostat;
        return this;
    }

    public Options withMirostatTau(Float mirostatTau) {
        this.mirostatTau = mirostatTau;
        return this;
    }

    public Options withMirostatEta(Float mirostatEta) {
        this.mirostatEta = mirostatEta;
        return this;
    }

    public Options withPenalizeNewline(Boolean penalizeNewline) {
        this.penalizeNewline = penalizeNewline;
        return this;
    }

    public Options withStop(List<String> stop) {
        this.stop = stop;
        return this;
    }

    public Options withUseNUMA(Boolean useNUMA) {
        this.useNUMA = useNUMA;
        return this;
    }

    public Options withNumCtx(Integer numCtx) {
        this.numCtx = numCtx;
        return this;
    }

    public Options withNumBatch(Integer numBatch) {
        this.numBatch = numBatch;
        return this;
    }

    public Options withNumGQA(Integer numGQA) {
        this.numGQA = numGQA;
        return this;
    }

    public Options withNumGPU(Integer numGPU) {
        this.numGPU = numGPU;
        return this;
    }

    public Options withMainGPU(Integer mainGPU) {
        this.mainGPU = mainGPU;
        return this;
    }

    public Options withLowVRAM(Boolean lowVRAM) {
        this.lowVRAM = lowVRAM;
        return this;
    }

    public Options withF16KV(Boolean f16KV) {
        this.f16KV = f16KV;
        return this;
    }

    public Options withLogitsAll(Boolean logitsAll) {
        this.logitsAll = logitsAll;
        return this;
    }

    public Options withVocabOnly(Boolean vocabOnly) {
        this.vocabOnly = vocabOnly;
        return this;
    }

    public Options withUseMMap(Boolean useMMap) {
        this.useMMap = useMMap;
        return this;
    }

    public Options withUseMLock(Boolean useMLock) {
        this.useMLock = useMLock;
        return this;
    }

    public Options withEmbeddingOnly(Boolean embeddingOnly) {
        this.embeddingOnly = embeddingOnly;
        return this;
    }

    public Options withRopeFrequencyBase(Float ropeFrequencyBase) {
        this.ropeFrequencyBase = ropeFrequencyBase;
        return this;
    }

    public Options withRopeFrequencyScale(Float ropeFrequencyScale) {
        this.ropeFrequencyScale = ropeFrequencyScale;
        return this;
    }

    public Options withNumThread(Integer numThread) {
        this.numThread = numThread;
        return this;
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
