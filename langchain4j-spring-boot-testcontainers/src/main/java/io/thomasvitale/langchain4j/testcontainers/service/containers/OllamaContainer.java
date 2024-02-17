package io.thomasvitale.langchain4j.testcontainers.service.containers;

import java.io.IOException;

import com.github.dockerjava.api.command.InspectContainerResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Testcontainers implementation for Ollama.
 * <p>
 * Supported image: {@code docker.io/ollama/ollama} and custom images.
 * <p>
 * Exposed ports: 11434
 *
 * @author Thomas Vitale
 */
public class OllamaContainer extends GenericContainer<OllamaContainer> {

    private static final Logger logger = LoggerFactory.getLogger(OllamaContainer.class);

    public static final String DEFAULT_OCI_IMAGE_NAME = "ollama/ollama";

    public static final Integer OLLAMA_PORT = 11434;

    private String model;

    private String ociImageName;

    public OllamaContainer(String ociImageName) {
        this(DockerImageName.parse(ociImageName));
        this.ociImageName = ociImageName;
    }

    public OllamaContainer(DockerImageName ociImageName) {
        super(ociImageName);
        addExposedPort(OLLAMA_PORT);
        this.ociImageName = ociImageName.asCanonicalNameString();
    }

    public OllamaContainer withModel(String model) {
        Assert.hasText(model, "model cannot be empty");
        this.model = model;
        return this;
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo) {
        if (StringUtils.hasText(model)) {
            try {
                logger.info("Start pulling the '%s' model. It will take several minutes...".formatted(model));
                execInContainer("ollama", "pull", this.model);
                logger.info("Completed pulling the '%s' model".formatted(model));
            }
            catch (IOException | InterruptedException ex) {
                throw new RuntimeException("Error pulling orca-mini model", ex);
            }
        }
    }

}
