package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.model.Alert;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

@Link("data/alerts")
public class AlertsDataPage extends DataPage {
    @Parameter
    private Long alertId;

    private Alert alert;

    @Inject
    private AlertDao alertDao;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (alertId != null) {
            alert = alertDao.find(alertId);
        }
    }

    @Action("deleteAlert")
    public void onDeleteAlert() {
        try {
            if (alert != null) {
                alertDao.delete(alert);
                put("success", true);

                setMessage($("Alert has been deleted"));
            } else {
                put("error", $("No such alert"));
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