package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.util.MailUtil;
import com.codeforces.graygoose.util.SmsUtil;
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
                alertDao.markDeleted(alert);
                put("success", true);

                setMessage($("Alert has been deleted."));
            } else {
                put("error", $("No such alert."));
            }
        } catch (Exception e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Action("testAlert")
    public void onTestAlert() {
        try {
            if (alert != null) {
                if ("E-mail".equals(alert.getType())) {
                    if (!MailUtil.sendMail(alert.getEmail(), "GrayGoose test alert: " + alert.getName(), "")) {
                        throw new RuntimeException($("E-mail was not sent for an unknown reason."));
                    }
                } else if ("Google calendar event".equals(alert.getType())) {
                    SmsUtil.send("GrayGoose test alert: " + alert.getName(), "", alert.getEmail(), alert.getPassword());
                } else {
                    throw new UnsupportedOperationException($("Unsupported alert type."));
                }

                put("success", true);
            } else {
                put("error", $("No such alert."));
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