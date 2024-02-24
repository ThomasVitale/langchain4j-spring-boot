package io.thomasvitale.langchain4j.spring.weaviate;

import java.util.List;
import java.util.Map;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import org.springframework.util.StringUtils;

import static io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore.ADDITIONAL_CERTAINTY_FIELD_NAME;
import static io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore.ADDITIONAL_FIELD_NAME;
import static io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore.ADDITIONAL_ID_FIELD_NAME;
import static io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore.ADDITIONAL_VECTOR_FIELD_NAME;
import static io.thomasvitale.langchain4j.spring.weaviate.WeaviateEmbeddingStore.CONTENT_FIELD_NAME;

/**
 * Utility class to convert between Weaviate and Langchain4j types.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public class WeaviateAdapters {

    public static EmbeddingMatch<TextSegment> toEmbeddingMatch(Map<String, ?> item) {
        Map<String, ?> additional = (Map<String, ?>) item.get(ADDITIONAL_FIELD_NAME);
        double certainty = (Double) additional.get(ADDITIONAL_CERTAINTY_FIELD_NAME);
        String id = (String) additional.get(ADDITIONAL_ID_FIELD_NAME);
        List<Float> embedding = ((List<Double>) additional.get(ADDITIONAL_VECTOR_FIELD_NAME)).stream()
                .map(Double::floatValue).toList();

        String content = (String) item.get(CONTENT_FIELD_NAME);

        return new EmbeddingMatch<>(certainty, id, Embedding.from(embedding), StringUtils.hasText(content) ? TextSegment.from(content) : null);
    }

}
