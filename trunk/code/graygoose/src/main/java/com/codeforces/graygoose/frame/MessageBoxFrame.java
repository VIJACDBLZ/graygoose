package com.codeforces.graygoose.frame;

public class MessageBoxFrame extends ApplicationFrame {
    public static final String MESSAGE_BOX_TEXT = "MessageBoxFrame.text";

    private boolean hasText() {
        return hasSession(MESSAGE_BOX_TEXT);
    }

    private String getText() {
        return getSession(MESSAGE_BOX_TEXT, String.class);
    }

    private void unsetText() {
        removeSession(MESSAGE_BOX_TEXT);
    }

    @Override
    public void action() {
        if (hasText()) {
            put("text", getText());
            unsetText();
        }
    }
}
