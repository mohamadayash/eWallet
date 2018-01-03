package com.example.omarsaad.appfortesting;

/**
 * Created by Omar Saad on 12/14/2015.
 */
public class Account {

    public String name;
    public String username;
    public String password;
    public String deviceid;
    public String email;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    @Override
    public String toString() {
        return "Account{" + "name='" + name + '\'' + ", txtUserName='" + username + '\'' + ", txtPassword='" + password + '\'' +
                ", DeviceId='" + deviceid + '\'' + ", txtEmail='" + email + '\'' + '}';
    }

}
