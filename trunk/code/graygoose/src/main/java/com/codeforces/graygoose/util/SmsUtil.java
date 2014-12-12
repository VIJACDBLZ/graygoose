package com.codeforces.graygoose.util;

import com.codeforces.commons.io.http.HttpMethod;
import com.codeforces.commons.io.http.HttpResponse;
import com.codeforces.commons.io.http.HttpUtil;
import com.codeforces.commons.text.StringUtil;
import com.codeforces.graygoose.exception.ConfigurationException;
import com.codeforces.graygoose.model.Alert;
import org.apache.log4j.Logger;

import java.util.regex.Pattern;

/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 09.12.14
 */
public class SmsUtil {
    private static final Logger logger = Logger.getLogger(SmsUtil.class);

    private static final Pattern PHONE_NORMALIZATION_PATTERN = Pattern.compile("[\\(\\)\\-\\+\\s]+");
    private static final Pattern PHONE_VERIFICATION_PATTERN = Pattern.compile("7[0-9]{10}");

    public static void send(Alert alert, String body) {
        logger.info("Sending SMS to '" + alert.getSmsServicePhone() + "'.");

        String url = alert.getSmsServiceUrl();
        if (StringUtil.isBlank(url)) {
            throw new ConfigurationException("URL of the SMS service is blank.");
        }

        String phoneParameterName = alert.getSmsServicePhoneParameterName();
        if (StringUtil.isBlank(phoneParameterName)) {
            throw new ConfigurationException("Phone parameter name of the SMS service is blank.");
        }

        String messageParameterName = alert.getSmsServiceMessageParameterName();
        if (StringUtil.isBlank(messageParameterName)) {
            throw new ConfigurationException("Message parameter name of the SMS service is blank.");
        }

        HttpResponse response = HttpUtil.newRequest(url)
                .setMethod(HttpMethod.POST)
                .setTimeoutMillis(10000)
                .appendParameter(phoneParameterName, getPhone(alert))
                .appendParameter(messageParameterName, body)
                .executeAndReturnResponse();

        if (response.getCode() == 200 && response.getBytes() != null && response.getUtf8String().startsWith("100\n")) {
            logger.info(String.format(
                    "Successfully sent SMS to '%s'.", alert.getSmsServicePhone()
            ));
        } else {
            String message = String.format(
                    "Received unexpected %s while sending SMS to '%s'.", response, alert.getSmsServicePhone()
            );

            if (response.hasIoException()) {
                logger.error(message, response.getIoException());
                throw new SmsException(message, response.getIoException());
            } else {
                logger.error(message);
                throw new SmsException(message);
            }
        }
    }

    public static boolean checkPhone(String phone) {
        return PHONE_VERIFICATION_PATTERN.matcher(normalizePhone(phone)).matches();
    }

    private static String getPhone(Alert alert) {
        String phone = normalizePhone(alert.getSmsServicePhone());

        if (!PHONE_VERIFICATION_PATTERN.matcher(phone).matches()) {
            logger.error("Phone '" + phone + "' verification failed for " + alert + '.');
        }

        return phone;
    }

    private static String normalizePhone(String phone) {
        return PHONE_NORMALIZATION_PATTERN.matcher(phone).replaceAll("");
    }

    private SmsUtil() {
        throw new UnsupportedOperationException();
    }
}
