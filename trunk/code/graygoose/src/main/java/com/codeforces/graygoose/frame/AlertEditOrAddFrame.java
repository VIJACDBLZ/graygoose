package com.codeforces.graygoose.frame;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.misc.ConfirmPasswordValidator;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.page.web.WebPage;
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
    private String passwordConfirmation;

    @Parameter
    private int maxAlertCountPerHour;

    private Alert alert;
    private Class<? extends WebPage> redirectPageClass;

    @Inject
    private AlertDao alertDao;

    public void setup(long id, Class<? extends WebPage> redirectPageClass) {
        this.id = id;
        this.redirectPageClass = redirectPageClass;
    }

    public void setup(Class<? extends WebPage> redirectPageClass) {
        this.redirectPageClass = redirectPageClass;
    }

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (id != null) {
            alert = alertDao.find(id);

            if (alert != null) {
                put("name", alert.getName());
                put("type", alert.getType());
                put("email", alert.getEmail());
                put("password", alert.getPassword());
                put("passwordConfirmation", alert.getPassword());
                put("maxAlertCountPerHour", "" + alert.getMaxAlertCountPerHour());

                put("edit", true);
            }
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

        addValidator("type", new RequiredValidator());
        addValidator("type", new OptionValidator($("E-mail"), $("Google calendar event")));

        addValidator("email", new RequiredValidator());
        addValidator("email", new LengthValidator(1, 200));
        addValidator("email", new EmailValidator());

        addValidator("passwordConfirmation", new ConfirmPasswordValidator(password));

        addValidator("maxAlertCountPerHour", new RequiredValidator());
        addValidator("maxAlertCountPerHour", new OptionValidator("0", "1", "2", "3", "5", "10"));

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
            alert.setMaxAlertCountPerHour(maxAlertCountPerHour);

            setMessage($("Alert has been updated."));
        } else {
            setMessage($("Can't find alert to update."));
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        Alert alert = new Alert();

        alert.setName(name);
        alert.setType(type);
        alert.setEmail(email);
        alert.setPassword(password);
        alert.setMaxAlertCountPerHour(maxAlertCountPerHour);

        alertDao.insert(alert);

        setMessage($("Alert has been added."));
        abortWithRedirect(redirectPageClass);
    }
}