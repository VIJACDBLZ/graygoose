package com.codeforces.graygoose.page;

public abstract class DataPage extends ApplicationPage {
    @Override
    public void init() {
        super.init();
        skipTemplate();
    }
}
