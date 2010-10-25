package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.util.ResponseChecker;
import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


@Link("data/checkSites")
public class SiteCheckingDataPage extends DataPage {
    @Inject
    private SiteDao siteDao;

    @Inject
    private RuleDao ruleDao;

    @Override
    public void initializeAction() {
        super.initializeAction();
    }

    @Action("checkSites")
    public void onCheckSites() throws Exception {
        try {
            URL url = new URL("http://acm.sgu.ru/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String text = IOUtils.toString(reader);
            reader.close();

            ResponseChecker.Response response = new ResponseChecker.Response(
                    connection.getResponseCode(), text);

            connection.disconnect();

            List<Rule> rules = new ArrayList<Rule>();

            Rule rule = new Rule(0, Rule.RuleType.RESPONSE_CODE_RULE_TYPE, null);
            SortedMap<String, String> settings = new TreeMap<String, String>();
            settings.put("expectedCodes", "100,200,300-400");
            rule.setData(settings);

            rules.add(rule);

            String errorMessage = ResponseChecker.getErrorMessage(response, rules);
            System.out.println(errorMessage);
        } catch (MalformedURLException e) {
            //
        } catch (IOException e) {
            //
        }
        /*List<Site> sites = siteDao.findAll();
        for (Site site : sites) {
            try {
                URL url = new URL(site.getUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text = IOUtils.toString(reader);
                reader.close();

                ResponseChecker.Response response = new ResponseChecker.Response(
                        connection.getResponseCode(), text);

                connection.disconnect();

                List<Rule> rules = ruleDao.findBySite(site);
            } catch (MalformedURLException e) {
                //
            } catch (IOException e) {
                //
            }
        }*/
    }

    @Override
    public void action() {
        // No operations.
    }
}