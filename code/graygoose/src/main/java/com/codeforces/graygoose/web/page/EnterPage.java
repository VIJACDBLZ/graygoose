package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.dao.UserDao;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.User;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.EmailValidator;
import org.nocturne.validation.RequiredValidator;
import org.nocturne.validation.ValidationException;
import org.nocturne.validation.Validator;

@Link("enter")
public class EnterPage extends WebPage {
    @Inject
    private UserDao userDao;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String email;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String password;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (getUser() != null) {
            abortWithRedirect(DashboardPage.class);
        }
    }

    @Validate("enter")
    public boolean validateEnter() {
        addValidator("email", new EmailValidator());

        addValidator("password", new RequiredValidator());
        addValidator("password", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                if (userDao.findByEmailAndPassword(email, password) == null) {
                    throw new ValidationException($("Invalid email or password"));
                }
            }
        });

        return runValidation();
    }

    @Action("enter")
    public void onEnter() {
        User user = userDao.findByEmailAndPassword(email, password);
        putSession("graygoose.user", user);
        addMessage("Welcome, " + user.getName() + ".", Noty.Type.SUCCESS);
        abortWithRedirect(DashboardPage.class);
    }

    @Override
    public void action() {
        // No operations.
    }

    @Override
    protected String getTitle() {
        return $("Enter");
    }

    @Override
    public boolean hasAnonymousAccess() {
        return true;
    }
}
