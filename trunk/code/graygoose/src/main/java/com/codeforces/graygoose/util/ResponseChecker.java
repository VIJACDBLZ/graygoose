package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseChecker {
    public static String getErrorMessage(Response response, Collection<Rule> rules) {
        for (Rule rule : rules) {
            switch (rule.getRuleType()) {
                case RESPONSE_CODE_RULE_TYPE:
                    if (!checkResponseCode(response, rule)) {
                        return rule.toString() + " fails.";
                    }
                    break;
                case SUBSTRING_RULE_TYPE:
                    if (!checkSubstringCount(response, rule)) {
                        return rule.toString() + " fails.";
                    }
                    break;
                case REGEX_RULE_TYPE:
                    if (!checkRegexMatch(response, rule)) {
                        return rule.toString() + " fails.";
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported rule type.");
            }
        }

        return null;
    }

    private static boolean checkResponseCode(Response response, Rule rule) {
        int code = response.getCode();
        StringTokenizer tokenizer = new StringTokenizer(rule.getProperty("expectedCodes"), ",", false);

        while (tokenizer.hasMoreTokens()) {
            String codeOrRange = tokenizer.nextToken().replace(" ", "");

            int hyphenPositon = codeOrRange.indexOf('-');

            if (hyphenPositon == -1) {
                if (code == Integer.parseInt(codeOrRange)) {
                    return true;
                }
            } else {
                int lowerRange = Integer.parseInt(codeOrRange.substring(0, hyphenPositon));
                int upperRange = Integer.parseInt(codeOrRange.substring(hyphenPositon + 1, codeOrRange.length()));

                if (code >= lowerRange && code <= upperRange) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean checkSubstringCount(Response response, Rule rule) {
        Pattern pattern = Pattern.compile(rule.getProperty("expectedSubstring"));
        Matcher matcher = pattern.matcher(response.getText());
        int count = 0;

        while (matcher.find()) {
            ++count;
        }

        return count >= rule.getPropertyAsInteger("expectedSubstringMinimalCount")
                && count <= rule.getPropertyAsInteger("expectedSubstringMaximalCount");
    }

    private static boolean checkRegexMatch(Response response, Rule rule) {
        return Pattern
                .compile(rule.getProperty("expectedRegex"))
                .matcher(response.getText())
                .matches();
    }

    public static class Response {

        private final int code;
        private final String text;

        public Response(int code, String text) {
            this.code = code;
            this.text = text;
        }

        public int getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }
}