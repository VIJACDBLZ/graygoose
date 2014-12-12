package com.codeforces.graygoose.validation;

import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 08.11.12
 */
public class UrlValidator extends Validator {
    private static final UrlValidator INSTANCE = new UrlValidator();

    @Override
    public void run(String value) throws ValidationException {
        try {
            URL url = new URL(value);
            if (!"http".equalsIgnoreCase(url.getProtocol())) {
                throw new ValidationException($("Only http protocol is supported"));
            }
        } catch (MalformedURLException ignored) {
            throw new ValidationException($("Enter valid URL"));
        }
    }

    public static UrlValidator getInstance() {
        return INSTANCE;
    }
}
