package com.poo.bibliosearch.Entities;

import java.util.ArrayList;

public class User {

    public boolean isAdmin;
    String firstName,lastName,userName,password,college,email,TYPE;
    Boolean isActive;
    ArrayList<Book> myBooks;

    public User(String userName, String password, String email, String firstName, String lastName, String college, boolean isAdmin){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.college = college;
        this.userName = userName;
        this.password = password;
        this.isActive = true;
        this.isAdmin = isAdmin;
        TYPE = "USER";
        myBooks = null;
    }
    public User(String userName, String password, String email, String firstName, String lastName, String college){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.college = college;
        this.userName = userName;
        this.password = password;
        this.isActive = true;
        this.isAdmin = false;
        TYPE = "USER";
    }

    public User(){
        this.userName = "empty";
        this.email = "empty@empty.com";
        this.password = "empty";
        this.isActive = true;
        this.isAdmin = false;
        TYPE = "USER";
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getTYPE (){
        return TYPE;
    }

    public ArrayList<Book> getMyBooks() {
        return myBooks;
    }
}