package io.thomasvitale.langchain4j.spring.core.aot;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

/**
 * Helpers to generate runtime hints for model and vector stores APIs.
 * <p>
 * Inspired by the Spring AI implementation.
 */
public class Langchain4jRuntimeHints {

	private static final Logger logger = LoggerFactory.getLogger(Langchain4jRuntimeHints.class);

	public static Set<TypeReference> findAllClassesInPackage(String packageName) {
		var typeFilter = new AssignableTypeFilter(Object.class);
        var classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);
        classPathScanningCandidateComponentProvider.addIncludeFilter(typeFilter);
        return classPathScanningCandidateComponentProvider.findCandidateComponents(packageName).stream()
                .map(beanDefinition -> TypeReference.of(Objects.requireNonNull(beanDefinition.getBeanClassName())))
                .peek(typeReference -> logger.debug("Generating reachability metadata for [{}]", typeReference.getName()))
                .collect(Collectors.toUnmodifiableSet());
	}

}
