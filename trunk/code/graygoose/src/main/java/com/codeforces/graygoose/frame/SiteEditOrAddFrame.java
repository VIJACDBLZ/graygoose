package com.codeforces.graygoose.frame;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.dao.RuleDao;
import com.codeforces.graygoose.dao.SiteDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.Rule;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.codeforces.graygoose.model.Site;
import com.codeforces.graygoose.page.web.WebPage;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.validation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class SiteEditOrAddFrame extends ApplicationFrame {
    @Parameter
    private Long id;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String name;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String url;

    @Parameter
    private int rescanPeriod;

    private Site site;
    private Class<? extends WebPage> redirectPageClass;

    @Inject
    private SiteDao siteDao;

    @Inject
    private RuleDao ruleDao;

    @Inject
    private AlertDao alertDao;

    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    public void setup(long id, Class<? extends WebPage> redirectPageClass) {
        this.id = id;
        this.redirectPageClass = redirectPageClass;
    }

    public void setup(Class<? extends WebPage> redirectPageClass) {
        this.redirectPageClass = redirectPageClass;
    }

    @Override
    public void initializeAction() {
        addJs("js/jquery.smartmodal.js");
        super.initializeAction();

        if (id != null) {
            site = siteDao.find(id);
            put("id", site.getId());
            put("name", site.getName());
            put("url", site.getUrl());
            put("rescanPeriod", "" + site.getRescanPeriodSeconds());

            put("ruleTypes", Rule.RuleType.values());

            List<Rule> rules = ruleDao.findAllBySite(id);
            put("rules", rules);

            Map<String, List<Alert>> alertsByRuleId = new TreeMap<String, List<Alert>>();
            Map<String, String> failCountByAlertIdAndRuleIdConcatenation = new HashMap<String, String>();

            for (Rule rule : rules) {
                List<RuleAlertRelation> ruleAlertRelations = ruleAlertRelationDao.findAllByRule(rule.getId());
                List<Alert> alerts = new ArrayList<Alert>(ruleAlertRelations.size());

                for (RuleAlertRelation ruleAlertRelation : ruleAlertRelations) {
                    Alert alert = alertDao.find(ruleAlertRelation.getAlertId());
                    alerts.add(alert);
                    failCountByAlertIdAndRuleIdConcatenation.put(
                            "" + alert.getId() + "#" + rule.getId(),
                            "" + ruleAlertRelation.getMaxConsecutiveFailCount());
                }

                alertsByRuleId.put("" + rule.getId(), alerts);
            }

            put("alertsByRuleId", alertsByRuleId);
            put("alerts", alertDao.findAll());
            put("failCountByAlertIdAndRuleIdConcatenation", failCountByAlertIdAndRuleIdConcatenation);

            put("edit", true);
        } else {
            put("edit", false);
        }
    }

    @Override
    public void action() {
        // No operations.
    }

    private boolean validateEditOrAdd() {
        addValidator("name", new RequiredValidator());
        addValidator("name", new LengthValidator(1, 100));

        addValidator("url", new RequiredValidator());
        addValidator("url", new LengthValidator(1, 200));
        addValidator("url", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                try {
                    String protocol = new URL(value).getProtocol();
                    if (!"http".equalsIgnoreCase(protocol)) {
                        throw new ValidationException($("Only http protocol is supported."));
                    }
                } catch (MalformedURLException e) {
                    throw new ValidationException($("Enter valid URL."));
                }
            }
        });

        addValidator("rescanPeriod", new RequiredValidator());
        addValidator("rescanPeriod", new OptionValidator("60", "120", "300", "600"));

        return runValidation();
    }

    @Validate("add")
    public boolean validateAdd() {
        return validateEditOrAdd();
    }

    @Validate("edit")
    public boolean validateEdit() {
        return validateEditOrAdd();
    }

    @Action("edit")
    public void onEdit() {
        if (site != null) {
            site.setName(name);
            site.setRescanPeriodSeconds(rescanPeriod);
            site.setUrl(url);

            siteDao.update(site);
            setMessage($("Site has been updated."));
        } else {
            setMessage($("Can't find site to update."));
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        siteDao.insert(new Site(name, url, rescanPeriod));
        setMessage($("Site has been added."));
        abortWithRedirect(redirectPageClass);
    }
}
