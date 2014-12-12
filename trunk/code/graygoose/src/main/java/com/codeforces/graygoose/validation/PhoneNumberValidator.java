package com.codeforces.graygoose.validation;

import com.codeforces.commons.text.StringUtil;
import com.codeforces.graygoose.util.SmsUtil;
import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 12.12.14
 */
public class PhoneNumberValidator extends Validator {
    @Override
    public void run(String value) throws ValidationException {
        if (StringUtil.isBlank(value)) {
            throw new ValidationException($("Field should not be empty"));
        }

        if (!SmsUtil.checkPhone(value)) {
            throw new ValidationException($("Field should contain a valid phone number"));
        }
    }
}
