package io.thomasvitale.langchain4j.testcontainers.service.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Testcontainers implementation for Ollama.
 * <p>
 * Supported image: {@code postgres}
 * <p>
 * Exposed ports: 11434
 */
public class OllamaContainer extends GenericContainer<OllamaContainer> {

    public static final Integer OLLAMA_PORT = 11434;

    public OllamaContainer(String ociImageName) {
        this(DockerImageName.parse(ociImageName));
    }

    public OllamaContainer(DockerImageName ociImageName) {
        super(ociImageName);
        addExposedPort(OLLAMA_PORT);
    }

}
