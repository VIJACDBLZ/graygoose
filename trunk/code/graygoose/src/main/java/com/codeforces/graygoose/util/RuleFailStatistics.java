package com.codeforces.graygoose.util;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleFailStatistics {

    private static final long MILLIS_PER_HOUR = 60 * 60 * 1000;

    private static final Map<Long, Long> failCountByRuleId = new HashMap<Long, Long>();

    public static synchronized void increaseRuleFailCount(long ruleId) {
        Long currentFailCount = failCountByRuleId.get(ruleId);
        failCountByRuleId.put(ruleId, currentFailCount == null ? 1L : currentFailCount + 1L);
    }

    public static synchronized void resetFailCount(long ruleId) {
        failCountByRuleId.put(ruleId, 0L);
    }

    public static synchronized List<Alert> getNeededAlerts(long ruleId,
                                                           RuleAlertRelationDao ruleAlertRelationDao,
                                                           AlertDao alertDao,
                                                           AlertTriggerEventDao alertTriggerEventDao) {
        List<Alert> neededAlerts = new ArrayList<Alert>();
        Long currentFailCount = failCountByRuleId.get(ruleId);

        if (currentFailCount == null || currentFailCount == 0) {
            return neededAlerts;
        }

        List<RuleAlertRelation> ruleAlertRelations = ruleAlertRelationDao.findByRule(ruleId);
        long currentTimeMillis = System.currentTimeMillis();

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelations) {
            if (ruleAlertRelation.getMaxConsecutiveFailCount() <= currentFailCount) {
                Alert alert = alertDao.find(ruleAlertRelation.getAlertId());

                if (alert != null) {
                    List<AlertTriggerEvent> alertTriggersForLastHour =
                            alertTriggerEventDao.findByAlertForPeriod(
                                    alert, currentTimeMillis - MILLIS_PER_HOUR, currentTimeMillis);
                    if (alertTriggersForLastHour.size() < alert.getMaxAlertCountPerHour()) {
                        neededAlerts.add(alert);
                    }
                }
            }
        }

        return neededAlerts;
    }

    protected RuleFailStatistics() {
    }
}
