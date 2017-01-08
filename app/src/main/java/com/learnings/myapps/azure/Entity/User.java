package com.learnings.myapps.azure.Entity;

public class User {
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        this.Password = password;
    }

    public String getLogin() {
        return Login;
    }
    public void setLogin(String login) {
        Login = login;
    }

    public String id;
    private String CardRefRecId;
    private String Login;
    private String Password;
}
