package com.example.sindhura.virtualeye;

/**
 * Created by Sindhura on 11/15/15.
 */
public class User {
    String name, password,location ,username;
    int phnum;

    public User(String name,String location,int phnum,String username, String password){
        this.name=name;
        this.location=location;
        this.phnum=phnum;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password){
        this.name="";
        this.location="";
        this.phnum=-1;
        this.username = username;
        this.password = password;
    }

}
