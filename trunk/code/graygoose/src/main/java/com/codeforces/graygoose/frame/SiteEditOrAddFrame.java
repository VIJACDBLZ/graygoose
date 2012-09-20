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
import com.codeforces.graygoose.util.DateFormatter;
import com.google.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.validation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SiteEditOrAddFrame extends ApplicationFrame {
    public static final Validator timeValidator = new Validator() {
        @Override
        public void run(String value) throws ValidationException {
            if (!StringUtils.isBlank(value) && !value.matches("^\\s*([0-1]?[0-9]|2[0-4])\\s*:\\s*([0-5][0-9])\\s*$")) {
                throw new ValidationException($("Enter valid data."));
            }
        }
    };

    @Parameter
    private Long id;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String name;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String url;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String pauseFromMinute;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String pauseToMinute;

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

    private final DateFormatter dateFormatter = new DateFormatter();

    public void setup(long id, Class<? extends WebPage> redirectPageClass) {
        this.id = id;
        this.redirectPageClass = redirectPageClass;
    }

    @SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
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
            put("rescanPeriod", String.valueOf(site.getRescanPeriodSeconds()));

            put("pauseFromMinute", site.getPauseFromMinute() == null ? ""
                    : dateFormatter.longToStringHHMM(site.getPauseFromMinute()));
            put("pauseToMinute", site.getPauseToMinute() == null ? ""
                    : dateFormatter.longToStringHHMM(site.getPauseToMinute()));

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
                            String.valueOf(alert.getId()) + '#' + rule.getId(),
                            String.valueOf(ruleAlertRelation.getMaxConsecutiveFailCount()));
                }

                alertsByRuleId.put(String.valueOf(rule.getId()), alerts);
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

        addValidator("pauseFromMinute", timeValidator);
        addValidator("pauseToMinute", timeValidator);

        final String fromMinute = getString("pauseFromMinute");
        addValidator("pauseToMinute", new Validator() {
            @Override
            public void run(String toMinute) throws ValidationException {
                if(StringUtils.isBlank(fromMinute) ^ StringUtils.isBlank(toMinute)){
                    throw new ValidationException($("Pause fields must be both empty or both filled."));
                }
                if(StringUtils.isBlank(fromMinute)){
                    return;
                }

                //checking that this is correct period

                long fromMinuteValue = dateFormatter.stringHHMMToLong(fromMinute);
                long toMinuteValue = dateFormatter.stringHHMMToLong(toMinute);

                if (fromMinuteValue == toMinuteValue) {
                    throw new ValidationException($("Pause to must not be equal to Pause from."));
                }
            }
        });

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
            site.setPauseFromMinute(StringUtils.isBlank(pauseFromMinute) ? null
                    : dateFormatter.stringHHMMToLong(pauseFromMinute));
            site.setPauseToMinute(StringUtils.isBlank(pauseToMinute) ? null
                    : dateFormatter.stringHHMMToLong(pauseToMinute));
            siteDao.update(site);
            setMessage($("Site has been updated."));
        } else {
            setMessage($("Can't find site to update."));
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        //siteDao.insert(new Site(name, url, rescanPeriod));
        siteDao.insert(new Site(name,
                url,
                rescanPeriod,
                StringUtils.isBlank(pauseFromMinute) ? null : dateFormatter.stringHHMMToLong(pauseFromMinute),
                StringUtils.isBlank(pauseToMinute) ? null : dateFormatter.stringHHMMToLong(pauseToMinute)));
        setMessage($("Site has been added."));
        abortWithRedirect(redirectPageClass);
    }
}
