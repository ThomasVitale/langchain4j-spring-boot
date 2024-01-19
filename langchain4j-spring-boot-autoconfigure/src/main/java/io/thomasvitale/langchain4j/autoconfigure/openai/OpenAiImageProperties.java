package io.thomasvitale.langchain4j.autoconfigure.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration properties for OpenAI image clients.
 *
 * @author Thomas Vitale
 */
@ConfigurationProperties(prefix = OpenAiImageProperties.CONFIG_PREFIX)
public class OpenAiImageProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.image";

    /**
     * The name of the model to use.
     */
    private String model = "dall-e-2";

    /**
     * Whether the generated image(s) will be saved to disk.
     */
    private Boolean persist = false;

    /**
     * The path where the generated image(s) will be saved to disk.
     * <p>
     * Only if {@code persist} is set to true.
     */
    private Path persistDirectory = Paths.get(System.getProperty("java.io.tmpdir"));

    /**
     * The format in which the generated images are returned.
     * Must be one of {@code url} or {@code b64_json}.
     */
    private String responseFormat = "url";

    /**
     * The size of the generated images.
     * <p>
     * Must be one of {@code 256x256}, {@code 512x512}, or {@code 1024x1024} when the model is {@code dall-e-2}.
     * <p>
     * Must be one of {@code 1024x1024}, {@code 1792x1024}, or {@code 1024x1792} when the model is {@code dall-e-3}.
     */
    private String size = "1024x1024";

    /**
     * The quality of the image that will be generated.
     * <p>
     * {@code hd} creates images with finer details and greater consistency across the image.
     * <p>
     * Only supported when using {@code dall-e-3}.
     */
    private String quality = "standard";

    /**
     * The number of images to generate. Must be between 1 and 10.
     * <p>
     * When using {@code dall-e-3}, only 1 is supported.
     */
    private Integer number = 1;

    /**
     * The style of the generated images.
     * Must be one of {@code vivid} or {@code natural}.
     * <p>
     * Vivid causes the model to lean towards generating hyper-real and dramatic images.
     * Natural causes the model to produce more natural, less hyper-real looking images.
     * <p>
     * Only supported when using {@code dall-e-3}.
     */
    private String style = "vivid";

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getPersist() {
        return persist;
    }

    public void setPersist(Boolean persist) {
        this.persist = persist;
    }

    public Path getPersistDirectory() {
        return persistDirectory;
    }

    public void setPersistDirectory(Path persistDirectory) {
        this.persistDirectory = persistDirectory;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(String responseFormat) {
        this.responseFormat = responseFormat;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
