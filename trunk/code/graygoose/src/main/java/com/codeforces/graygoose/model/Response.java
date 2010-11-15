package com.codeforces.graygoose.model;

import com.google.appengine.api.datastore.Text;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Response extends AbstractEntity {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String siteUrl;

    @Persistent
    private int code;

    @Persistent
    private Text text;

    @Persistent
    private boolean deleted;

    @Persistent
    private Date creationTime;

    /**
     * Constructs a <code>{@link Response}</code> with specified parameters.
     *
     * @param siteUrl URL of the site from which came the HTTP-response
     * @param code    response code or <code>-1</code> if no HTTP-response came within specified timeout
     *                or any other exception has been thrown during URL fetching
     * @param text    body of the HTTP-response in case of success,
     *                empty string or <code>{@link Exception#getMessage Exception.getMessage}</code> in case of failure
     */
    public Response(String siteUrl, int code, Text text) {
        this.siteUrl = siteUrl;
        this.code = code;
        this.text = text;
        deleted = false;
        creationTime = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

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

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Date getCreationTime() {
        return creationTime;
    }
}
