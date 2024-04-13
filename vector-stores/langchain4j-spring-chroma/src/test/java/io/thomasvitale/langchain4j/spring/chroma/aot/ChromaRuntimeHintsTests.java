package io.thomasvitale.langchain4j.spring.chroma.aot;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.TypeReference;

import io.thomasvitale.langchain4j.spring.chroma.api.Collection;

import static io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints.findAllClassesInPackage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.aot.hint.predicate.RuntimeHintsPredicates.reflection;

/**
 * Unit tests for {@link ChromaRuntimeHints}.
 */
class ChromaRuntimeHintsTests {

    @Test
    void hintsAreRegistered() {
        RuntimeHints runtimeHints = new RuntimeHints();
        ChromaRuntimeHints chromaRuntimeHints = new ChromaRuntimeHints();
        chromaRuntimeHints.registerHints(runtimeHints, null);

        Set<TypeReference> apiClasses = findAllClassesInPackage(Collection.class.getPackageName());
        for (TypeReference apiClass : apiClasses) {
            assertThat(runtimeHints).matches(reflection().onType(apiClass));
        }
    }

}
