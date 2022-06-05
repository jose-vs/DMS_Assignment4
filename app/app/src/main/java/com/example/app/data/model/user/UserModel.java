package com.example.app.data.model.user;

public class UserModel {

    private String name;
    private String email;
    private String uname;
    private String password;

    public UserModel(String name, String uname, String email, String password) {
        this.name = name;
        this.uname = uname;
        this.email = email;
        this.password = password;
    }

    public UserModel(){ }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUname() {
        return uname;
    }

    public String getPassword() {
        return password;
    }
}
