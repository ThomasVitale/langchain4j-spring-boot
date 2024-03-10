package io.thomasvitale.langchain4j.spring.openai;

import java.nio.file.Path;
import java.nio.file.Paths;

import io.thomasvitale.langchain4j.spring.openai.api.image.ImageModels;

/**
 * Options for customizing interactions with the OpenAI Image Generation API.
 */
public class OpenAiImageOptions {

    /**
     * The model to use for image generation.
     */
    private String model = ImageModels.DALL_E_2.toString();

    /**
     * The number of images to generate. Must be between 1 and 10. For dall-e-3, only n=1 is supported.
     */
    private Integer n = 1;

    /**
     * The quality of the image that will be generated. hd creates images with finer details and greater consistency across the image. This param is only supported for dall-e-3.
     */
    private String quality;

    /**
     * The format in which the generated images are returned. Must be one of 'url' or 'b64_json'.
     */
    private String responseFormat;

    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024 for dall-e-2. Must be one of 1024x1024, 1792x1024, or 1024x1792 for dall-e-3 models.
     */
    private String size;

    /**
     * The style of the generated images. Must be one of vivid or natural. Vivid causes the model to lean towards generating hyper-real and dramatic images. Natural causes the model to produce more natural, less hyper-real looking images. This param is only supported for dall-e-3.
     */
    private String style;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     */
    private String user;

    /**
     * Whether the generated image(s) will be saved to disk.
     */
    private Boolean persist = false;

    /**
     * Path where the generated image(s) will be saved to disk.
     * Only if {@code persist} is set to true.
     */
    private Path persistDirectory = Paths.get(System.getProperty("java.io.tmpdir"));

    // Builders

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OpenAiImageOptions options = new OpenAiImageOptions();

        private Builder() {}

        public Builder model(String model) {
            this.options.model = model;
            return this;
        }

        public Builder n(Integer n) {
            this.options.n = n;
            return this;
        }

        public Builder quality(String quality) {
            this.options.quality = quality;
            return this;
        }

        public Builder responseFormat(String responseFormat) {
            this.options.responseFormat = responseFormat;
            return this;
        }

        public Builder size(String size) {
            this.options.size = size;
            return this;
        }

        public Builder style(String style) {
            this.options.style = style;
            return this;
        }

        public Builder user(String user) {
            this.options.user = user;
            return this;
        }

        public Builder persist(Boolean persist) {
            this.options.persist = persist;
            return this;
        }

        public Builder persistDirectory(Path persistDirectory) {
            this.options.persistDirectory = persistDirectory;
            return this;
        }

        public OpenAiImageOptions build() {
            return options;
        }
    }

    // Getters and Setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

}
