package io.thomasvitale.langchain4j.docker.compose.service.connection.chroma;

import java.net.URI;

import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.chroma.ChromaConnectionDetails;

/**
 * {@link DockerComposeConnectionDetailsFactory} to create {@link ChromaConnectionDetails}
 * for a {@code "chroma"} service.
 */
public class ChromaDockerComposeConnectionDetailsFactory
        extends DockerComposeConnectionDetailsFactory<ChromaConnectionDetails> {

    private static final String[] CHROMA_CONTAINER_NAMES = { "ghcr.io/chroma-core/chroma" };

    private static final Integer CHROMA_PORT = 8000;

    ChromaDockerComposeConnectionDetailsFactory() {
        super(CHROMA_CONTAINER_NAMES);
    }

    @Override
    protected ChromaConnectionDetails getDockerComposeConnectionDetails(DockerComposeConnectionSource source) {
        return new ChromaDockerComposeConnectionDetails(source.getRunningService());
    }

    /**
     * {@link ChromaConnectionDetails} backed by a Docker {@link RunningService}.
     */
    private static final class ChromaDockerComposeConnectionDetails extends DockerComposeConnectionDetails
            implements ChromaConnectionDetails {

        private final URI url;

        private ChromaDockerComposeConnectionDetails(RunningService service) {
            super(service);
            this.url = URI.create("http://" + service.host() + ":" + service.ports().get(CHROMA_PORT));
        }

        @Override
        public URI getUrl() {
            return url;
        }

        @Override
        public String getApiToken() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

        @Override
        public String getUsername() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

        @Override
        public String getPassword() {
            // Not supported because Chroma doesn't allow configuration via environment
            // variables.
            return null;
        }

    }

}
