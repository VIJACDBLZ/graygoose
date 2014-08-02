package com.codeforces.graygoose.validation;

import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 08.11.12
 */
public class PatternValidator extends Validator {
    private static final PatternValidator INSTANCE = new PatternValidator();

    @Override
    public void run(String value) throws ValidationException {
        try {
            Pattern.compile(value);
        } catch (PatternSyntaxException e) {
            throw new ValidationException($("Illegal regex: {0}", e.getMessage()));
        }
    }

    public static PatternValidator getInstance() {
        return INSTANCE;
    }
}
