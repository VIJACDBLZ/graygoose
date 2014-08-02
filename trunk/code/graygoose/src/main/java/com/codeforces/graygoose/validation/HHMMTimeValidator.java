package com.codeforces.graygoose.validation;

import org.apache.commons.lang.StringUtils;
import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

import java.util.regex.Pattern;

/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 08.11.12
 */
public class HHMMTimeValidator extends Validator {
    private static final HHMMTimeValidator INSTANCE = new HHMMTimeValidator();
    private static final Pattern TIME_PATTERN = Pattern.compile("^\\s*([0-1]?[0-9]|2[0-4])\\s*:\\s*([0-5][0-9])\\s*$");

    @Override
    public void run(String value) throws ValidationException {
        if (!StringUtils.isBlank(value) && !TIME_PATTERN.matcher(value).matches()) {
            throw new ValidationException($("Enter time in format HH:MM or leave field blank."));
        }
    }

    public static HHMMTimeValidator getInstance() {
        return INSTANCE;
    }
}
