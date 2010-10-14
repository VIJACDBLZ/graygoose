package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.page.ApplicationPage;

public abstract class DataPage extends ApplicationPage {
    @Override
    public void initializeAction() {
        super.initializeAction();
        skipTemplate();
    }
}
