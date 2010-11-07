package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;

public class ResponseCheckingService {
    private static final Logger logger = Logger.getLogger(ResponseCheckingService.class);

    private static final Map<String, Pattern> compiledPatternByRegexString =
            new Hashtable<String, Pattern>();

    private static final Map<String, Set<Integer>> responseCodesByCodesString =
            new Hashtable<String, Set<Integer>>();

    public static String getErrorMessage(Response response, Rule rule) {
        logger.info("Check response from URL [" + response.getSiteUrl()
                + "] for matching the rule [" + rule.toShortString() + "].");

        switch (rule.getRuleType()) {
            case RESPONSE_CODE_RULE_TYPE:
                if (!checkResponseCode(response, rule)) {
                    return getFormattedErrorString(response, rule);
                }
                break;
            case SUBSTRING_RULE_TYPE:
                if (!checkSubstringCount(response, rule)) {
                    return getFormattedErrorString(response, rule);
                }
                break;
            case REGEX_RULE_TYPE:
                if (!checkRegexMatch(response, rule)) {
                    return getFormattedErrorString(response, rule);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported rule type.");
        }

        return null;
    }

    private static String getFormattedErrorString(Response response, Rule rule) {
        return "Site " + response.getSiteUrl() + "; " + rule.toShortString();
    }

    private static boolean checkResponseCode(Response response, Rule rule) {
        String codesString = rule.getProperty("expectedCodes");
        Set<Integer> responseCodes = responseCodesByCodesString.get(codesString);

        if (responseCodes == null) {
            responseCodes = getResponseCodesByCodesString(codesString);
            responseCodesByCodesString.put(codesString, responseCodes);
        }

        return responseCodes.contains(response.getCode());
    }

    private static Set<Integer> getResponseCodesByCodesString(String codesString) {
        Set<Integer> responseCodes = new HashSet<Integer>();
        StringTokenizer tokenizer = new StringTokenizer(codesString, ",", false);

        while (tokenizer.hasMoreTokens()) {
            String codeOrRange = tokenizer.nextToken().replace(" ", "");

            int hyphenPositon = codeOrRange.indexOf('-');

            if (hyphenPositon == -1) {
                responseCodes.add(Integer.parseInt(codeOrRange));
            } else {
                int lowerRange = Integer.parseInt(codeOrRange.substring(0, hyphenPositon));
                int upperRange = Integer.parseInt(codeOrRange.substring(hyphenPositon + 1, codeOrRange.length()));

                for (int currentCode = lowerRange; currentCode <= upperRange; ++currentCode) {
                    responseCodes.add(currentCode);
                }
            }
        }

        return responseCodes;
    }

    private static boolean checkSubstringCount(Response response, Rule rule) {
        String text = response.getText();
        String substring = rule.getProperty("expectedSubstring");

        int matchCount = 0;
        int position = 0;

        while ((position = text.indexOf(substring, position)) >= 0) {
            ++matchCount;
            position += substring.length();
        }

        return rule.getPropertyAsInteger("expectedSubstringMinimalCount") <= matchCount
                && matchCount <= rule.getPropertyAsInteger("expectedSubstringMaximalCount");
    }

    private static boolean checkRegexMatch(Response response, Rule rule) {
        String regexString = rule.getProperty("expectedRegex");
        Pattern pattern = compiledPatternByRegexString.get(regexString);

        if (pattern == null) {
            pattern = Pattern.compile(regexString);
            compiledPatternByRegexString.put(regexString, pattern);
        }

        return pattern.matcher(response.getText()).matches();
    }

    public static class Response {
        private final String siteUrl;
        private final int code;
        private final String text;

        /**
         * Constructs a <code>ResponseCheckingService.Response</code> with specified parameters.
         *
         * @param siteUrl URL of the site from which came the HTTP-response
         * @param code    response code or <code>-1</code> if no HTTP-response came within specified timeout
         *                or any other exception has been thrown during URL fetching
         * @param text    body of the HTTP-response in case of success,
         *                empty string or <code>exception.getMessage()</code>, in case of failure
         */
        public Response(String siteUrl, int code, String text) {
            this.siteUrl = siteUrl;
            this.code = code;
            this.text = text;
        }

        public String getSiteUrl() {
            return siteUrl;
        }

        public int getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
}