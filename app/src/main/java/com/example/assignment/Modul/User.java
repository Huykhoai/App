package com.example.assignment.Modul;

public class User {
    private int id;
    private String name;
    private String pass;
    private String confirmPass;

    public User(int id, String name, String pass, String confirmPass) {
        this.id = id;
        this.name = name;
        this.pass = pass;
        this.confirmPass = confirmPass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }
}
