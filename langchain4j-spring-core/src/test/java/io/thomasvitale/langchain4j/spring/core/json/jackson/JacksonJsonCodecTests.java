package io.thomasvitale.langchain4j.spring.core.json.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.thomasvitale.langchain4j.spring.core.json.JsonDeserializationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Unit tests for {@link JacksonJsonCodec}.
 * <p>
 * Adapted from GsonJsonCodecTest in the LangChain4j project.
 *
 * @author Thomas Vitale
 */
class JacksonJsonCodecTests {

    @Test
    public void parsePojo() throws Exception {
        var jsonCodec = new JacksonJsonCodec();

        var example = new ExamplePojo("John", 42);
        assertThat(jsonCodec.fromJson(jsonCodec.toJson(example), ExamplePojo.class)).isEqualTo(example);

        var inputStream = jsonCodec.toInputStream(example, ExamplePojo.class);
        assertThat(jsonCodec.fromJson(readAllBytes(inputStream), ExamplePojo.class)).isEqualTo(example);
    }

    @Test
    public void parseRecord() throws Exception {
        var jsonCodec = new JacksonJsonCodec();

        var example = new ExampleRecord("John", 42);
        assertThat(jsonCodec.fromJson(jsonCodec.toJson(example), ExampleRecord.class)).isEqualTo(example);

        var inputStream = jsonCodec.toInputStream(example, ExampleRecord.class);
        assertThat(jsonCodec.fromJson(readAllBytes(inputStream), ExampleRecord.class)).isEqualTo(example);
    }

    @Test
    public void parseMap() throws JSONException {
        var jsonCodec = new JacksonJsonCodec();

        var expectedMap = Map.of("a", "b");

        JSONAssert.assertEquals(jsonCodec.toJson(expectedMap), """
                {
                    "a": "b"
                }
                """, JSONCompareMode.STRICT);

        assertThat(jsonCodec.fromJson("""
                {
                    "a": "b"
                }
                """, (Class<?>) expectedMap.getClass())).isEqualTo(expectedMap);
    }

    @Test
    public void parseEnum() throws Exception {
        var jsonCodec = new JacksonJsonCodec();

        var example = new ExampleWithEnum(ExampleWithEnum.Demo.TEST1);
        assertThat(jsonCodec.fromJson(jsonCodec.toJson(example), ExampleWithEnum.class)).isEqualTo(example);

        var inputStream = jsonCodec.toInputStream(example, ExampleWithEnum.class);
        assertThat(jsonCodec.fromJson(readAllBytes(inputStream), ExampleWithEnum.class)).isEqualTo(example);

        var yetAnotherExample = new ExampleWithEnum(ExampleWithEnum.Demo.TEST2);
        JSONAssert.assertEquals(jsonCodec.toJson(yetAnotherExample), """
                {
                    "demo": "TEST2"
                }
                """, JSONCompareMode.STRICT);

        assertThat(jsonCodec.fromJson("""
                {
                    "demo": "TEST2"
                }
                """, ExampleWithEnum.class)).isEqualTo(yetAnotherExample);
    }

    @Test
    public void parseDateTime() throws JSONException {
        var jsonCodec = new JacksonJsonCodec();

        var example = new DateExamplePojo(LocalDate.of(2019, 1, 1), LocalDateTime.of(2019, 1, 1, 0, 0, 0));

        JSONAssert.assertEquals(jsonCodec.toJson(example), """
                {
                    "localDate": "2019-01-01",
                    "localDateTime": "2019-01-01T00:00:00"
                }
                """, JSONCompareMode.STRICT);

        assertThat(jsonCodec.fromJson(jsonCodec.toJson(example), DateExamplePojo.class)).isEqualTo(example);
    }

    @Test
    void parseWithError() {
        var jsonCodec = new JacksonJsonCodec();

        assertThatExceptionOfType(JsonDeserializationException.class)
            .isThrownBy(() -> jsonCodec.fromJson("abc", Integer.class));
    }

    private static String readAllBytes(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buf = new byte[1024];
        while (true) {
            int n = stream.read(buf);
            if (n < 0) {
                break;
            }
            sb.append(new String(buf, 0, n));
        }
        return sb.toString();
    }

    record ExampleRecord(String name, int age) {
    }

    record ExampleWithEnum(ExampleWithEnum.Demo demo) {
        enum Demo {

            TEST1, TEST2

        }
    }

    static class ExamplePojo {

        private final String name;

        private final int age;

        @JsonCreator
        public ExamplePojo(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            var example = (ExamplePojo) o;
            return age == example.age && Objects.equals(name, example.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }

    }

    static class DateExamplePojo {

        private final LocalDate localDate;

        private final LocalDateTime localDateTime;

        @JsonCreator
        public DateExamplePojo(LocalDate localDate, LocalDateTime localDateTime) {
            this.localDate = localDate;
            this.localDateTime = localDateTime;
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            var that = (DateExamplePojo) o;
            return Objects.equals(localDate, that.localDate) && Objects.equals(localDateTime, that.localDateTime);
        }

        @Override
        public int hashCode() {
            return Objects.hash(localDate, localDateTime);
        }

    }

}
