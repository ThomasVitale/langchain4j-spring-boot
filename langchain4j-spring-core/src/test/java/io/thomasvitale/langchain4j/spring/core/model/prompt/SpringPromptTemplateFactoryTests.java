package io.thomasvitale.langchain4j.spring.core.model.prompt;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link SpringPromptTemplateFactory}.
 * <p>
 * Adapted from PromptTemplateTest in the LangChain4j project.
 */
class SpringPromptTemplateFactoryTests {

    @Test
    void createPromptFromTemplateWithoutVariables() {
        String template = "They're taking the hobbits to Isengard!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of();

        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text()).isEqualTo("They're taking the hobbits to Isengard!");
    }

    @Test
    void createPromptFromTemplateWithItVariables() {
        String template = "They're taking the hobbits to {it}!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);

        Prompt prompt = promptTemplate.apply("Isengard");

        assertThat(prompt.text()).isEqualTo("They're taking the hobbits to Isengard!");
    }

    @Test
    void createPromptFromTemplateWithSingleVariable() {
        String template = "They're taking the hobbits to {place}!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("place", "Isengard");

        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text()).isEqualTo("They're taking the hobbits to Isengard!");
    }

    @Test
    void createPromptFromTemplateWithSingleVariableRepeated() {
        String template = "They're taking the hobbits to {place}! To {place}! To {place}!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("place", "Isengard");

        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text()).isEqualTo("They're taking the hobbits to Isengard! To Isengard! To Isengard!");
    }

    @Test
    void createPromptFromTemplateWithMultipleVariables() {
        String template = "Today is {day} and it's {temperature} degrees. That's {evaluation}.";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("day", "Wednesday", "temperature", 42, "evaluation", true);

        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text()).isEqualTo("Today is Wednesday and it's 42 degrees. That's true.");
    }

    @Test
    void createPromptFromTemplateWhenValueIsMissing() {
        String template = "They're taking the hobbits to {place}!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of();

        assertThatThrownBy(() -> promptTemplate.apply(variables)).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Not all template variables were replaced")
            .hasMessageContaining("place");
    }

    @Test
    void createPromptFromTemplateWhenValueIsNull() {
        String template = "They're taking the hobbits to {place}!";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = new HashMap<>();
        variables.put("place", null);

        assertThatThrownBy(() -> promptTemplate.apply(variables)).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Not all input variables have a non-empty value")
            .hasMessageContaining("place");
    }

    @Test
    void createPromptFromTemplateWhenTemplateIsNotValid() {
        String template = "They're taking the hobbits to {place!";

        assertThatThrownBy(() -> PromptTemplate.from(template)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("The template string is not valid");
    }

    @Test
    void createPromptFromTemplateWithProvidedDate() {
        String template = "They're taking the hobbits to {place}! When? {current_date}";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("place", "Isengard");

        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text())
            .isEqualTo("They're taking the hobbits to Isengard! When? %s".formatted(LocalDate.now()));
    }

    @Test
    void createPromptFromTemplateWithProvidedTime() {
        Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        String template = "They're taking the hobbits to {place}! When? {current_time}";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("place", "Isengard");

        ReflectionTestUtils.setField(promptTemplate, "clock", clock);
        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text())
            .isEqualTo("They're taking the hobbits to Isengard! When? %s".formatted(LocalTime.now(clock)));
    }

    @Test
    void createPromptFromTemplateWithProvidedDateTime() {
        Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        String template = "They're taking the hobbits to {place}! When? {current_date_time}";
        PromptTemplate promptTemplate = PromptTemplate.from(template);
        Map<String, Object> variables = Map.of("place", "Isengard");

        ReflectionTestUtils.setField(promptTemplate, "clock", clock);
        Prompt prompt = promptTemplate.apply(variables);

        assertThat(prompt.text())
            .isEqualTo("They're taking the hobbits to Isengard! When? %s".formatted(LocalDateTime.now(clock)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "$", "$$", "{", "{{", "}", "}}", "{}", "{{}}", "*", "**", "\\", "\\\\", "${}*\\",
            "${ *hello* }", "\\$\\{ \\*hello\\* \\}" })
    void createPromptFromTemplateWithSpecialCharacters(String s) {
        PromptTemplate promptTemplate = PromptTemplate.from("This is {it}.");
        Prompt prompt = promptTemplate.apply(s);
        System.out.println(prompt.text());
        assertThat(prompt.text()).isEqualTo("This is " + s + ".");
    }

}
