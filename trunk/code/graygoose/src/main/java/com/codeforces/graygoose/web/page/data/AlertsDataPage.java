package com.codeforces.graygoose.web.page.data;

import com.codeforces.commons.text.StringUtil;
import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.util.GoogleCalendarException;
import com.codeforces.graygoose.util.GoogleCalendarUtil;
import com.codeforces.graygoose.util.MailUtil;
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
                if (Alert.E_MAIL_ALERT_TYPE.equals(alert.getType())) {
                    try {
                        MailUtil.sendMail(alert.getEmail(), "GrayGoose test alert: " + alert.getName(), "...");
                    } catch (MessagingException e) {
                        throw new RuntimeException("E-mail was not sent: " + e.getMessage());
                    }
                } else if (Alert.GOOGLE_CALENDAR_ALERT_TYPE.equals(alert.getType())) {
                    try {
                        GoogleCalendarUtil.addEvent(
                                "GrayGoose test alert: " + alert.getName(), alert.getEmail(), alert.getPassword()
                        );
                    } catch (GoogleCalendarException e) {
                        throw new RuntimeException("Can't add Google calendar event: " + e.getMessage());
                    }
                } else if (Alert.SMS_REQUEST_ALERT_TYPE.equals(alert.getType())) {
                    SmsUtil.send(alert, StringUtil.shrinkTo("GrayGoose test alert: " + alert.getName(), 100));
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
}
