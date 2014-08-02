package com.codeforces.graygoose.model;

public class Site extends ApplicationEntity {
    private String name;
    private String url;
    private int rescanPeriodSeconds;
    private Long pauseFromMinute;
    private Long pauseToMinute;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRescanPeriodSeconds() {
        return rescanPeriodSeconds;
    }

    public void setRescanPeriodSeconds(int rescanPeriodSeconds) {
        this.rescanPeriodSeconds = rescanPeriodSeconds;
    }

    public Long getPauseFromMinute() {
        return pauseFromMinute;
    }

    public void setPauseFromMinute(Long pauseFromMinute) {
        this.pauseFromMinute = pauseFromMinute;
    }

    public Long getPauseToMinute() {
        return pauseToMinute;
    }

    public void setPauseToMinute(Long pauseToMinute) {
        this.pauseToMinute = pauseToMinute;
    }

    public static Site newSite(String name, String url, int rescanPeriodSeconds, Long pauseFromMinute, Long pauseToMinute) {
        Site site = new Site();

        site.setName(name);
        site.setUrl(url);
        site.setRescanPeriodSeconds(rescanPeriodSeconds);
        site.setPauseFromMinute(pauseFromMinute);
        site.setPauseToMinute(pauseToMinute);

        return site;
    }
}
