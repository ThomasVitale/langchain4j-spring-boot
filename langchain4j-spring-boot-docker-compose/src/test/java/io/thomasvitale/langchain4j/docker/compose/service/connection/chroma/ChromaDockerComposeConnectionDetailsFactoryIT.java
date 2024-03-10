package io.thomasvitale.langchain4j.docker.compose.service.connection.chroma;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;
import io.thomasvitale.langchain4j.docker.compose.service.connection.DockerComposeIntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ChromaDockerComposeConnectionDetailsFactory}.
 */
class ChromaDockerComposeConnectionDetailsFactoryIT extends DockerComposeIntegrationTestSupport {

    private static final DockerImageName chromaImageName = DockerImageName.parse("ghcr.io/chroma-core/chroma");

    ChromaDockerComposeConnectionDetailsFactoryIT() {
        super("chroma-compose.yml", chromaImageName);
    }

    @Test
    void createConnectionDetails() {
        ChromaConnectionDetails connectionDetails = run(ChromaConnectionDetails.class);
        assertThat(connectionDetails.getUrl().getHost()).isEqualTo("127.0.0.1");
    }

}
