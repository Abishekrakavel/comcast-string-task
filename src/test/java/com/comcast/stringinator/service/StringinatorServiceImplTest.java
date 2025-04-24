package com.comcast.stringinator.service;

import com.comcast.stringinator.model.StatsResult;
import com.comcast.stringinator.model.StringinatorInput;
import com.comcast.stringinator.model.StringinatorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class StringinatorServiceImplTest {

    private StringinatorServiceImpl stringinatorService;

    @BeforeEach
    void setup() {
        stringinatorService = new StringinatorServiceImpl();
    }

    static Stream<String> inputProvider() {
        return Stream.of("hello", "world", "112233", "test", "AaAaBb");
    }

    @ParameterizedTest
    @MethodSource("inputProvider")
    void testStringinate_validInputs_shouldReturnExpectedResult(String input) {
        StringinatorInput stringInput = new StringinatorInput(input);
        StringinatorResult result = stringinatorService.stringinate(stringInput);

        assertEquals(input, result.getInput());
        assertEquals(input.length(), result.getLength());
        assertNotNull(result.getMostFrequentChar());
        assertTrue(result.getFrequency() > 0);
    }

    @Test
    void testStats_returnsCorrectStatsStructure() {
        stringinatorService.stringinate(new StringinatorInput("apple"));
        stringinatorService.stringinate(new StringinatorInput("banana"));
        stringinatorService.stringinate(new StringinatorInput("apple"));

        StatsResult stats = stringinatorService.stats();

        assertNotNull(stats);
        assertEquals("apple", stats.getMostPopularString());
        assertNotNull(stats.getLongestInput());
        assertTrue(stats.getInputs().containsKey("apple"));
    }
}
