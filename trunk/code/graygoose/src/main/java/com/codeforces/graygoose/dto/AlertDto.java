package com.codeforces.graygoose.dto;

import org.nocturne.main.ApplicationContext;

import java.util.LinkedList;
import java.util.List;

import com.codeforces.graygoose.model.Alert;

public class AlertDto {
    private final Alert alert;

    public AlertDto(Alert alert) {
        if (alert == null) {
            throw new NullPointerException(ApplicationContext.getInstance().$("Argument 'alert' is null."));
        }

        this.alert = alert;
    }

    public Long getId() {
        return alert.getId();
    }

    public String getName() {
        return alert.getName();
    }

    public void setName(String name) {
        alert.setName(name);
    }

    public String getType() {
        return alert.getType();
    }

    public void setType(String type) {
        alert.setType(type);
    }

    public String getEmail() {
        return alert.getEmail();
    }

    public void setEmail(String email) {
        alert.setEmail(email);
    }

    public String getPassword() {
        return alert.getPassword() == null ? "" : ApplicationContext.getInstance().$("*present*");
    }

    public void setPassword(String password, String passwordConfirmation) {

        if (password == null || "".equals(password)) {
            if (passwordConfirmation == null || "".equals(passwordConfirmation)) {
                alert.setPassword(null);
                return;
            }
        } else {
            if (password.equals(passwordConfirmation)) {
                if (!password.equals(ApplicationContext.getInstance().$("*present*"))) {
                    alert.setPassword(password);
                }
                return;
            }
        }

        throw new RuntimeException(
                ApplicationContext.getInstance().$("Password confirmation was failed."));
    }

    public int getMaxAlertCountPerHour() {
        return alert.getMaxAlertCountPerHour();
    }

    public void setMaxAlertCountPerHour(int maxAlertCountPerHour) {
        alert.setMaxAlertCountPerHour(maxAlertCountPerHour);
    }

    public static List<AlertDto> getWrappedAlertsList(List<Alert> alerts) {
        List<AlertDto> wrappedAlerts = new LinkedList<AlertDto>();
        for (Alert alert : alerts) {
            wrappedAlerts.add(new AlertDto(alert));
        }
        return wrappedAlerts;
    }
}
