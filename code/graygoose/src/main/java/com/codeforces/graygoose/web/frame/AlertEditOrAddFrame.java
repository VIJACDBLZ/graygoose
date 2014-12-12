package com.codeforces.graygoose.web.frame;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.validation.ConfirmPasswordValidator;
import com.codeforces.graygoose.validation.PhoneNumberValidator;
import com.codeforces.graygoose.validation.UrlValidator;
import com.codeforces.graygoose.web.page.WebPage;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.validation.*;

public class AlertEditOrAddFrame extends ApplicationFrame {
    @Parameter
    private Long id;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String name;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String type;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String email;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String password;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String smsServiceUrl;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String smsServicePhoneParameterName;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String smsServicePhone;

    @Parameter(stripMode = Parameter.StripMode.SAFE)
    private String smsServiceMessageParameterName;

    @Parameter
    private int maxAlertCountPerHour;

    private Alert alert;
    private Class<? extends WebPage> redirectPageClass;

    @Inject
    private AlertDao alertDao;

    public void setupEdit(long id, Class<? extends WebPage> redirectPageClass) {
        this.id = id;
        this.redirectPageClass = redirectPageClass;
    }

    @SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
    public void setupAdd(Class<? extends WebPage> redirectPageClass) {
        this.redirectPageClass = redirectPageClass;
    }

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (id != null && (alert = alertDao.find(id)) != null) {
            put("name", alert.getName());
            put("type", alert.getType());
            put("email", alert.getEmail());
            put("password", alert.getPassword());
            put("passwordConfirmation", alert.getPassword());
            put("smsServiceUrl", alert.getSmsServiceUrl());
            put("smsServicePhoneParameterName", alert.getSmsServicePhoneParameterName());
            put("smsServicePhone", alert.getSmsServicePhone());
            put("smsServiceMessageParameterName", alert.getSmsServiceMessageParameterName());
            put("maxAlertCountPerHour", alert.getMaxAlertCountPerHour());

            put("edit", true);
        } else {
            put("smsServicePhoneParameterName", "to");
            put("smsServiceMessageParameterName", "text");

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

        addValidator("type", new RequiredValidator());
        addValidator("type", new OptionValidator(
                $(Alert.E_MAIL_ALERT_TYPE), $(Alert.GOOGLE_CALENDAR_ALERT_TYPE), $(Alert.SMS_REQUEST_ALERT_TYPE)
        ));

        if (Alert.E_MAIL_ALERT_TYPE.equals(type) || Alert.GOOGLE_CALENDAR_ALERT_TYPE.equals(type)) {
            addValidator("email", new RequiredValidator());
            addValidator("email", new LengthValidator(1, 200));
            addValidator("email", new EmailValidator());

            if (Alert.GOOGLE_CALENDAR_ALERT_TYPE.equals(type)) {
                addValidator("passwordConfirmation", new ConfirmPasswordValidator(password));
            }
        }

        if (Alert.SMS_REQUEST_ALERT_TYPE.equals(type)) {
            addValidator("smsServiceUrl", new RequiredValidator());
            addValidator("smsServiceUrl", new UrlValidator());

            addValidator("smsServicePhoneParameterName", new RequiredValidator());
            addValidator("smsServicePhoneParameterName", new LengthValidator(1, 50));
            addValidator("smsServicePhoneParameterName", new EnglishValidator());
            addValidator("smsServicePhoneParameterName", new PatternValidator("[a-zA-Z0-9]{1,50}", "Illegal parameter name"));

            addValidator("smsServicePhone", new RequiredValidator());
            addValidator("smsServicePhone", new LengthValidator(5, 50));
            addValidator("smsServicePhone", new PhoneNumberValidator());

            addValidator("smsServiceMessageParameterName", new RequiredValidator());
            addValidator("smsServiceMessageParameterName", new LengthValidator(1, 50));
            addValidator("smsServiceMessageParameterName", new EnglishValidator());
            addValidator("smsServiceMessageParameterName", new PatternValidator("[a-zA-Z0-9]{1,50}", "Illegal parameter name"));
        }

        addValidator("maxAlertCountPerHour", new RequiredValidator());
        addValidator("maxAlertCountPerHour", new OptionValidator("0", "1", "2", "3", "5", "10", "30"));

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
        if (alert != null) {
            alert.setName(name);
            alert.setType(type);
            alert.setEmail(email);
            alert.setPassword(password);
            alert.setSmsServiceUrl(smsServiceUrl);
            alert.setSmsServicePhoneParameterName(smsServicePhoneParameterName);
            alert.setSmsServicePhone(smsServicePhone);
            alert.setSmsServiceMessageParameterName(smsServiceMessageParameterName);
            alert.setMaxAlertCountPerHour(maxAlertCountPerHour);

            alertDao.update(alert);
            addMessage($("Alert has been updated"), Noty.Type.SUCCESS);
        } else {
            addMessage($("Can't find alert to update"), Noty.Type.ERROR);
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        switch (type) {
            case Alert.E_MAIL_ALERT_TYPE:
                alertDao.insert(Alert.newEmailAlert(name, email, maxAlertCountPerHour));
                break;
            case Alert.GOOGLE_CALENDAR_ALERT_TYPE:
                alertDao.insert(Alert.newCalendarAlert(name, email, password, maxAlertCountPerHour));
                break;
            case Alert.SMS_REQUEST_ALERT_TYPE:
                alertDao.insert(Alert.newSmsAlert(
                        name, smsServiceUrl, smsServicePhoneParameterName, smsServicePhone,
                        smsServiceMessageParameterName, maxAlertCountPerHour
                ));
                break;
            default:
                addMessage($("Unknown alert type"), Noty.Type.ERROR);
                abortWithRedirect(redirectPageClass);
        }

        addMessage($("Alert has been added"), Noty.Type.SUCCESS);
        abortWithRedirect(redirectPageClass);
    }
}