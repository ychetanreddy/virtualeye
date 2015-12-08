package com.example.sindhura.virtualeye;

import android.os.AsyncTask;
import android.util.Log;
import android.app.ProgressDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sindhura on 12/2/15.
 */

public class Retrieve {
    ProgressDialog progressDialog;

    public void retrieveDataInBackground(User user,  GetPostCallback postCallback){
        Log.i("retrieveDataInB", user.name);


        //progressDialog.show();
        new retrieveDataAsyncTask(user,postCallback).execute(user.username.toString());

    }
    public class retrieveDataAsyncTask extends AsyncTask<String,Void,String[]> {
        User user;
        GetPostCallback postCallback;

        public retrieveDataAsyncTask(User user, GetPostCallback postCallBack) {
            this.user = user;
            this.postCallback = postCallBack;

        }

        protected String[] doInBackground(String... params) {
            //Context context = getApplicationContext();
            //  CharSequence text = "Hello toast!";
            String username = params[0];
            Log.i("retrieve-username", username);
            // int duration = Toast.LENGTH_SHORT;
            //Test Code below
            String SERVER_ADDRESS = "http://10.0.2.2:8888/";
            int CONNECTION_TIMEOUT = 1000 * 150 * 100000;
            ArrayList<NameValuePair> dataToSend1 = new ArrayList<>();
            dataToSend1.add(new BasicNameValuePair("username", username));
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpClient client = new DefaultHttpClient(httpRequestParams);//param is passed to know the connection timeout
            HttpPost post1 = new HttpPost(SERVER_ADDRESS + "retrievepost.php");
            Log.i("retrieve-doInBackground", post1.getURI().toString());
            String result = "";

            try {

                post1.setEntity(new UrlEncodedFormEntity(dataToSend1));//encode the datatosend n give to post
                HttpResponse httpResponse = client.execute(post1);
                result = EntityUtils.toString(httpResponse.getEntity());
                Log.d("retrieve_result", result);
                JSONObject jObject = new JSONObject(result);
                int x = jObject.length();
                Log.i("jsonLength", Integer.toString(x));
                if (jObject.length() == 0) {
                    Log.i("try-retrieve", "null");
                } else {
                    String[] posts = new String[x];
                    for (int i = 0; i < x; i++) {

                        String jsonstr = "post" + Integer.toString(i);
                        posts[i]=(jObject.getString(jsonstr));
                    }
                    //String post = jObject.getString("post0");
                    Log.i("data-recieved", posts.toString());
                    return posts;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("SR-doInBackground", "catch");
                Log.i("retrieve-doInBackground", e.toString());
            }
//Test above

//        Toast toast = Toast.makeText(null, text, duration);
            //      toast.show();
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String[] posts) {
            //
            postCallback.done(posts);
            super.onPostExecute(posts);
        }
    }

}

