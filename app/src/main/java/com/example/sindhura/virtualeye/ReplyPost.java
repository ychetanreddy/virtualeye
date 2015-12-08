package com.example.sindhura.virtualeye;

/**
 * Created by Sindhura on 12/5/15.
 */
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

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

import java.util.ArrayList;

public class ReplyPost extends AsyncTask<String, Void, Void> {
    protected Void doInBackground(String... params) {
        //Context context = getApplicationContext();
        //  CharSequence text = "Hello toast!";
        String postId = params[0];
        String response = params[1];
        String replyusername = params[2];
        // int duration = Toast.LENGTH_SHORT;
        //Test Code below
        String SERVER_ADDRESS ="http://10.0.2.2:8888/";
        int CONNECTION_TIMEOUT = 1000*150*100000;
        ArrayList<NameValuePair> dataToSend1 = new ArrayList<>();
        dataToSend1.add(new BasicNameValuePair("postId",postId));
        dataToSend1.add(new BasicNameValuePair("response",response));
        dataToSend1.add(new BasicNameValuePair("username", replyusername ));
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpRequestParams);//param is passed to know the connection timeout
        HttpPost post1 = new HttpPost(SERVER_ADDRESS + "replyPost.php");
        Log.i("SR-doInBackground", post1.getURI().toString());
        try{
            post1.setEntity(new UrlEncodedFormEntity(dataToSend1));//encode the datatosend n give to post
            client.execute(post1);
            Log.i("SR-doInBackground", "try");
        }catch (Exception e) {
            e.printStackTrace();
            Log.i("SR-doInBackground", "catch");
            Log.i("SR-doInBackground", e.toString());
        }
//Test above

//        Toast toast = Toast.makeText(null, text, duration);
        //      toast.show();
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }


    public void execute(EditText etPost, EditText etLocation) {
    }
}




