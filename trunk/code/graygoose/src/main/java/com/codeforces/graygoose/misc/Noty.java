package com.codeforces.graygoose.misc;

import java.io.Serializable;

public class Noty implements Serializable {
    private String text;
    private Type type;

    public String getText() {
        return text;
    }

    public String getType() {
        return type.toString().toLowerCase();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        SUCCESS,
        ERROR
    }
}
