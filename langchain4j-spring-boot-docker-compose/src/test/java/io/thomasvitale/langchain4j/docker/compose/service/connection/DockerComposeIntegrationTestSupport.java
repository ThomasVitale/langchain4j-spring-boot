package io.thomasvitale.langchain4j.docker.compose.service.connection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationShutdownHandlers;
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.function.ThrowingSupplier;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.fail;

/**
 * Base class for integration tests that use Docker Compose.
 * <p>
 * Based on the AbstractDockerComposeIntegrationTests class from the Spring Boot project.
 */
public abstract class DockerComposeIntegrationTestSupport {

    @TempDir
    private static Path tempDir;

    private final Resource composeResource;

    private final DockerImageName dockerImageName;

    @AfterAll
    static void shutDown() {
        SpringApplicationShutdownHandlers shutdownHandlers = SpringApplication.getShutdownHandlers();
        ((Runnable) shutdownHandlers).run();
    }

    protected DockerComposeIntegrationTestSupport(String composeResource, DockerImageName dockerImageName) {
        this.composeResource = new ClassPathResource(composeResource, getClass());
        this.dockerImageName = dockerImageName;
    }

    protected final <T extends ConnectionDetails> T run(Class<T> type) {
        SpringApplication application = new SpringApplication(Config.class);
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("spring.docker.compose.skip.in-tests", "false");
        properties.put("spring.docker.compose.file",
                transformedComposeFile(ThrowingSupplier.of(this.composeResource::getFile).get(), this.dockerImageName));
        properties.put("spring.docker.compose.stop.command", "down");
        application.setDefaultProperties(properties);
        return application.run().getBean(type);
    }

    private File transformedComposeFile(File composeFile, DockerImageName imageName) {
        File tempComposeFile = Path.of(tempDir.toString(), composeFile.getName()).toFile();
        try {
            String composeFileContent = FileCopyUtils.copyToString(new FileReader(composeFile));
            composeFileContent = composeFileContent.replace("{imageName}", imageName.asCanonicalNameString());
            FileCopyUtils.copy(composeFileContent, new FileWriter(tempComposeFile));
        }
        catch (IOException ex) {
            fail("Error transforming Docker compose file '" + composeFile + "' to '" + tempComposeFile + "': "
                    + ex.getMessage());
        }
        return tempComposeFile;
    }

    @Configuration(proxyBeanMethods = false)
    static class Config {

    }

}
