package com.codeforces.graygoose.util;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.dao.AlertTriggerEventDao;
import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.misc.TimeConstants;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.model.AlertTriggerEvent;
import com.codeforces.graygoose.model.RuleAlertRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleFailStatistics {
    private static final Map<Long, Long> consecutiveFailCountByRuleId = new HashMap<Long, Long>();

    public static synchronized void increaseConsecutiveFailCountByRuleId(long ruleId) {
        Long currentFailCount = consecutiveFailCountByRuleId.get(ruleId);
        consecutiveFailCountByRuleId.put(ruleId, currentFailCount == null ? 1L : currentFailCount + 1L);
    }

    public static synchronized void resetConsecutiveFailCountByRuleId(long ruleId) {
        consecutiveFailCountByRuleId.put(ruleId, 0L);
    }

    public static synchronized List<Alert> getTriggeredAlerts(long ruleId,
                                                              RuleAlertRelationDao ruleAlertRelationDao,
                                                              AlertDao alertDao,
                                                              AlertTriggerEventDao alertTriggerEventDao) {
        List<Alert> neededAlerts = new ArrayList<Alert>();
        Long currentConsecutiveFailCount = consecutiveFailCountByRuleId.get(ruleId);

        if (currentConsecutiveFailCount == null || currentConsecutiveFailCount == 0) {
            return neededAlerts;
        }

        List<RuleAlertRelation> ruleAlertRelations = ruleAlertRelationDao.findByRule(ruleId);
        long currentTimeMillis = System.currentTimeMillis();

        for (RuleAlertRelation ruleAlertRelation : ruleAlertRelations) {
            if (ruleAlertRelation.getMaxConsecutiveFailCount() <= currentConsecutiveFailCount) {
                Alert alert = alertDao.find(ruleAlertRelation.getAlertId());

                if (alert != null) {
                    List<AlertTriggerEvent> alertTriggersForLastHour =
                            alertTriggerEventDao.findByAlertForPeriod(
                                    alert.getId(), currentTimeMillis - TimeConstants.MILLIS_PER_HOUR, currentTimeMillis);
                    if (alertTriggersForLastHour.size() < alert.getMaxAlertCountPerHour()) {
                        neededAlerts.add(alert);
                    }
                }
            }
        }

        return neededAlerts;
    }

    private RuleFailStatistics() {
    }
}
