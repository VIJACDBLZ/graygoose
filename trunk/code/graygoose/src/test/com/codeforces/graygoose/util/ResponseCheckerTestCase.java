package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.util.ResponseChecker.Response;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

public class ResponseCheckerTestCase extends TestCase {

    public void testResponseCode() {
        Rule rule = new Rule(0, Rule.RuleType.RESPONSE_CODE_RULE_TYPE, null);

        SortedMap<String, String> properties = new TreeMap<String, String>();
        properties.put("expectedCodes", "200, 300, 400-500");
        rule.setData(properties);

        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(200, ""), Arrays.asList(rule)));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(300, ""), Arrays.asList(rule)));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(400, ""), Arrays.asList(rule)));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(500, ""), Arrays.asList(rule)));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(450, ""), Arrays.asList(rule)));

        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(501, ""), Arrays.asList(rule)));
        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(399, ""), Arrays.asList(rule)));
        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(100, ""), Arrays.asList(rule)));
    }

    public void testSubstringCount() {
        Rule rule = new Rule(0, Rule.RuleType.SUBSTRING_RULE_TYPE, null);

        SortedMap<String, String> properties = new TreeMap<String, String>();
        properties.put("expectedSubstring", "abc");
        properties.put("expectedSubstringMinimalCount", "1");
        properties.put("expectedSubstringMaximalCount", "3");
        rule.setData(properties);

        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abc"), Arrays.asList(rule)));
        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abc abc"), Arrays.asList(rule)));
        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abc abc abc"), Arrays.asList(rule)));

        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abz"), Arrays.asList(rule)));
        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abc abc abc abc"), Arrays.asList(rule)));

        properties = new TreeMap<String, String>();
        properties.put("expectedSubstring", "abc");
        properties.put("expectedSubstringMinimalCount", "0");
        properties.put("expectedSubstringMaximalCount", "0");
        rule.setData(properties);

        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "asdfghjkl"), Arrays.asList(rule)));

        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(0, "abc"), Arrays.asList(rule)));
    }

    public void testRegexMatch() {
        Rule rule = new Rule(0, Rule.RuleType.REGEX_RULE_TYPE, null);

        SortedMap<String, String> properties = new TreeMap<String, String>();
        properties.put("expectedRegex", "\\d+");
        rule.setData(properties);

        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, "0123456789"), Arrays.asList(rule)));
        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, "1024"), Arrays.asList(rule)));

        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, ""), Arrays.asList(rule)));
        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, "1024x"), Arrays.asList(rule)));

        properties = new TreeMap<String, String>();
        properties.put("expectedRegex", "\\d*");
        rule.setData(properties);

        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, "1024"), Arrays.asList(rule)));
        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, ""), Arrays.asList(rule)));

        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(0, " "), Arrays.asList(rule)));
    }
}