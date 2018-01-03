package com.example.omarsaad.appfortesting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.Date;

public class Login extends Activity {
    Account account;
    String DeviceId,UserName,Password,currenttime;
    DataPreferences DataPreferences;
    private flexjson.JSONSerializer serializer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serializer = new flexjson.JSONSerializer();

        long time = System.currentTimeMillis();
        currenttime = String.valueOf(time);

        DataPreferences = new DataPreferences(this);
        DeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        final EditText txtUserName = (EditText)findViewById(R.id.login_usernametxt);
        final EditText txtPassword = (EditText)findViewById(R.id.login_passwordtxt);

        Button register = (Button)findViewById(R.id.login_register);
        Button login = (Button)findViewById(R.id.login_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,LicensePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = txtUserName.getText().toString();
                Password = txtPassword.getText().toString();
                if(UserName.isEmpty() || Password.isEmpty())
                    Toast.makeText(getBaseContext(), "Please Enter the empty blanck(s)..." , Toast.LENGTH_LONG).show();
                else {
                    AsyncTaskAccount action = new AsyncTaskAccount();
                    action.execute(Constant.url + "/Account/LoginAccount");
                }
            }
        });
    }

    private class AsyncTaskAccount extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {

            account = new Account();
            account.setUsername(UserName);
            account.setPassword(Password);
            account.setDeviceid(DeviceId);

            return POST(urls[0],account);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("ErrorPassword"))
                Toast.makeText(getBaseContext(), "Your Password is error...", Toast.LENGTH_LONG).show();
            else
                if (result.equals("NotRegistered"))
                    Toast.makeText(getBaseContext(), "You are use this account on another device...", Toast.LENGTH_LONG).show();
                else
                    if(result.equals("NotExist"))
                        Toast.makeText(getBaseContext(), "You Don't have an account/nYou should create an account First..." ,Toast.LENGTH_LONG).show();
                    else {
                        DataPreferences.StorePrefrence("loginpassword", Password);
                        DataPreferences.StorePrefrence("license", result);
                        DataPreferences.StorePrefrence("LicensePageStartPrevious", "true");
                        DataPreferences.StorePrefrence("login", "true");
                        Intent intent = new Intent(getBaseContext(), WelcomePage.class);
                        startActivity(intent);
                        finish();
                    }
        }
    }

    private String POST(String url, Account account) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            json = this.serializer.deepSerialize(account);
        /*
            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("UserName", account.getUsername());
            jsonObject.accumulate("Password", account.getPassword());
            jsonObject.accumulate("DeviceId", account.getDeviceid());

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
        */
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = Constant.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

}
