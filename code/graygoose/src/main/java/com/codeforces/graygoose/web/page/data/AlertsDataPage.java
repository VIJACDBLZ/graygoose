package com.codeforces.graygoose.web.page.data;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.util.MailUtil;
import com.codeforces.graygoose.util.SmsSendException;
import com.codeforces.graygoose.util.SmsUtil;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.link.Link;

import javax.mail.MessagingException;

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

                addMessage($("Alert has been deleted."), Noty.Type.SUCCESS);
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
                    try {
                        MailUtil.sendMail(alert.getEmail(), "GrayGoose test alert: " + alert.getName(), "...");
                    } catch (MessagingException e) {
                        throw new RuntimeException("E-mail was not sent: " + e.getMessage());
                    }
                } else if ("Google calendar event".equals(alert.getType())) {
                    try {
                        SmsUtil.send("GrayGoose test alert: " + alert.getName(), alert.getEmail(), alert.getPassword());
                    } catch (SmsSendException e) {
                        throw new RuntimeException("Can't add Google calendar event: " + e.getMessage());
                    }
                } else {
                    throw new UnsupportedOperationException($("Unsupported alert type."));
                }

                put("success", true);
            } else {
                put("error", $("No such alert."));
            }
        } catch (RuntimeException e) {
            put("error", e.getMessage());
        }

        printTemplateMapAsStringsUsingJson("success", "error");
    }

    @Override
    public void action() {
        // No operations.
    }
}