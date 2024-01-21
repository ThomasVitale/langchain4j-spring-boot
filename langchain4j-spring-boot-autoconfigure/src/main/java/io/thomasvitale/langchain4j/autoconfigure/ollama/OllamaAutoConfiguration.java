package io.thomasvitale.langchain4j.autoconfigure.ollama;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for Ollama clients and models.
 *
 * @author Thomas Vitale
 */
@AutoConfiguration
@ConditionalOnClass(OllamaChatModel.class)
@EnableConfigurationProperties({ OllamaProperties.class, OllamaChatProperties.class, OllamaEmbeddingProperties.class })
public class OllamaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OllamaChatModel.class)
    OllamaChatModel ollamaChatModel(OllamaProperties ollamaProperties, OllamaChatProperties ollamaChatProperties) {
        return OllamaChatModel.builder()
            .baseUrl(ollamaProperties.getBaseUrl().toString())
            .modelName(ollamaChatProperties.getModel())
            .temperature(ollamaChatProperties.getTemperature())
            .topK(ollamaChatProperties.getTopK())
            .topP(ollamaChatProperties.getTopP())
            .repeatPenalty(ollamaChatProperties.getRepeatPenalty())
            .seed(ollamaChatProperties.getSeed())
            .numPredict(ollamaChatProperties.getNumPredict())
            .stop(ollamaChatProperties.getStop())
            .timeout(ollamaProperties.getTimeout())
            .maxRetries(ollamaProperties.getMaxRetries())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(OllamaStreamingChatModel.class)
    OllamaStreamingChatModel ollamaStreamingChatModel(OllamaProperties ollamaProperties,
            OllamaChatProperties ollamaChatProperties) {
        return OllamaStreamingChatModel.builder()
            .baseUrl(ollamaProperties.getBaseUrl().toString())
            .modelName(ollamaChatProperties.getModel())
            .temperature(ollamaChatProperties.getTemperature())
            .topK(ollamaChatProperties.getTopK())
            .topP(ollamaChatProperties.getTopP())
            .repeatPenalty(ollamaChatProperties.getRepeatPenalty())
            .seed(ollamaChatProperties.getSeed())
            .numPredict(ollamaChatProperties.getNumPredict())
            .stop(ollamaChatProperties.getStop())
            .timeout(ollamaProperties.getTimeout())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(OllamaEmbeddingModel.class)
    OllamaEmbeddingModel ollamaEmbeddingModel(OllamaProperties ollamaProperties,
            OllamaEmbeddingProperties ollamaEmbeddingProperties) {
        return OllamaEmbeddingModel.builder()
            .baseUrl(ollamaProperties.getBaseUrl().toString())
            .modelName(ollamaEmbeddingProperties.getModel())
            .timeout(ollamaProperties.getTimeout())
            .maxRetries(ollamaProperties.getMaxRetries())
            .build();
    }

}
