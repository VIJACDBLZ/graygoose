package com.codeforces.graygoose.web.page;

import com.codeforces.graygoose.exception.ConfigurationException;
import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.model.User;
import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Page;

import java.io.UnsupportedEncodingException;

public abstract class ApplicationPage extends Page {
    @Override
    public void initializeAction() {
        super.initializeAction();

        getTemplateEngineConfiguration().setNumberFormat("0.######");
        getTemplateEngineConfiguration().setLocale(ApplicationContext.getInstance().getLocale());
        getTemplateEngineConfiguration().setEncoding(ApplicationContext.getInstance().getLocale(), "UTF-8");

        try {
            getRequest().setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ConfigurationException("Can't find UTF-8.", e);
        }
        getResponse().setCharacterEncoding("UTF-8");
        getResponse().setContentType("text/html");
    }

    public User getUser() {
        return getSession("graygoose.user", User.class);
    }

    public void logout() {
        removeSession("graygoose.user");
    }

    public boolean hasAnonymousAccess() {
        return false;
    }

    public void addMessage(String text, Noty.Type type) {
        Noty noty = new Noty();
        noty.setText(text);
        noty.setType(type);

        //noinspection unchecked
        putSession("graygoose.noty", noty);
    }

    @Override
    public void finalizeAction() {
        if (hasSession("graygoose.noty")) {
            putGlobal("noty", getSession("graygoose.noty", Noty.class));
            removeSession("graygoose.noty");
        }

        super.finalizeAction();
    }
}
