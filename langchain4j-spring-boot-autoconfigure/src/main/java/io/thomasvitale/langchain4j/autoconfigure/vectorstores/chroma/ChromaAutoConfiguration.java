package io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma;

import java.net.URI;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;
import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaProperties;
import io.thomasvitale.langchain4j.spring.chroma.ChromaEmbeddingStore;
import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClient;
import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClientConfig;

@AutoConfiguration(after = RestClientAutoConfiguration.class)
@ConditionalOnClass(ChromaEmbeddingStore.class)
@EnableConfigurationProperties({ ChromaProperties.class })
public class ChromaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ChromaConnectionDetails.class)
    PropertiesChromaConnectionDetails propertiesChromaConnectionDetails(ChromaProperties chromaProperties) {
        return new PropertiesChromaConnectionDetails(chromaProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    ChromaClient chromaClient(ChromaConnectionDetails chromaConnectionDetails, ChromaProperties chromaProperties,
            RestClient.Builder restClientBuilder) {

        ChromaClientConfig chromaClientConfig = ChromaClientConfig.builder()
            .url(chromaConnectionDetails.getUrl())
            .connectTimeout(chromaProperties.getClient().getConnectTimeout())
            .readTimeout(chromaProperties.getClient().getReadTimeout())
            .sslBundle(chromaProperties.getClient().getSslBundle())
            .apiToken(chromaConnectionDetails.getApiToken())
            .username(chromaConnectionDetails.getUsername())
            .password(chromaConnectionDetails.getPassword())
            .build();

        return new ChromaClient(chromaClientConfig, restClientBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    ChromaEmbeddingStore chromaEmbeddingStore(ChromaClient chromaClient, ChromaProperties chromaProperties) {
        return ChromaEmbeddingStore.builder()
            .client(chromaClient)
            .collectionName(chromaProperties.getCollectionName())
            .build();
    }

    /**
     * Adapts {@link OllamaProperties} to {@link OllamaConnectionDetails}.
     */
    static class PropertiesChromaConnectionDetails implements ChromaConnectionDetails {

        private final ChromaProperties chromaProperties;

        PropertiesChromaConnectionDetails(ChromaProperties chromaProperties) {
            this.chromaProperties = chromaProperties;
        }

        @Override
        public URI getUrl() {
            return chromaProperties.getClient().getUrl();
        }

        @Override
        public String getApiToken() {
            return chromaProperties.getClient().getApiToken();
        }

        @Override
        public String getUsername() {
            return chromaProperties.getClient().getUsername();
        }

        @Override
        public String getPassword() {
            return chromaProperties.getClient().getPassword();
        }

    }

}
