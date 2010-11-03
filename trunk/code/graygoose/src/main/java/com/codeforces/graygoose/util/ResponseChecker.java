package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;

import java.util.*;
import java.util.regex.Pattern;

public class ResponseChecker {
    private static final Map<String, Pattern> compiledPatternByRegexString =
            new Hashtable<String, Pattern>();

    private static final Map<String, Set<Integer>> responseCodesByCodesString =
            new Hashtable<String, Set<Integer>>();

    public static String getErrorMessage(Response response, Collection<Rule> rules) {
        for (Rule rule : rules) {
            String errorMessage = getErrorMessage(response, rule);
            if (errorMessage != null) {
                return errorMessage;
            }
        }

        return null;
    }

    public static String getErrorMessage(Response response, Rule rule) {
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
        return "Site " + response.getSiteUrl() + "; " + rule.toString();
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