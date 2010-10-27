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

        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 200, ""), rule));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 300, ""), rule));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 400, ""), rule));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 500, ""), rule));
        assertNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 450, ""), rule));

        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 501, ""), rule));
        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 399, ""), rule));
        assertNotNull("testResponseCode", ResponseChecker.getErrorMessage(new Response(null, 100, ""), rule));
    }

    public void testSubstringCount() {
        Rule rule = new Rule(0, Rule.RuleType.SUBSTRING_RULE_TYPE, null);

        SortedMap<String, String> properties = new TreeMap<String, String>();
        properties.put("expectedSubstring", "abc");
        properties.put("expectedSubstringMinimalCount", "1");
        properties.put("expectedSubstringMaximalCount", "3");
        rule.setData(properties);

        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abc"), rule));
        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abc abc"), rule));
        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abc abc abc"), rule));

        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abz"), rule));
        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abc abc abc abc"), rule));

        properties = new TreeMap<String, String>();
        properties.put("expectedSubstring", "abc");
        properties.put("expectedSubstringMinimalCount", "0");
        properties.put("expectedSubstringMaximalCount", "0");
        rule.setData(properties);

        assertNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "asdfghjkl"), rule));

        assertNotNull("testSubstringCount", ResponseChecker.getErrorMessage(new Response(null, 0, "abc"), rule));
    }

    public void testRegexMatch() {
        Rule rule = new Rule(0, Rule.RuleType.REGEX_RULE_TYPE, null);

        SortedMap<String, String> properties = new TreeMap<String, String>();
        properties.put("expectedRegex", "\\d+");
        rule.setData(properties);

        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, "0123456789"), rule));
        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, "1024"), rule));

        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, ""), rule));
        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, "1024x"), rule));

        properties = new TreeMap<String, String>();
        properties.put("expectedRegex", "\\d*");
        rule.setData(properties);

        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, "1024"), rule));
        assertNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, ""), rule));

        assertNotNull("testRegexMatch", ResponseChecker.getErrorMessage(new Response(null, 0, " "), rule));
    }
}