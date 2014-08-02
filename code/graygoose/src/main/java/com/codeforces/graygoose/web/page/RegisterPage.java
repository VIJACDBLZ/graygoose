package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.dao.UserDao;
import com.codeforces.graygoose.model.User;
import com.google.inject.Inject;
import org.nocturne.annotation.Action;
import org.nocturne.annotation.Parameter;
import org.nocturne.annotation.Validate;
import org.nocturne.link.Link;
import org.nocturne.validation.*;

import java.util.Objects;

@Link("register")
public class RegisterPage extends WebPage {
    @Inject
    private UserDao userDao;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String email;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String name;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String password;

    @Parameter(stripMode = Parameter.StripMode.NONE)
    private String password2;

    @Override
    public void initializeAction() {
        super.initializeAction();

        if (getUser() != null) {
            abortWithRedirect(DashboardPage.class);
        }
    }

    @Validate("register")
    public boolean validateRegister() {
        addValidator("email", new RequiredValidator());
        addValidator("email", new EmailValidator());

        addValidator("email", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                if (userDao.findByEmail(email) != null) {
                    throw new ValidationException($("This email is currently in use"));
                }
            }
        });

        addValidator("password", new RequiredValidator());
        addValidator("password", new LengthValidator(5, 80));

        addValidator("password2", new RequiredValidator());
        addValidator("password2", new LengthValidator(5, 80));

        addValidator("password2", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                if (!Objects.equals(password, password2)) {
                    throw new ValidationException($("Confirmation mismatched"));
                }
            }
        });

        addValidator("name", new RequiredValidator());
        addValidator("name", new LengthValidator(2, 80));

        addValidator("name", new Validator() {
            @Override
            public void run(String value) throws ValidationException {
                // <>&{}/#$
                for (int i = 0; i < name.length(); i++) {
                    if (name.charAt(i) == '<' || name.charAt(i) == '>' || name.charAt(i) == '&'
                            || name.charAt(i) == '{' || name.charAt(i) == '{' || name.charAt(i) == '/'
                            || name.charAt(i) == '#' || name.charAt(i) == '$') {
                        throw new ValidationException("Symbol " + name.charAt(i) + " is denied");
                    }
                }
            }
        });

        return runValidation();
    }

    @Action("register")
    public void onRegister() {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        userDao.register(newUser, password);

        putSession("graygoose.user", userDao.findByEmailAndPassword(email, password));
        abortWithRedirect(DashboardPage.class);
    }

    @Override
    public void action() {
        // No operations.
    }

    @Override
    protected String getTitle() {
        return $("Register");
    }

    @Override
    public boolean hasAnonymousAccess() {
        return true;
    }
}
