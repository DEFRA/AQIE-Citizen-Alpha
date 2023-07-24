package com.example.airqualitylimitedjs.validation;

import java.util.regex.Pattern;

public class PostcodeRegexMatcher {
    private static final String UK_GOV_POSTCODE_REGEX = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";

    private final Pattern pattern;

    public PostcodeRegexMatcher() {
        pattern = Pattern.compile(UK_GOV_POSTCODE_REGEX);
    }

    public boolean isValid(String postcode) {
        return pattern.matcher(postcode).matches();
    }
}
