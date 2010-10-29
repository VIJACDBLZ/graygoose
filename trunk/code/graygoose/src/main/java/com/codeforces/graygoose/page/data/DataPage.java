package com.codeforces.graygoose.page.data;

import com.codeforces.graygoose.page.ApplicationPage;
import com.codeforces.graygoose.page.UserPage;

public abstract class DataPage extends UserPage {
    @Override
    public void initializeAction() {
        super.initializeAction();
        skipTemplate();
    }
}
