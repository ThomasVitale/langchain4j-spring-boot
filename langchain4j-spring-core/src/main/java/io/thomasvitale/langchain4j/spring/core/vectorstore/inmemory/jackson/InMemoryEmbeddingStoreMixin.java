package io.thomasvitale.langchain4j.spring.core.vectorstore.inmemory.jackson;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

/**
 * Mixin used to serialize / deserialize {@link InMemoryEmbeddingStore}.
 * <p>
 * Adapted from the Quarkus LangChain4j implementation.
 *
 * @author Thomas Vitale
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = InMemoryEmbeddingStoreMixin.InMemoryEmbeddingStoreDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class InMemoryEmbeddingStoreMixin {

    static class InMemoryEmbeddingStoreDeserializer extends JsonDeserializer<InMemoryEmbeddingStore<TextSegment>> {

        @Override
        public InMemoryEmbeddingStore<TextSegment> deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException {
            return context.readValue(jsonParser, InMemoryEmbeddingStoreMirror.class).toInMemoryEmbeddingStore();
        }

        private static class InMemoryEmbeddingStoreMirror {

            public List<InMemoryEmbeddingStoreEntryMirror> entries;

            public InMemoryEmbeddingStore<TextSegment> toInMemoryEmbeddingStore() {
                var result = new InMemoryEmbeddingStore<TextSegment>();
                for (var entry : entries) {
                    result.add(entry.id(), entry.embedding(), entry.embedded());
                }
                return result;
            }

        }

        private record InMemoryEmbeddingStoreEntryMirror(String id, Embedding embedding, TextSegment embedded) {
        }

    }

}
