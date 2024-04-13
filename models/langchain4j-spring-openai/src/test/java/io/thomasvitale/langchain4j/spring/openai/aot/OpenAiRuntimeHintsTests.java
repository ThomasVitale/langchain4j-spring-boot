package io.thomasvitale.langchain4j.spring.openai.aot;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.TypeReference;

import io.thomasvitale.langchain4j.spring.openai.api.Usage;

import static io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints.findAllClassesInPackage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.aot.hint.predicate.RuntimeHintsPredicates.reflection;

/**
 * Unit tests for {@link OpenAiRuntimeHints}.
 */
class OpenAiRuntimeHintsTests {

    @Test
    void hintsAreRegistered() {
        RuntimeHints runtimeHints = new RuntimeHints();
        OpenAiRuntimeHints openAiRuntimeHints = new OpenAiRuntimeHints();
        openAiRuntimeHints.registerHints(runtimeHints, null);

        Set<TypeReference> apiClasses = findAllClassesInPackage(Usage.class.getPackageName());
        for (TypeReference apiClass : apiClasses) {
            assertThat(runtimeHints).matches(reflection().onType(apiClass));
        }
    }

}
