package io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.thomasvitale.langchain4j.spring.chroma.ChromaEmbeddingStore;
import io.thomasvitale.langchain4j.spring.chroma.api.Collection;
import io.thomasvitale.langchain4j.spring.chroma.client.ChromaClient;

import static dev.langchain4j.internal.Utils.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ChromaAutoConfiguration}.
 *
 * @author Thomas Vitale
 */
@Testcontainers
class ChromaAutoConfigurationIT {

    @Container
    static GenericContainer<?> chroma = new GenericContainer<>("ghcr.io/chroma-core/chroma:0.4.22")
        .withExposedPorts(8000);

    private static String getBaseUrl() {
        return "http://%s:%s".formatted(chroma.getHost(), chroma.getMappedPort(8000));
    }

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(ChromaAutoConfiguration.class, RestClientAutoConfiguration.class));

    @Test
    void chromaClient() {
        var collectionName = randomUUID();
        contextRunner.withPropertyValues("langchain4j.vectorstore.chroma.client.url=%s".formatted(getBaseUrl()))
            .withPropertyValues("langchain4j.vectorstore.chroma.collection-name=%s".formatted(collectionName))
            .run(context -> {
                ChromaClient chromaClient = context.getBean(ChromaClient.class);
                Collection collection = chromaClient.getCollection(collectionName);
                assertThat(collection).isNotNull();
                assertThat(collection.name()).isEqualTo(collectionName);
            });
    }

    @Test
    void chromaEmbeddingStore() {
        var collectionName = randomUUID();
        contextRunner.withPropertyValues("langchain4j.vectorstore.chroma.client.url=%s".formatted(getBaseUrl()))
            .withPropertyValues("langchain4j.vectorstore.chroma.collection-name=%s".formatted(collectionName))
            .run(context -> {
                ChromaEmbeddingStore chromaEmbeddingStore = context.getBean(ChromaEmbeddingStore.class);
                assertThat(chromaEmbeddingStore).isNotNull();
            });
    }

}
