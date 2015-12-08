package com.example.sindhura.virtualeye;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Sindhura on 11/15/15.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putString("location", user.location);
        spEditor.putInt("phnum", user.phnum);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }


    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("name", "");
        String location = userLocalDatabase.getString("location", "");
        int phnum = userLocalDatabase.getInt("phnum",-1);
        String username = userLocalDatabase.getString("username","");
        String password = userLocalDatabase.getString("password","");

        User storedUser = new User(name, location, phnum, username, password);
        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if (userLocalDatabase.getBoolean("LoggedIn", false)== true){
            Log.i("getUserLogIn", "true");
            return true;
        }else{
            Log.i("getUserLogIn","false");
            return false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}


