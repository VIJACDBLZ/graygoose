package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Response;
import com.codeforces.graygoose.model.Rule;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ResponseCheckingService {
    private static final Logger logger = Logger.getLogger(ResponseCheckingService.class);

    private static final Map<String, Pattern> compiledPatternByRegexString =
            new ConcurrentHashMap<String, Pattern>();

    private static final Map<String, Set<Integer>> responseCodesByCodesString =
            new ConcurrentHashMap<String, Set<Integer>>();

    public static String getErrorMessage(String siteName, Response response, Rule rule) {
        logger.info("Check response from URL [" + response.getSiteUrl()
                + "] for matching the rule [" + rule.toShortString() + "].");

        switch (rule.getRuleType()) {
            case RESPONSE_CODE_RULE_TYPE:
                if (!checkResponseCode(response, rule)) {
                    return getFormattedErrorString(siteName, rule);
                }
                break;
            case SUBSTRING_RULE_TYPE:
                if (!checkSubstringCount(response, rule)) {
                    return getFormattedErrorString(siteName, rule);
                }
                break;
            case REGEX_RULE_TYPE:
                if (!checkRegexMatch(response, rule)) {
                    return getFormattedErrorString(siteName, rule);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported rule type.");
        }

        return null;
    }

    private static String getFormattedErrorString(String siteName, Rule rule) {
        return siteName + ": " + rule.toShortString();
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
        String text = response.getText().getValue();
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

        return pattern.matcher(response.getText().getValue()).matches();
    }

    private ResponseCheckingService() {
    }
}