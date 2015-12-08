package com.example.sindhura.virtualeye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.lang.Thread;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sindhura on 11/15/15.
 */
public class ServerRequests {

    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*150*10000;
    public static final String SERVER_ADDRESS ="http://10.0.2.2:8888/";

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please Wait");
    }
    public void storeUserDataInBackground(User user,  GetUserCallback userCallback){
        Log.i("SR-storeUserDataInB", user.name);
        progressDialog.show();
        new StoreUserDataAsyncTask(user,userCallback).execute();

    }
    public void fetchUserDataInBackground(User user, GetUserCallback callBack){

        Log.i("SR-fetchUserDataInB", user.username);

        progressDialog.show();
        new FetchUserDataAsyncTask(user, callBack).execute();
    }

    public class StoreUserDataAsyncTask extends AsyncTask<Void,Void, Void> {
        User user;
        GetUserCallback userCallback;
        public StoreUserDataAsyncTask(User user, GetUserCallback userCallBack){
            this.user = user;
            this.userCallback = userCallBack;
        }
        //datatosend contains the user whihc needs to send to the server
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("SR-doInBackground", user.name);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name",user.name));
            dataToSend.add(new BasicNameValuePair("location",user.location));
            dataToSend.add(new BasicNameValuePair("phnum",user.phnum + ""));
            dataToSend.add(new BasicNameValuePair("username",user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);//param is passed to know the connection timeout
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");
            Log.i("SR-doInBackground",post.getURI().toString());
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));//encode the datatosend n give to post
                client.execute(post);
                Log.i("SR-doInBackground", "try");
            }catch (Exception e) {
                e.printStackTrace();
                Log.i("SR-doInBackground", "catch");
                Log.i("SR-doInBackground", e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }
    public class FetchUserDataAsyncTask extends AsyncTask<Void,Void,User> {
        User user;
        GetUserCallback userCallback;

        public FetchUserDataAsyncTask(User user,GetUserCallback userCallBack){
            this.user = user;
            this.userCallback = userCallBack;

        }

        @Override
        protected User doInBackground(Void... params) {           //send request to the server the user which has username n pwd as follows
            ArrayList<NameValuePair> dataToSend1 = new ArrayList<>();
            dataToSend1.add(new BasicNameValuePair("username", user.username));
            dataToSend1.add(new BasicNameValuePair("password", user.password));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);//param is passed to know the connection timeout

            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");
            //HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            Log.i("SRlogin-doInBackground", post.getURI().toString());
            //Log.i("doInBackground", dataToSend.toString());
            //Log.i("doInBackground", post.getURI().toString());

            User returnedUser = null;
            String result = "";

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend1));//encode the datatosend n give to post
                //Log.i("doInBackground-try", dataToSend.toString());
                HttpResponse httpResponse = client.execute(post);//receive response of the users data

                //HttpEntity entity = httpResponse.getEntity();// entity is the data from the entity response
                // Log.d("SR_entity",entity.toString());


                result = EntityUtils.toString(httpResponse.getEntity());
                Log.d("SR_result",result);
                JSONObject jObject = new JSONObject(result);

                Log.i("SR-Login", "try");
                // Log.i("SR-Login", "try");
                Log.i("length", Integer.toString(jObject.length()));
                //decodes from json
                //length zero means no user returned
                if (jObject.length() == 0) {
                    returnedUser = null;
                } else {                      //user returned , send data from php "-- notation used in the table--"
                    String name = jObject.getString("name");
                    String location = jObject.getString("location");
                    int phnum = jObject.getInt("phnum");
                    returnedUser = new User(name, location,phnum, user.username, user.password);//we already know the username n password so we reuse them
                    //Log.d("returnedUser", returnedUser);
                }


            }catch (Exception e) {
                e.printStackTrace();
                Log.i("SR-Login", "catch");
                Log.i("SR-Login", e.toString());

            }
            return returnedUser;

        }

        @Override
        protected void onPostExecute(User returnedUser) {
//            Log.i("onPostExe",returnedUser.name);
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
