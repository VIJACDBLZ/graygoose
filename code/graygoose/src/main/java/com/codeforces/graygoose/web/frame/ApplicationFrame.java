package com.codeforces.graygoose.web.frame;

import com.codeforces.graygoose.misc.Noty;
import com.codeforces.graygoose.web.page.ApplicationPage;
import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Frame;
import org.nocturne.main.Page;

public abstract class ApplicationFrame extends Frame {
    public void addMessage(String text, Noty.Type type) {
        Page page = ApplicationContext.getInstance().getCurrentPage();
        if (page instanceof ApplicationPage) {
            ApplicationPage applicationPage = (ApplicationPage) page;
            applicationPage.addMessage(text, type);
        }
    }
}
