package io.thomasvitale.langchain4j.docker.compose.service.connection.weaviate;

import java.net.URI;

import org.springframework.boot.docker.compose.core.RunningService;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionDetailsFactory;
import org.springframework.boot.docker.compose.service.connection.DockerComposeConnectionSource;

import io.thomasvitale.langchain4j.autoconfigure.vectorstores.weaviate.WeaviateConnectionDetails;

/**
 * {@link DockerComposeConnectionDetailsFactory} to create {@link WeaviateConnectionDetails}
 * for a {@code "weaviate"} service.
 */
public class WeaviateDockerComposeConnectionDetailsFactory
        extends DockerComposeConnectionDetailsFactory<WeaviateConnectionDetails> {

    private static final String[] WEAVIATE_CONTAINER_NAMES = { "docker.io/semitechnologies/weaviate", "semitechnologies/weaviate", "cr.weaviate.io/semitechnologies/weaviate" };

    private static final int WEAVIATE_PORT = 8080;

    WeaviateDockerComposeConnectionDetailsFactory() {
        super(WEAVIATE_CONTAINER_NAMES);
    }

    @Override
    protected WeaviateConnectionDetails getDockerComposeConnectionDetails(DockerComposeConnectionSource source) {
        return new WeaviateDockerComposeConnectionDetails(source.getRunningService());
    }

    /**
     * {@link WeaviateConnectionDetails} backed by a Docker {@link RunningService}.
     */
    private static final class WeaviateDockerComposeConnectionDetails extends DockerComposeConnectionDetails
            implements WeaviateConnectionDetails {

        private final URI url;
        private final String apiKey;

        private WeaviateDockerComposeConnectionDetails(RunningService service) {
            super(service);
            this.url = URI.create("http://" + service.host() + ":" + service.ports().get(WEAVIATE_PORT));
            this.apiKey = "";
        }

        @Override
        public URI getUrl() {
            return url;
        }

        @Override
        public String getApiKey() {
            return apiKey;
        }

    }

}
