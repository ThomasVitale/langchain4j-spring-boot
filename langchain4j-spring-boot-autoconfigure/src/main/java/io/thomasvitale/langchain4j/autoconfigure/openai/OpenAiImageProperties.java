package io.thomasvitale.langchain4j.autoconfigure.openai;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.nio.file.Paths;

@ConfigurationProperties(prefix = OpenAiImageProperties.CONFIG_PREFIX)
public class OpenAiImageProperties {

    public static final String CONFIG_PREFIX = "langchain4j.openai.image";

    private String model = "dall-e-2";
    private Boolean persist = false;
    private Path persistDirectory = Paths.get(System.getProperty("java.io.tmpdir"));
    private String responseFormat = "url";
    private String size = "1024x1024";
    private String quality = "standard";
    private Integer number = 1;
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
