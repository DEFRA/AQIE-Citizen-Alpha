package com.example.airqualitylimitedjs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PostcodeValidator implements ConstraintValidator<Postcode, String> {
    private final PostcodeRegexMatcher postcodeRegexMatcher;

    public PostcodeValidator() {
        postcodeRegexMatcher = new PostcodeRegexMatcher();
    }

    @Override
    public boolean isValid(String zipCode, ConstraintValidatorContext constraintValidatorContext) {
        return postcodeRegexMatcher.isValid(zipCode);
    }
}

