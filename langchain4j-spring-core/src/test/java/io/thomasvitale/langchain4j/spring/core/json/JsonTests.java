package io.thomasvitale.langchain4j.spring.core.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.langchain4j.internal.Json;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Json}.
 * <p>
 * Adapted from JsonTest in the LangChain4j project.
 */
class JsonTests {

    @Test
    void conversionToJsonAndFromJsonWorks() throws JSONException {
        var testData = new TestData();
        testData.setSampleDate(LocalDate.of(2023, 1, 15));
        testData.setSampleDateTime(LocalDateTime.of(2023, 1, 15, 10, 20));
        testData.setSomeValue("value");

        var json = Json.toJson(testData);

        JSONAssert.assertEquals("""
                {
                      "sampleDate": "2023-01-15",
                      "sampleDateTime": "2023-01-15T10:20:00",
                      "some_value": "value"
                }
                """, json, JSONCompareMode.STRICT);

        var deserializedTestData = Json.fromJson(json, TestData.class);

        assertThat(deserializedTestData.getSampleDate()).isEqualTo(testData.getSampleDate());
        assertThat(deserializedTestData.getSampleDateTime()).isEqualTo(testData.getSampleDateTime());
        assertThat(deserializedTestData.getSomeValue()).isEqualTo(testData.getSomeValue());
    }

    @Test
    void toInputStreamWorksForList() throws IOException, JSONException {
        var testObjects = List.of(
                new TestObject("John", LocalDate.of(2021, 8, 17), LocalDateTime.of(2021, 8, 17, 14, 20)),
                new TestObject("Jane", LocalDate.of(2021, 8, 16), LocalDateTime.of(2021, 8, 16, 13, 19)));

        var inputStream = Json.toInputStream(testObjects, List.class);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            var resultJson = bufferedReader.lines().collect(Collectors.joining());
            JSONAssert.assertEquals("""
                    [
                        {"name":"John","date":"2021-08-17","dateTime":"2021-08-17T14:20:00"},
                        {"name":"Jane","date":"2021-08-16","dateTime":"2021-08-16T13:19:00"}
                    ]
                    """, resultJson, JSONCompareMode.STRICT);
        }
    }

    private static class TestObject {

        private final String name;

        private final LocalDate date;

        private final LocalDateTime dateTime;

        public TestObject(String name, LocalDate date, LocalDateTime dateTime) {
            this.name = name;
            this.date = date;
            this.dateTime = dateTime;
        }

    }

    private static class TestData {

        private LocalDate sampleDate;

        private LocalDateTime sampleDateTime;

        @JsonProperty("some_value")
        private String someValue;

        LocalDate getSampleDate() {
            return sampleDate;
        }

        void setSampleDate(LocalDate sampleDate) {
            this.sampleDate = sampleDate;
        }

        LocalDateTime getSampleDateTime() {
            return sampleDateTime;
        }

        void setSampleDateTime(LocalDateTime sampleDateTime) {
            this.sampleDateTime = sampleDateTime;
        }

        String getSomeValue() {
            return someValue;
        }

        void setSomeValue(String someValue) {
            this.someValue = someValue;
        }

    }

}
