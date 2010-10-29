package com.codeforces.graygoose.page;

import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Page;

public abstract class ApplicationPage extends Page {
    @Override
    public void initializeAction() {
        super.initializeAction();

        getTemplateEngineConfiguration().setNumberFormat("0.######");
        getTemplateEngineConfiguration().setLocale(ApplicationContext.getInstance().getLocale());
        getTemplateEngineConfiguration().setEncoding(ApplicationContext.getInstance().getLocale(), "UTF-8");
    }
}
