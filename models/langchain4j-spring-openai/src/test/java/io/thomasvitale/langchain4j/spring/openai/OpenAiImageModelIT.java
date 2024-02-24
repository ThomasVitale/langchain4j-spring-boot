package io.thomasvitale.langchain4j.spring.openai;

import java.util.List;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import io.thomasvitale.langchain4j.spring.openai.api.image.ImageModels;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClient;
import io.thomasvitale.langchain4j.spring.openai.client.OpenAiClientConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OpenAiImageModel}.
 * <p>
 * Adapted from OpenAiImageModelIT in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
@EnabledIfEnvironmentVariable(named = "LANGCHAIN4J_OPENAI_CLIENT_API_KEY", matches = ".*")
class OpenAiImageModelIT {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiChatModelIT.class);

    private static OpenAiClient openAiClient;

    @BeforeAll
    static void beforeAll() {
        var apiKey = System.getenv("LANGCHAIN4J_OPENAI_CLIENT_API_KEY");
        openAiClient = new OpenAiClient(OpenAiClientConfig.builder().apiKey(apiKey).build(), RestClient.builder());
    }

    @Test
    void generateSingleImage() {
        OpenAiImageModel imageModel = OpenAiImageModel.builder()
                .client(openAiClient)
                .options(OpenAiImageOptions.builder()
                        .model(ImageModels.DALL_E_2.toString())
                        .n(1)
                        .size("256x256")
                        .build())
                .build();

        Response<Image> response = imageModel.generate("sun");

        Image image = response.content();
        assertThat(image.url()).isNotNull();

        logger.info("Generated image: {}", image.url());
    }

    @Test
    void generateMultipleImages() {
        OpenAiImageModel imageModel = OpenAiImageModel.builder()
                .client(openAiClient)
                .options(OpenAiImageOptions.builder()
                        .model(ImageModels.DALL_E_2.toString())
                        .n(1)
                        .size("256x256")
                        .build())
                .build();

        Response<List<Image>> response = imageModel.generate("sun", 2);

        List<Image> images = response.content();
        assertThat(images).hasSize(2);
        assertThat(images.get(0).url()).isNotNull();
        assertThat(images.get(1).url()).isNotNull();

        logger.info("Generated image 1: {}", images.get(0).url());
        logger.info("Generated image 2: {}", images.get(1).url());
    }

}
