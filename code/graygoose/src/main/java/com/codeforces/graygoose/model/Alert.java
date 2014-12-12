package com.codeforces.graygoose.model;

public class Alert extends ApplicationEntity {
    public static final String E_MAIL_ALERT_TYPE = "E-mail";
    public static final String GOOGLE_CALENDAR_ALERT_TYPE = "Google calendar event";
    public static final String SMS_REQUEST_ALERT_TYPE = "SMS POST-request";

    private String name;

    /**
     * This field contains the name of an alert type.
     * Valid values are (without apostrophes) 'E-mail', 'Google calendar event' and 'SMS POST-request'.
     */
    private String type;

    /**
     * Optional, required only for 'E-mail' and 'Google calendar event' alert types.
     */
    private String email;

    /**
     * Optional, required only for 'Google calendar event' alert type.
     */
    private String password;

    /**
     * Optional, required only for 'SMS POST-request' alert type.
     */
    private String smsServiceUrl;

    /**
     * Optional, required only for 'SMS POST-request' alert type.
     */
    private String smsServicePhoneParameterName;

    /**
     * Optional, required only for 'SMS POST-request' alert type.
     */
    private String smsServicePhone;

    /**
     * Optional, required only for 'SMS POST-request' alert type.
     */
    private String smsServiceMessageParameterName;

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

    public String getSmsServiceUrl() {
        return smsServiceUrl;
    }

    public void setSmsServiceUrl(String smsServiceUrl) {
        this.smsServiceUrl = smsServiceUrl;
    }

    public String getSmsServicePhoneParameterName() {
        return smsServicePhoneParameterName;
    }

    public void setSmsServicePhoneParameterName(String smsServicePhoneParameterName) {
        this.smsServicePhoneParameterName = smsServicePhoneParameterName;
    }

    public String getSmsServicePhone() {
        return smsServicePhone;
    }

    public void setSmsServicePhone(String smsServicePhone) {
        this.smsServicePhone = smsServicePhone;
    }

    public String getSmsServiceMessageParameterName() {
        return smsServiceMessageParameterName;
    }

    public void setSmsServiceMessageParameterName(String smsServiceMessageParameterName) {
        this.smsServiceMessageParameterName = smsServiceMessageParameterName;
    }

    public int getMaxAlertCountPerHour() {
        return maxAlertCountPerHour;
    }

    public void setMaxAlertCountPerHour(int maxAlertCountPerHour) {
        this.maxAlertCountPerHour = maxAlertCountPerHour;
    }

    public static Alert newEmailAlert(String name, String email, int maxAlertCountPerHour) {
        Alert alert = new Alert();

        alert.name = name;
        alert.type = E_MAIL_ALERT_TYPE;
        alert.email = email;
        alert.maxAlertCountPerHour = maxAlertCountPerHour;

        return alert;
    }

    public static Alert newCalendarAlert(String name, String email, String password, int maxAlertCountPerHour) {
        Alert alert = new Alert();

        alert.name = name;
        alert.type = GOOGLE_CALENDAR_ALERT_TYPE;
        alert.email = email;
        alert.password = password;
        alert.maxAlertCountPerHour = maxAlertCountPerHour;

        return alert;
    }

    public static Alert newSmsAlert(
            String name, String smsServiceUrl, String smsServicePhoneParameterName,
            String smsServicePhone, String smsServiceMessageParameterName, int maxAlertCountPerHour) {
        Alert alert = new Alert();

        alert.name = name;
        alert.type = SMS_REQUEST_ALERT_TYPE;
        alert.smsServiceUrl = smsServiceUrl;
        alert.smsServicePhoneParameterName = smsServicePhoneParameterName;
        alert.smsServicePhone = smsServicePhone;
        alert.smsServiceMessageParameterName = smsServiceMessageParameterName;
        alert.maxAlertCountPerHour = maxAlertCountPerHour;

        return alert;
    }
}
