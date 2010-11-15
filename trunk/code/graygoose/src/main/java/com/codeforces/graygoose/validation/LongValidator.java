package com.codeforces.graygoose.validation;

import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

public class LongValidator extends Validator {
    /**
     * Minimal long integer value.
     */
    private long minimalValue = Long.MIN_VALUE;

    /**
     * Maximal long integer value.
     */
    private long maximalValue = Long.MAX_VALUE;

    /**
     * Long integer validator which doesn't check range.
     */
    public LongValidator() {
    }

    /**
     * Checks that given value is in the range [minimalValue, maximalValue].
     *
     * @param minimalValue min value.
     * @param maximalValue max value.
     */
    public LongValidator(long minimalValue, long maximalValue) {
        this.minimalValue = minimalValue;
        this.maximalValue = maximalValue;
    }

    /**
     * @param value Value to be analyzed.
     * @throws org.nocturne.validation.ValidationException
     *          On validation error. It is good idea to pass
     *          localized via captions value inside ValidationException,
     *          like {@code return new ValidationException($("Field can't be empty"));}.
     */
    @Override
    public void run(String value) throws ValidationException {
        if (value == null || !value.matches("[-0-9]+")) {
            throw new ValidationException($("Field should contain long integer value"));
        }

        long numeric;

        try {
            numeric = Long.parseLong(value);
        } catch (Exception e) {
            throw new ValidationException($("Field should contain long integer value"));
        }

        if (numeric < minimalValue) {
            throw new ValidationException($("Field should be at least {0}", minimalValue));
        }

        if (numeric > maximalValue) {
            throw new ValidationException($("Field should be no more than {0}", maximalValue));
        }
    }
}