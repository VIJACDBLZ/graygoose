package com.codeforces.graygoose.validation;

import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

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
