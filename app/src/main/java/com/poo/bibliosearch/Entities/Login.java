package com.poo.bibliosearch.Entities;

public class Login {

    private User user;
    private  Boolean isLogedIn;
    private  Boolean logedAsAdmin;

    public Login(User user, Boolean isLogedIn,Boolean logedAsAdmin) {
        this.user = user;
        this.isLogedIn = isLogedIn;
        this.logedAsAdmin = user.isAdmin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getLogedIn() {
        return isLogedIn;
    }

    public void setLogedIn(Boolean logedIn) {
        isLogedIn = logedIn;
    }

    public Boolean getLogedAsAdmin() {
        return logedAsAdmin;
    }

    public void setLogedAsAdmin(Boolean logedAsAdmin) {
        this.logedAsAdmin = logedAsAdmin;
    }
}
