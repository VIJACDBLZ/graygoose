package com.codeforces.graygoose.frame;

import com.codeforces.graygoose.page.web.*;
import org.nocturne.main.ApplicationContext;
import org.nocturne.main.Page;

import java.util.ArrayList;
import java.util.List;

public class MainMenuFrame extends ApplicationFrame {
    private List<Link> links;

    @Override
    public void action() {
        links = new ArrayList<Link>();
        setupLinks();

        put("links", links);

        Page page = ApplicationContext.getInstance().getCurrentPage();
        for (Link link : links) {
            if (page.getClass().equals(link.getWebPageClass())) {
                link.setActive(true);
            }
        }

        Page currentPage = ApplicationContext.getInstance().getCurrentPage();
        put("currentPage", ((WebPage) currentPage).getTitle());
    }

    private void setupLinks() {
        links.add(new Link(LogsPage.class, $("Logs")));
        links.add(new Link(AlertsPage.class, $("Alerts")));
        links.add(new Link(SitesPage.class, $("Sites")));
        links.add(new Link(DashboardPage.class, $("Dashboard")));
    }

    public static class Link {
        private final Class<? extends WebPage> webPageClass;
        private final String text;
        private boolean active;

        public Link(Class<? extends WebPage> webPageClass, String text) {
            this.webPageClass = webPageClass;
            this.text = text;
        }

        public Class<? extends WebPage> getWebPageClass() {
            return webPageClass;
        }

        public String getText() {
            return text;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getWebPageClassName() {
            return webPageClass.getSimpleName();
        }
    }
}