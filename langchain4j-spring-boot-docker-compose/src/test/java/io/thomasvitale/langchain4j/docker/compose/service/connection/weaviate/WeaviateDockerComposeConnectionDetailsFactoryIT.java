package io.thomasvitale.langchain4j.docker.compose.service.connection.weaviate;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate.WeaviateConnectionDetails;
import io.thomasvitale.langchain4j.docker.compose.service.connection.DockerComposeIntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link WeaviateDockerComposeConnectionDetailsFactory}.
 */
class WeaviateDockerComposeConnectionDetailsFactoryIT extends DockerComposeIntegrationTestSupport {

    private static final DockerImageName weaviateDockerImage = DockerImageName.parse("semitechnologies/weaviate");

    WeaviateDockerComposeConnectionDetailsFactoryIT() {
        super("weaviate-compose.yml", weaviateDockerImage);
    }

    @Test
    void createConnectionDetails() {
        WeaviateConnectionDetails connectionDetails = run(WeaviateConnectionDetails.class);
        assertThat(connectionDetails.getUrl().getHost()).isEqualTo("127.0.0.1");
    }

}
