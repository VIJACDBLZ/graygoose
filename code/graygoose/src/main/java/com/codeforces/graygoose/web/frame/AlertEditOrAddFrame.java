package com.codeforces.graygoose.web.frame;

import com.codeforces.graygoose.dao.AlertDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.Alert;
import com.codeforces.graygoose.web.page.WebPage;
import com.codeforces.graygoose.validation.ConfirmPasswordValidator;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.validation.EmailValidator;
import org.nocturne.validation.LengthValidator;
import org.nocturne.validation.OptionValidator;
import org.nocturne.validation.RequiredValidator;

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

        if (id != null) {
            alert = alertDao.find(id);

            if (alert != null) {
                put("name", alert.getName());
                put("type", alert.getType());
                put("email", alert.getEmail());
                put("password", alert.getPassword());
                put("passwordConfirmation", alert.getPassword());
                put("maxAlertCountPerHour", String.valueOf(alert.getMaxAlertCountPerHour()));

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

            alertDao.update(alert);
            addMessage($("Alert has been updated"), Noty.Type.SUCCESS);
        } else {
            addMessage($("Can't find alert to update"), Noty.Type.ERROR);
        }

        abortWithRedirect(redirectPageClass);
    }

    @Action("add")
    public void onAdd() {
        alertDao.insert(Alert.newAlert(name, type, email, password, maxAlertCountPerHour));

        addMessage($("Alert has been added"), Noty.Type.SUCCESS);
        abortWithRedirect(redirectPageClass);
    }
}