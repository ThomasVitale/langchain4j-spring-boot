package io.thomasvitale.langchain4j.docker.compose.service.connection.ollama;

import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

import io.thomasvitale.langchain4j.autoconfigure.models.ollama.OllamaConnectionDetails;

/**
 * {@link DockerComposeConnectionDetailsFactory} to create {@link OllamaConnectionDetails}
 * for an {@code "ollama"} service.
 *
 * @author Thomas Vitale
 */
public class OllamaDockerComposeConnectionDetailsFactory
        extends DockerComposeConnectionDetailsFactory<OllamaConnectionDetails> {

    private static final Integer OLLAMA_PORT = 11434;

    OllamaDockerComposeConnectionDetailsFactory() {
        super("ollama");
    }

    @Override
    protected OllamaConnectionDetails getDockerComposeConnectionDetails(DockerComposeConnectionSource source) {
        return new OllamaDockerComposeConnectionDetails(source.getRunningService());
    }

    /**
     * {@link OllamaConnectionDetails} backed by a Docker {@link RunningService}.
     */
    private static final class OllamaDockerComposeConnectionDetails extends DockerComposeConnectionDetails
            implements OllamaConnectionDetails {

        private final String url;

        private OllamaDockerComposeConnectionDetails(RunningService service) {
            super(service);
            this.url = "http://" + service.host() + ":" + service.ports().get(OLLAMA_PORT);
        }

        @Override
        public String getUrl() {
            return url;
        }

    }

}
