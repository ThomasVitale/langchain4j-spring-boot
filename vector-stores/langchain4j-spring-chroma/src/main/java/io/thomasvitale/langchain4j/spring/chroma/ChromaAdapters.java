package io.thomasvitale.langchain4j.spring.chroma;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;

import org.springframework.util.CollectionUtils;

import io.thomasvitale.langchain4j.spring.chroma.api.QueryResponse;

/**
 * Utility class to convert between Chroma and Langchain4j types.
 * <p>
 * Based on the original LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
public final class ChromaAdapters {

    private record ChromaEmbedding(
    //@formatter:off
            String id,
            List<Float> embedding,
            String document,
            Map<String, String> metadata,
            Double distance
    //@formatter:on
    ) {
    }

    public static List<EmbeddingMatch<TextSegment>> toEmbeddingMatches(QueryResponse queryResponse) {
        List<ChromaEmbedding> chromaEmbeddings = extractChromaEmbeddingsFromResponse(queryResponse);

        return chromaEmbeddings.stream().map(chromaEmbedding -> {
            Embedding embedding = Embedding.from(chromaEmbedding.embedding());
            TextSegment textSegment = toTextSegment(chromaEmbedding);
            double score = scoreDistanceForCosineSimilaritySearch(chromaEmbedding.distance());
            return new EmbeddingMatch<>(score, chromaEmbedding.id(), embedding, textSegment);
        }).toList();
    }

    private static List<ChromaEmbedding> extractChromaEmbeddingsFromResponse(QueryResponse queryResponse) {
        List<ChromaEmbedding> result = new ArrayList<>();

        if (queryResponse == null || CollectionUtils.isEmpty(queryResponse.ids())) {
            return result;
        }
        for (int i = 0; i < queryResponse.ids().get(0).size(); i++) {
            //@formatter:off
            var chromaEmbedding = new ChromaEmbedding(
                    queryResponse.ids().get(0).get(i),
                    queryResponse.embeddings().get(0).get(i),
                    queryResponse.documents().get(0).get(i),
                    queryResponse.metadata().get(0).get(i),
                    queryResponse.distances().get(0).get(i));
            result.add(chromaEmbedding);
            //@formatter:on
        }

        return result;
    }

    private static TextSegment toTextSegment(ChromaEmbedding chromaEmbedding) {
        var text = chromaEmbedding.document;
        var metadata = chromaEmbedding.metadata;
        return text == null ? null : TextSegment.from(text, metadata == null ? new Metadata() : new Metadata(metadata));
    }

    private static double scoreDistanceForCosineSimilaritySearch(double distance) {
        return 1 - distance;
    }

}
