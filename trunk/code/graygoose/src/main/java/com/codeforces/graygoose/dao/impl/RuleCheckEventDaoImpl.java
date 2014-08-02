package com.codeforces.graygoose.dao.impl;

import com.codeforces.graygoose.dao.RuleCheckEventDao;
import com.codeforces.graygoose.model.RuleCheckEvent;
import org.jacuzzi.core.Row;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RuleCheckEventDaoImpl extends ApplicationDaoImpl<RuleCheckEvent> implements RuleCheckEventDao {
    @Override
    public List<RuleCheckEvent> findAllForPeriod(long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<RuleCheckEvent> findAllByRule(long ruleId) {
        return findBy("NOT deleted AND ruleId=? ORDER BY creationTime DESC", ruleId);
    }

    @Override
    public List<RuleCheckEvent> findAllByStatusForPeriod(RuleCheckEvent.Status status, long lowerBoundMillis,
                                                         long upperBoundMillis) {
        return findBy("NOT deleted AND status=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                status.toString(), new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<RuleCheckEvent> findAllBySiteForPeriod(long siteId, long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND siteId=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                siteId, new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<RuleCheckEvent> findAllByRuleForPeriod(long ruleId, long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND ruleId=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                ruleId, new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<RuleCheckEvent> findAllBySiteAndStatusForPeriod(long siteId, RuleCheckEvent.Status status,
                                                                long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND status=? AND siteId=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                status.toString(), siteId, new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<RuleCheckEvent> findAllByRuleAndStatusForPeriod(long ruleId, RuleCheckEvent.Status status,
                                                                long lowerBoundMillis, long upperBoundMillis) {
        return findBy("NOT deleted AND status=? AND ruleId=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                status.toString(), ruleId, new Date(lowerBoundMillis), new Date(upperBoundMillis));
    }

    @Override
    public List<Long> findKeysByRuleAndStatusForPeriod(long ruleId, RuleCheckEvent.Status status,
                                                       long lowerBoundMillis, long upperBoundMillis) {
        List<Row> rows = getJacuzzi().findRows(
                "SELECT id FROM RuleCheckEvent WHERE NOT deleted AND ruleId=? AND status=? AND creationTime>=? AND creationTime<=? ORDER BY creationTime DESC",
                ruleId, status.toString(), new Date(lowerBoundMillis), new Date(upperBoundMillis));

        List<Long> data = new ArrayList<>(rows.size());

        for (Row row : rows) {
            data.add((Long) row.get("id"));
        }

        return data;
    }

    @Override
    public void insert(RuleCheckEvent object) {
        super.insert(object);
    }

    @Override
    public void delete(RuleCheckEvent object) {
        super.delete(object);
    }

    @Override
    public RuleCheckEvent find(long id) {
        return super.find(id);
    }

    @Override
    public List<RuleCheckEvent> findAll() {
        return super.findAll();
    }

    @Override
    public void update(RuleCheckEvent ruleCheckEvent) {
        super.update(ruleCheckEvent);
    }
}
