package com.codeforces.graygoose.validation;

import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ResponseCodesValidator extends Validator {

    private static final Pattern RESPONSE_CODE_PATTERN = Pattern.compile("\\d+");
    private static final Pattern RESPONSE_CODE_RANGE_PATTERN = Pattern.compile("\\d+-\\d+");

    @Override
    public void run(String value) throws ValidationException {
        StringTokenizer tokenizer = new StringTokenizer(value, ",", false);
        while (tokenizer.hasMoreTokens()) {
            validateResponseCodeOrRange(tokenizer.nextToken().replace(" ", ""));
        }
    }

    private void validateResponseCodeOrRange(String responseCodeOrRange) throws ValidationException {
        try {
            if (RESPONSE_CODE_PATTERN.matcher(responseCodeOrRange).matches()) {
                Integer.parseInt(responseCodeOrRange);
                return;
            }

            if (RESPONSE_CODE_RANGE_PATTERN.matcher(responseCodeOrRange).matches()) {
                int hyphenPositon = responseCodeOrRange.indexOf('-');
                int lowerRange = Integer.parseInt(responseCodeOrRange.substring(0, hyphenPositon));
                int upperRange = Integer.parseInt(responseCodeOrRange.substring(hyphenPositon + 1, responseCodeOrRange.length()));
                if (lowerRange >= upperRange) {
                    throw new NumberFormatException();
                }
                return;
            }

            throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new ValidationException($("Enter codes in the following format: int[ - toInt] [, int[ - toInt]] ..."));
        }
    }
}