package com.example.dellinc.lo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    String user = new String();
    Integer exist;
    String pass = new String();
    String na = new String();
    JSONObject jsonObj = null;
    HttpEntity entity;
    String TAG = MainActivity.class.getSimpleName();
    String resultnew = null;
    String response;
    Button register,login;
    EditText username, name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logindesign);
        username = (EditText) findViewById(R.id.editText4);
        password = (EditText) findViewById(R.id.editText5);
        login = (Button) findViewById(R.id.button3);
     login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = username.getText().toString();
                pass = password.getText().toString();

                Network net = new Network();

                net.execute();
            }
        });

    }

    public  class Network extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        protected void onPreExecute() {
            progressDialog.setTitle("Loading.....");
            progressDialog.setMessage("Loading.....");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface arg0) {
                    Network.this.cancel(true);
                }
            });
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity entity = null;
            HttpResponse httpResponse = null;
            httpClient = new DefaultHttpClient();
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
            String url = "http://192.168.16.1:1234/login/login.php";
            pairs.add(new BasicNameValuePair("user", user));
            pairs.add(new BasicNameValuePair("pass", pass));
            InputStream inputStream = null;
            String result = null;

            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                Log.i(TAG, "Entity SEt");
                httpResponse = httpClient.execute(httpPost);

                entity = httpResponse.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
            } catch (Exception e) {
                // Oops
            }
            finally {
                try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
            }
            return result;
        }

        protected void onPostExecute(String result) {
            resultnew = result;
            check();

        }
    }

    private void check() {
        try {
            JSONObject jsonObj = new JSONObject(resultnew);
            exist = jsonObj.getInt("exists");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (exist == 1) {
            Toast.makeText(LoginActivity.this, "Login Success :)", Toast.LENGTH_LONG).show();
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);


        } else {
            Toast.makeText(LoginActivity.this, "Failed :(", Toast.LENGTH_LONG).show();
            Intent i = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(i);

        }


    }


}
