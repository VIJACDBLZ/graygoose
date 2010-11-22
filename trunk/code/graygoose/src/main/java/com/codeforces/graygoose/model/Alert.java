package com.codeforces.graygoose.model;

import com.codeforces.graygoose.util.EncryptUtil;

import javax.jdo.annotations.*;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Alert extends AbstractEntity {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    /**
     * This field contains the name of an alert type.
     * Valid values are (without apostrophes) 'E-mail' and 'Google calendar event'.
     */
    @Persistent
    private String type;

    @Persistent
    private String email;

    @Persistent
    private String password;    // optional, required only for 'Google calendar event'-type alert

    @Persistent
    private int maxAlertCountPerHour;

    @Persistent
    private boolean deleted;

    @SuppressWarnings({"FieldMayBeFinal"})
    @Persistent
    private Date creationTime;

    public Alert(String name, String type, String email, String password, int maxAlertCountPerHour) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.password = password;
        this.maxAlertCountPerHour = maxAlertCountPerHour;
        deleted = false;
        creationTime = new Date();
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return EncryptUtil.decrypt(password);
    }

    public void setPassword(String password) {
        this.password = EncryptUtil.encrypt(password);
    }

    public int getMaxAlertCountPerHour() {
        return maxAlertCountPerHour;
    }

    public void setMaxAlertCountPerHour(int maxAlertCountPerHour) {
        this.maxAlertCountPerHour = maxAlertCountPerHour;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @SuppressWarnings({"ReturnOfDateField"})
    @Override
    public Date getCreationTime() {
        return creationTime;
    }
}
