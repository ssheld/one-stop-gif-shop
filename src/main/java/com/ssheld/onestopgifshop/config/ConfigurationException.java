package com.ssheld.onestopgifshop.config;

/**
 * Author: Stephen Sheldon
 **/
public class ConfigurationException extends Exception {

    private String action;

    private String reason;

    public ConfigurationException(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }
}
