package io.thomasvitale.langchain4j.autoconfigure.models.ollama;

import java.net.URI;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.ollama.OllamaChatModel;
import io.thomasvitale.langchain4j.spring.ollama.OllamaEmbeddingModel;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClient;
import io.thomasvitale.langchain4j.spring.ollama.client.OllamaClientConfig;

/**
 * Auto-configuration for Ollama clients and models.
 *
 * @author Thomas Vitale
 */
@AutoConfiguration(after = RestClientAutoConfiguration.class)
@ConditionalOnClass(OllamaChatModel.class)
@ConditionalOnProperty(prefix = OllamaProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
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

        OllamaClientConfig ollamaClientConfig = OllamaClientConfig.builder()
            .baseUrl(ollamaConnectionDetails.getUrl())
            .connectTimeout(ollamaProperties.getClient().getConnectTimeout())
            .readTimeout(ollamaProperties.getClient().getReadTimeout())
            .sslBundle(ollamaProperties.getClient().getSslBundle())
            .logRequests(ollamaProperties.getClient().isLogRequests())
            .logResponses(ollamaProperties.getClient().isLogResponses())
            .build();

        return new OllamaClient(ollamaClientConfig, restClientBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    OllamaChatModel ollamaChatModel(OllamaClient ollamaClient, OllamaChatProperties ollamaChatProperties) {
        return OllamaChatModel.builder()
            .client(ollamaClient)
            .model(ollamaChatProperties.getModel())
            .format(ollamaChatProperties.getFormat())
            .options(ollamaChatProperties.getOptions())
            .build();
    }

    @Bean
    @ConditionalOnMissingBean
    OllamaEmbeddingModel ollamaEmbeddingModel(OllamaClient ollamaClient,
            OllamaEmbeddingProperties ollamaEmbeddingProperties) {
        return OllamaEmbeddingModel.builder()
            .client(ollamaClient)
            .model(ollamaEmbeddingProperties.getModel())
            .options(ollamaEmbeddingProperties.getOptions())
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
        public URI getUrl() {
            return ollamaProperties.getClient().getBaseUrl();
        }

    }

}
