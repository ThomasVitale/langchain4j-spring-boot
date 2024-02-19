package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.ollama.OllamaChatModel;
import io.thomasvitale.langchain4j.spring.ollama.OllamaEmbeddingModel;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;

/**
 * Auto-configuration for Ollama clients and models.
 *
 * @author Thomas Vitale
 */
@AutoConfiguration(after = RestClientAutoConfiguration.class)
@ConditionalOnClass(OllamaChatModel.class)
@EnableConfigurationProperties({ OllamaProperties.class, OllamaChatProperties.class, OllamaEmbeddingProperties.class })
public class OllamaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OllamaConnectionDetails.class)
    PropertiesOllamaConnectionDetails propertiesOllamaConnectionDetails(OllamaProperties ollamaProperties) {
        return new PropertiesOllamaConnectionDetails(ollamaProperties);
    }

    @Bean
    @ConditionalOnMissingBean(OllamaClient.class)
    OllamaClient ollamaClient(OllamaConnectionDetails ollamaConnectionDetails, OllamaProperties ollamaProperties,
            RestClient.Builder restClientBuilder) {
        return new OllamaClient(ollamaConnectionDetails.getUrl(), restClientBuilder, ollamaProperties.getClient());
    }

    @Bean
    @ConditionalOnMissingBean(OllamaChatModel.class)
    OllamaChatModel ollamaChatModel(OllamaClient ollamaClient, OllamaChatProperties ollamaChatProperties) {
        return OllamaChatModel.builder()
            .withClient(ollamaClient)
            .withModel(ollamaChatProperties.getModel())
            .withFormat(ollamaChatProperties.getFormat())
            .withOptions(ollamaChatProperties.getOptions())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean(OllamaEmbeddingModel.class)
    OllamaEmbeddingModel ollamaEmbeddingModel(OllamaClient ollamaClient,
            OllamaEmbeddingProperties ollamaEmbeddingProperties) {
        return OllamaEmbeddingModel.builder()
            .withClient(ollamaClient)
            .withModel(ollamaEmbeddingProperties.getModel())
            .withOptions(ollamaEmbeddingProperties.getOptions())
            .build();
    }

    /**
     * Adapts {@link OllamaProperties} to {@link OllamaConnectionDetails}.
     */
    static class PropertiesOllamaConnectionDetails implements OllamaConnectionDetails {

        private final OllamaProperties ollamaProperties;

        PropertiesOllamaConnectionDetails(OllamaProperties ollamaProperties) {
            this.ollamaProperties = ollamaProperties;
        }

        @Override
        public String getUrl() {
            return ollamaProperties.getBaseUrl().toString();
        }

    }

}
