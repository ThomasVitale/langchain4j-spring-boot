package io.thomasvitale.langchain4j.spring.openai.api.chat;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.util.Assert;

/**
 * A tool the model may call. Currently, only functions are supported as a tool.
 *
 * @param type The type of the tool. Currently, only 'function' is supported.
 * @param function The function definition.
 *
 * @author Thomas Vitale
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Tool(
        Type type,
        Function function
) {

    public Tool {
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(function, "function cannot be null");
    }

    @ConstructorBinding
    public Tool(Function function) {
        this(Type.FUNCTION, function);
    }

    public enum Type {
        @JsonProperty("function") FUNCTION
    }

    /**
     * Function definition.
     *
     * @param description A description of what the function does, used by the model to choose
     *                    when and how to call the function.
     * @param name The name of the function to be called. Must be a-z, A-Z, 0-9, or contain
     *             underscores and dashes, with a maximum length of 64.
     * @param parameters The parameters the functions accepts, described as a JSON Schema object.
     *                   Omitting 'parameters' defines a function with an empty parameter list.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Function(
            String description,
            String name,
            Parameters parameters
    ) {

        public Function {
            Assert.hasText(name, "name cannot be null or empty");
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String description;
            private String name;
            private Parameters parameters;

            private Builder() {}

            public Builder description(String description) {
                this.description = description;
                return this;
            }

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder parameters(Parameters parameters) {
                this.parameters = parameters;
                return this;
            }

            public Function build() {
                return new Function(description, name, parameters);
            }
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Parameters(
            String type,
            Map<String, Map<String, Object>> properties,
            List<String> required
    ){
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final String type = "object";
            private Map<String, Map<String, Object>> properties;
            private List<String> required;

            private Builder(){}

            public Builder properties(Map<String, Map<String, Object>> properties) {
                this.properties = properties;
                return this;
            }

            public Builder required(List<String> required) {
                this.required = required;
                return this;
            }

            public Parameters build() {
                return new Parameters(type, properties, required);
            }
        }

    }

}
