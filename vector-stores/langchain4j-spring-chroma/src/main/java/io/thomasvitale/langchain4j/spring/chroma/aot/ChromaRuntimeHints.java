package io.thomasvitale.langchain4j.spring.chroma.aot;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import io.thomasvitale.langchain4j.spring.chroma.api.Collection;
import io.thomasvitale.langchain4j.spring.core.aot.Langchain4jRuntimeHints;

/**
 * Register runtime hints for Chroma.
 */
public class ChromaRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        for (var typeReference : Langchain4jRuntimeHints.findAllClassesInPackage(Collection.class.getPackageName()))
            hints.reflection().registerType(typeReference, MemberCategory.values());
    }

}
