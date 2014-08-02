package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Response;
import com.codeforces.graygoose.model.Rule;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseCheckingService {
    private static final Logger logger = Logger.getLogger(ResponseCheckingService.class);

    private static final ConcurrentMap<String, Pattern> compiledPatternByRegexString = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Set<Integer>> responseCodesByCodesString = new ConcurrentHashMap<>();

    public static String getErrorMessageOrNull(String siteName, Response response, Rule rule) {
        logger.info("Check response from URL [" + response.getSiteUrl()
                + "] for matching the rule [" + rule.toShortString() + "].");

        return hasError(response, rule) ? getFormattedErrorString(siteName, rule) : null;
    }

    private static boolean hasError(Response response, Rule rule) {
        switch (rule.getRuleType()) {
            case RESPONSE_CODE_RULE_TYPE:
                return !checkResponseCode(response, rule);
            case SUBSTRING_RULE_TYPE:
                return !checkSubstringCount(response, rule);
            case REGEX_MATCH_RULE_TYPE:
                return !checkRegexMatch(response, rule);
            case REGEX_NOT_MATCH_RULE_TYPE:
                return !checkRegexNotMatch(response, rule);
            case REGEX_FIND_RULE_TYPE:
                return !checkRegexCount(response, rule);
            default:
                throw new UnsupportedOperationException("Unsupported rule type.");
        }
    }

    private static String getFormattedErrorString(String siteName, Rule rule) {
        return siteName + ": " + rule.toShortString();
    }

    private static boolean checkResponseCode(Response response, Rule rule) {
        String codesString = rule.getProperty("codes");
        Set<Integer> responseCodes = responseCodesByCodesString.get(codesString);

        if (responseCodes == null) {
            responseCodes = getResponseCodesByCodesString(codesString);
            responseCodesByCodesString.put(codesString, responseCodes);
        }

        return responseCodes.contains(response.getCode());
    }

    private static Set<Integer> getResponseCodesByCodesString(String codesString) {
        Set<Integer> responseCodes = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(codesString, ",", false);

        while (tokenizer.hasMoreTokens()) {
            String codeOrRange = tokenizer.nextToken().replace(" ", "");

            int hyphenPosition = codeOrRange.indexOf('-');

            if (hyphenPosition == -1) {
                responseCodes.add(Integer.parseInt(codeOrRange));
            } else {
                int lowerRange = Integer.parseInt(codeOrRange.substring(0, hyphenPosition));
                int upperRange = Integer.parseInt(codeOrRange.substring(hyphenPosition + 1, codeOrRange.length()));

                for (int currentCode = lowerRange; currentCode <= upperRange; ++currentCode) {
                    responseCodes.add(currentCode);
                }
            }
        }

        return responseCodes;
    }

    private static boolean checkSubstringCount(Response response, Rule rule) {
        //String text = response.getText().getValue(); ??
        String text = response.getText();

        String substring = rule.getProperty("substring");

        int matchCount = 0;
        int position = 0;

        while ((position = text.indexOf(substring, position)) >= 0) {
            ++matchCount;
            position += substring.length();
        }

        return rule.getPropertyAsInteger("substringMinCount") <= matchCount
                && matchCount <= rule.getPropertyAsInteger("substringMaxCount");
    }

    private static boolean checkRegexMatch(Response response, Rule rule) {
        String regexString = rule.getProperty("matchPattern");
        Pattern pattern = compiledPatternByRegexString.get(regexString);

        if (pattern == null) {
            pattern = Pattern.compile(regexString);
            compiledPatternByRegexString.putIfAbsent(regexString, pattern);
        }

        //??
        return pattern.matcher(response.getText()).matches();
    }

    private static boolean checkRegexNotMatch(Response response, Rule rule) {
        String regexString = rule.getProperty("notMatchPattern");
        Pattern pattern = compiledPatternByRegexString.get(regexString);

        if (pattern == null) {
            pattern = Pattern.compile(regexString);
            compiledPatternByRegexString.putIfAbsent(regexString, pattern);
        }

        //??
        return !pattern.matcher(response.getText()).matches();
    }

    private static boolean checkRegexCount(Response response, Rule rule) {
        String regexString = rule.getProperty("findPattern");
        Pattern pattern = compiledPatternByRegexString.get(regexString);

        if (pattern == null) {
            pattern = Pattern.compile(regexString);
            compiledPatternByRegexString.putIfAbsent(regexString, pattern);
        }

        //??
        Matcher matcher = pattern.matcher(response.getText());
        int matchCount = 0;

        while (matcher.find()) {
            ++matchCount;
        }

        return rule.getPropertyAsInteger("patternMinCount") <= matchCount
                && matchCount <= rule.getPropertyAsInteger("patternMaxCount");
    }

    private ResponseCheckingService() {
        throw new UnsupportedOperationException();
    }
}