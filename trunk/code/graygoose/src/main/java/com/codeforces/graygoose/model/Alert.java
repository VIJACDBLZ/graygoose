package com.codeforces.graygoose.model;

import javax.jdo.annotations.*;
import java.io.Serializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Alert implements Serializable {
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

    public Alert() {
    }

    public Alert(String name, String type, String email, String password, int maxAlertCountPerHour) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.password = password;
        this.maxAlertCountPerHour = maxAlertCountPerHour;
    }

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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxAlertCountPerHour() {
        return maxAlertCountPerHour;
    }

    public void setMaxAlertCountPerHour(int maxAlertCountPerHour) {
        this.maxAlertCountPerHour = maxAlertCountPerHour;
    }
}
