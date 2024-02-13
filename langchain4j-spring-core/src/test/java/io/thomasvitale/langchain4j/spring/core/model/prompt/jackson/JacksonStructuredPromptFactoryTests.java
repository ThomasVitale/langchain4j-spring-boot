package io.thomasvitale.langchain4j.spring.core.model.prompt.jackson;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPrompt;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link JacksonStructuredPromptFactory}.
 * <p>
 * Adapted from DefaultStructuredPromptFactoryTest in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
class JacksonStructuredPromptFactoryTests {

    @Test
    void structuredPromptWithClass() {
        var structuredPromptFactory = new JacksonStructuredPromptFactory();
        Prompt prompt = structuredPromptFactory.toPrompt(new Greeting("Isabelle"));
        assertThat(prompt.text()).isEqualTo("Hello, my name is Isabelle");
    }

    @Test
    void structuredPromptWithRecord() {
        var structuredPromptFactory = new JacksonStructuredPromptFactory();
        Prompt prompt = structuredPromptFactory.toPrompt(new AnotherGreeting("Bjorn"));
        assertThat(prompt.text()).isEqualTo("Hello, my name is Bjorn");
    }

    @StructuredPrompt("Hello, my name is {{name}}")
    static class Greeting {

        public final String name;

        public Greeting(String name) {
            this.name = name;
        }

    }

    @StructuredPrompt("Hello, my name is {name}")
    record AnotherGreeting(String name) {
    }

}
