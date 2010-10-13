package com.codeforces.graygoose.frame;

import java.util.ArrayList;
import java.util.List;

public class TopMenuFrame extends ApplicationFrame {
    private List<Link> links;

    @Override
    public void action() {
        links = new ArrayList<Link>();

        setupLinks();

        put("links", links);
    }

    private void setupLinks() {
        links.add(new Link(getUserService().createLogoutURL(getRequest().getRequestURI()), $("Logout")));
    }

    public static class Link {
        private final String address;
        private final String text;

        public Link(String address, String text) {
            this.address = address;
            this.text = text;
        }

        public String getAddress() {
            return address;
        }

        public String getText() {
            return text;
        }
    }
}
