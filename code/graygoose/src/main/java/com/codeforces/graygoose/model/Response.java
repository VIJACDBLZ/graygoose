package com.codeforces.graygoose.model;

public class Response extends ApplicationEntity {
    private String siteUrl;

    /** HTTP-status code or -1. */
    private int code;

    private String text;

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static Response newResponse(String siteUrl, int code, String text) {
        Response response = new Response();
        response.setSiteUrl(siteUrl);
        response.setCode(code);
        response.setText(text);
        return response;
    }
}
