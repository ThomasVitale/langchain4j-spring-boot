package io.thomasvitale.langchain4j.testcontainers.service.containers;

import org.json.JSONException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.web.client.RestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class OllamaContainerIT {

    private final RestClient restClient = RestClient.builder().build();

    @Nested
    class OllamaContainerWithDefaultImageIT {

        @Container
        static final OllamaContainer container = new OllamaContainer("ollama/ollama");

        @Test
        void noModelAvailable() throws JSONException {
            var ollamaUrl = "http://" + container.getHost() + ":"
                    + container.getMappedPort(OllamaContainer.OLLAMA_PORT);

            var listOfModels = restClient.get()
                .uri(ollamaUrl + "/api/tags")
                .retrieve()
                .toEntity(String.class)
                .getBody();

            JSONAssert.assertEquals("""
                    {
                        "models": []
                    }
                    """, listOfModels, JSONCompareMode.STRICT);
        }

    }

    @Nested
    class OllamaContainerWithCustomImageAndModelIT {

        @Container
        static final OllamaContainer container = new OllamaContainer("ollama/ollama").withModel("orca-mini");

        @Test
        void downloadedModelAvailable() {
            var ollamaUrl = "http://" + container.getHost() + ":"
                    + container.getMappedPort(OllamaContainer.OLLAMA_PORT);

            var listOfModels = restClient.get()
                .uri(ollamaUrl + "/api/tags")
                .retrieve()
                .toEntity(String.class)
                .getBody();

            assertThat(listOfModels).contains("orca-mini");
        }

    }

    @Nested
    class OllamaContainerWithCustomImageIT {

        @Container
        static final OllamaContainer container = new OllamaContainer("ghcr.io/thomasvitale/ollama-orca-mini");

        @Test
        void builtInModelAvailable() {
            var ollamaUrl = "http://" + container.getHost() + ":"
                    + container.getMappedPort(OllamaContainer.OLLAMA_PORT);

            var listOfModels = restClient.get()
                .uri(ollamaUrl + "/api/tags")
                .retrieve()
                .toEntity(String.class)
                .getBody();

            assertThat(listOfModels).contains("orca-mini");
        }

    }

}
