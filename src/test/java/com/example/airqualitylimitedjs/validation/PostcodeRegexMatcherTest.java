package com.example.airqualitylimitedjs.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostcodeRegexMatcherTest {
    private final PostcodeRegexMatcher postcodeRegexMatcher = new PostcodeRegexMatcher();

    @ParameterizedTest
    @ValueSource(strings = {"SO16 0AU", "SO160AU"})
    public void givenValidPostcode_shouldReturnTrue(String input) {
        assertTrue(postcodeRegexMatcher.isValid(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"SO16", "a", "  SO160AU", "SO160AU  ", "  SO160AU  "})
    public void givenInvalidPostcode_shouldReturnFalse(String input) {
        assertFalse(postcodeRegexMatcher.isValid(input));
    }
}
