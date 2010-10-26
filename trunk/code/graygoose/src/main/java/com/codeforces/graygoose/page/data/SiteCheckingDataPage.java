package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.util.FetchUtil;
import com.codeforces.graygoose.util.ResponseChecker;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.link.Link;

import java.io.IOException;
import java.util.Arrays;


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
        //
    }

    @Action("fetch")
    public void onFetch() {
        try {
            ResponseChecker.Response response = FetchUtil.fetchUrl(getString("url"));

            put("responseCode", response.getCode());
            put("responseText", response.getText());

            put("success", true);
        } catch (IOException e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error", "responseCode", "responseText");
    }

    @Action("checkRule")
    public void onCheckRule() {
        try {
            ResponseChecker.Response response = new ResponseChecker.Response(
                    getString("url"), getInteger("responseCode"), getString("responseText"));

            String errorMessage = ResponseChecker.getErrorMessage(response, Arrays.asList(
                    ruleDao.find(getLong("ruleId"))
            ));

            if (errorMessage == null) {
                put("success", true);
            } else {
                put("error", errorMessage);
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Override
    public void action() {
        // No operations.
    }
}