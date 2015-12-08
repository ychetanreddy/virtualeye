package com.example.sindhura.virtualeye;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button blogin;
    EditText etUsername, etPassword;
    TextView tvregisterLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        blogin = (Button) findViewById(R.id.bLogin);
        tvregisterLink = (TextView) findViewById(R.id.tvRegisterLink);

        blogin.setOnClickListener(this);
        tvregisterLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);
                authenticate(user);
                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchUserDataInBackground(user, new GetUserCallback() {//after fetch backgnd iscompleted the done parameter is recalled --(if u seein server request cmd-userCallback.done(returnedUser)
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                    Log.i("L-Auth", returnedUser.toString());
                }
            }
        });
    }
    private void showErrorMessage(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(Login.this);
        dialogueBuilder.setMessage("Incorrect User Details");
        dialogueBuilder.setPositiveButton("OK",null);//null is kept to say that no action to be taken just error msg disappears after click of ok button
        dialogueBuilder.show();
    }
    private void logUserIn(User returnedUser){
        Log.i("Loguserin",returnedUser.name);
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this,Home.class));

    }
}
