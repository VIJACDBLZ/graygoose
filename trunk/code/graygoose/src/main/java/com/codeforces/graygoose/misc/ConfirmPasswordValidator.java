package com.codeforces.graygoose.misc;

import org.nocturne.validation.Validator;
import org.nocturne.validation.ValidationException;

public class ConfirmPasswordValidator extends Validator {

    private String password;

    public ConfirmPasswordValidator(String password) {
        this.password = password;
    }

    @Override
    public void run(String value) throws ValidationException {
        if (password == null && value == null || password != null && password.equals(value)) {
            return;
        }

        throw new ValidationException($("Password confirmation was failed."));
    }
}
