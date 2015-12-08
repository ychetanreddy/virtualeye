package com.example.sindhura.virtualeye;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity implements View.OnClickListener {

    Button bToPost, bToLogout, bToSettings;
    EditText etPost, etLocation;
    UserLocalStore userLocalStore;

    NotificationManager NM;

    ListView myListView;
    ArrayAdapter<String> aa;
    final Context context = this;
    ArrayList<String> postdata;
    ArrayList<Integer> postids;

    private Timer autoUpdate;

    final ArrayList<String> noteList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        etPost = (EditText) findViewById(R.id.beditText);
        etLocation = (EditText) findViewById(R.id.bLocation);
        bToPost = (Button) findViewById(R.id.bToPost);
        myListView = (ListView) findViewById(R.id.myListView);
        postdata = new ArrayList<String>();
        postids = new ArrayList<Integer>();
        bToLogout = (Button) findViewById(R.id.bToLogout);
        bToSettings = (Button) findViewById(R.id.bToSettings);
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);
        myListView.setAdapter(aa);
        userLocalStore = new UserLocalStore(this);
        // Enable this if you want to direct to new activity on click of each list item
        User user = userLocalStore.getLoggedInUser();
        Retrieve retrieve = new Retrieve();
        retrieve.retrieveDataInBackground(user, new GetPostCallback() {//after fetch backgnd iscompleted the done parameter is recalled --(if u seein server request cmd-userCallback.done(returnedUser)
            @Override
            public void done(String[] post) {
                Log.i("Home_post", post.toString());
                postextract(post);//post retrieved here from retrieve.java
            }
        });
        bToPost.setOnClickListener(this);
        bToLogout.setOnClickListener(this) ;
        bToSettings.setOnClickListener(this) ;
    }

        private void Notify(String notificationTitle, String notificationMessage){
            int notificationID = 100;

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
            mBuilder.setContentTitle(notificationTitle);
            mBuilder.setContentText(notificationMessage);

            Intent resultIntent = new Intent(this, Home.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Home.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationID, mBuilder.build());

        }

    //FOR AUTO REFRESHING THE PAGE
    /*
    @Override
    public void onResume(){
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        postextract(); //need to call the posting function
                    }
                });
            }
        });
    }

    @Override
    public void onPause(){
        autoUpdate.cancel();
        super.onPause();
    }*/

    public void refresh()
    {
        startActivity(new Intent(this,Home.class));
    }

    public void postextract(final String[] post)
    {

        Log.i("Postextract",post[0]);
        int x = post.length;
        Log.i("Postextractlen", Integer.toString(x));
        for(int i=0;i<x;i++)
        {
            final String[] parts = post[i].split("#");
            Log.i("Postextractpart", parts[0]);
            Log.i("Postextractpart2", parts[1]);
            Notify(parts[0],parts[1]);
            postdata.add(parts[1]);
            postids.add(Integer.parseInt(parts[0]));
            noteList.add(0, post.length - i + "." + postdata.get(i));//posts are displayed on the listview
            myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.activity_custom_dialouge);
                    dialog.setTitle("Response to the post..");
                    final EditText response = (EditText) dialog.findViewById(R.id.toReply);
                    Button responseButton = (Button) dialog.findViewById(R.id.ResponseButton);
                    responseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            noteList.add(0, postids.get(post.length - position - 1) + "\nReply: " + response.getText().toString());
                            //aa.notifyDataSetChanged();
                            dialog.dismiss();
                            sendReply(post.length - position - 1, response.getText().toString());
                            refresh();
                        }
                    });
                    Button cancelButton = (Button) dialog.findViewById(R.id.bCancel);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            aa.notifyDataSetChanged();
        }
    }
    public void sendReply(int postindex,String response)
    {
        User user = userLocalStore.getLoggedInUser();
        Log.i("sendReply",user.username);
        response = postdata.get(postindex) + "\n"+ user.username + ": " + response;
        Log.i("sendReply2", response);
        Log.i("sendReply2", postids.get(postindex).toString());
        //postids.get(postindex),response,user.username
        new ReplyPost().execute(postids.get(postindex).toString(), response, user.username);
    }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bToPost:
                    if (etPost.getText().length() > 0) {
                        noteList.add(0, "To " + etLocation.getText().toString() + ": \n " + etPost.getText().toString());
                        //String post = etPost.getText().toString();
                        //String loc = etLocation.getText().toString();
                        User user = userLocalStore.getLoggedInUser();
                        Log.i("Home.java", user.username);
                        new PostToDatabase().execute(etPost.getText().toString(), etLocation.getText().toString(), user.username.toString());
                        //aa.notifyDataSetChanged();
                        etPost.setText("");
                        etLocation.setText("");
                        refresh();
                    }
                    break;
                case R.id.bToLogout:
                    userLocalStore.clearUserData();
                    userLocalStore.setUserLoggedIn(false);
                    startActivity(new Intent(this,Login.class));
                    break;
                case R.id.bToSettings:
                    startActivity(new Intent(this,SettingsActivity.class));
            }
        }
    }