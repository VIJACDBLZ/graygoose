package com.codeforces.graygoose.web.page.data;

import com.codeforces.graygoose.web.page.ApplicationPage;

public class DataPage extends ApplicationPage {
    @Override
    public void initializeAction() {
        super.initializeAction();
        skipTemplate();
    }

    @Override
    public void action() {
        // No operations.
    }
}
