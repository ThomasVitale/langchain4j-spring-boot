package io.thomasvitale.langchain4j.docker.compose.service.connection.ollama;

import org.junit.jupiter.api.Test;
import org.testcontainers.utility.DockerImageName;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;
import io.thomasvitale.langchain4j.docker.compose.service.connection.DockerComposeIntegrationTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OllamaDockerComposeConnectionDetailsFactory}.
 */
class OllamaDockerComposeConnectionDetailsFactoryIT extends DockerComposeIntegrationTestSupport {

    private static final DockerImageName ollamaImageName = DockerImageName
        .parse("ghcr.io/thomasvitale/ollama-orca-mini");

    OllamaDockerComposeConnectionDetailsFactoryIT() {
        super("ollama-compose.yml", ollamaImageName);
    }

    @Test
    void createConnectionDetails() {
        OllamaConnectionDetails connectionDetails = run(OllamaConnectionDetails.class);
        assertThat(connectionDetails.getUrl().getHost()).isEqualTo("127.0.0.1");
    }

}
