package com.ssheld.onestopgifshop.dao;

/**
 * Author: Stephen Sheldon
 **/
public class DaoException extends Exception {

    private String action;

    private String reason;

    public DaoException(String action, String reason) {
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
