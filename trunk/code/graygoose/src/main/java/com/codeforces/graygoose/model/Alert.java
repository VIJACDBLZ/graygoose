package com.codeforces.graygoose.model;

public class Alert extends ApplicationEntity {
    private String name;

    /**
     * This field contains the name of an alert type.
     * Valid values are (without apostrophes) 'E-mail' and 'Google calendar event'.
     */
    private String type;

    private String email;

    private String password;    // optional, required only for 'Google calendar event'-type alert

    private int maxAlertCountPerHour;

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

    public static Alert newAlert(String name, String type, String email, String password, int maxAlertCountPerHour) {
        Alert alert = new Alert();

        alert.setName(name);
        alert.setType(type);
        alert.setEmail(email);
        alert.setPassword(password);
        alert.setMaxAlertCountPerHour(maxAlertCountPerHour);

        return alert;
    }
}
