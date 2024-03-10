package io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate;

import io.weaviate.client.v1.data.replication.model.ConsistencyLevel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.weaviate.WeaviateContainer;

import io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore;

import static dev.langchain4j.internal.Utils.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WeaviateAutoConfiguration}.
 */
@Testcontainers
class WeaviateAutoConfigurationIT {

    @Container
    static WeaviateContainer weaviate = new WeaviateContainer("semitechnologies/weaviate:1.23.10");

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(WeaviateAutoConfiguration.class));

    @Test
    void weaviateEmbeddingStore() {
        var objectClassName = randomUUID();
        var consistencyLevel = ConsistencyLevel.ALL;
        contextRunner.withPropertyValues("langchain4j.vectorstore.weaviate.client.url=http://%s".formatted(weaviate.getHttpHostAddress()))
            .withPropertyValues("langchain4j.vectorstore.weaviate.object-class-name=%s".formatted(objectClassName))
            .withPropertyValues("langchain4j.vectorstore.weaviate.consistency-level=%s".formatted(consistencyLevel))
            .run(context -> {
                WeaviateEmbeddingStore weaviateEmbeddingStore = context.getBean(WeaviateEmbeddingStore.class);
                assertThat(weaviateEmbeddingStore).isNotNull();
            });
    }

    @Test
    void disabled() {
        contextRunner.withPropertyValues("langchain4j.vectorstore.weaviate.enabled=false").run(context -> {
            assertThat(context).doesNotHaveBean(WeaviateEmbeddingStore.class);
        });
    }

}
