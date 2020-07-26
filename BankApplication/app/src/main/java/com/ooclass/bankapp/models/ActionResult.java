package com.ooclass.bankapp.models;

public class ActionResult {
    public boolean success;
    public String message;

    public ActionResult() {}

    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
