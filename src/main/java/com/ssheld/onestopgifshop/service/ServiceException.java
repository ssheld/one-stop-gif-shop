package com.ssheld.onestopgifshop.service;

/**
 * Author: Stephen Sheldon
 **/
public class ServiceException extends Exception {

    private String action;

    private String reason;

    public ServiceException(String action, String reason) {
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
