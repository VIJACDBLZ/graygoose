package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.RuleAlertRelationDao;
import com.codeforces.graygoose.model.RuleAlertRelation;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.IntegerValidator;
import org.nocturne.validation.RequiredValidator;

import java.util.List;

@Link("data/ruleAlertRelations")
public class RuleAlertRelationsDataPage extends DataPage {
    @Parameter
    private Long alertId;

    @Parameter
    private Long ruleId;

    @Parameter
    private long maxConsecutiveFailCount;

    @Inject
    private RuleAlertRelationDao ruleAlertRelationDao;

    private List<RuleAlertRelation> ruleAlertRelations;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (alertId != null && ruleId != null) {
            ruleAlertRelations = ruleAlertRelationDao.findAllByRuleAndAlert(ruleId, alertId);
        }
    }

    @Validate("attachAlert")
    public boolean validateAttachAlert() {
        addValidator("ruleId", new RequiredValidator());

        addValidator("alertId", new RequiredValidator());

        addValidator("maxConsecutiveFailCount", new RequiredValidator());
        addValidator("maxConsecutiveFailCount", new IntegerValidator(1, 100));

        return runValidationAndPrintErrors();
    }

    @Action("attachAlert")
    public void onAttachAlert() {
        try {
            if (ruleAlertRelations == null || ruleAlertRelations.isEmpty()) {
                RuleAlertRelation ruleAlertRelation = new RuleAlertRelation(ruleId, alertId, maxConsecutiveFailCount);
                ruleAlertRelationDao.insert(ruleAlertRelation);

                put("success", true);
                setMessage($("Alert has been attached."));
            } else {
                put("error", String.format($("A relation between alert #%d and rule #%d is already exists."), alertId, ruleId));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("detachAlert")
    public void onDetachAlert() {
        try {
            if (ruleAlertRelations != null && !ruleAlertRelations.isEmpty()) {
                for (RuleAlertRelation ruleAlertRelation : ruleAlertRelations) {
                    ruleAlertRelationDao.delete(ruleAlertRelation);
                }

                put("success", true);
            } else {
                put("error", $("No such alert is attached to this rule."));
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