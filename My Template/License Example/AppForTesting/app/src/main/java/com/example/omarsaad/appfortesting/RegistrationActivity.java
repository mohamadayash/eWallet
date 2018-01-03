package com.example.omarsaad.appfortesting;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class RegistrationActivity extends Activity {

    String DeviceId,Name,UserName,Password,Email ;
    int LicenseTypeId;
    DTOAccount DTOaccount;
    EditText txtFName, txtLName, txtUserName, txtPassword, txtEmail;
    DataPreferences DataPreferences;
    private flexjson.JSONSerializer serializer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.serializer = new flexjson.JSONSerializer();


        LicenseTypeId = getIntent().getExtras().getInt("LicenseTypeId");

        DataPreferences = new DataPreferences(this);
        DeviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        txtFName = (EditText)findViewById(R.id.txt_fname);
        txtLName = (EditText)findViewById(R.id.txt_lname);
        txtUserName = (EditText)findViewById(R.id.txt_username);
        txtPassword = (EditText)findViewById(R.id.txt_password);
        txtEmail = (EditText) findViewById(R.id.txt_email);

        final TextView errormsg = (TextView)findViewById(R.id.txt_error);

        Button register = (Button)findViewById(R.id.reg_register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName = txtUserName.getText().toString();
                Name = txtFName.getText().toString() + " " + txtLName.getText().toString();
                Password = txtPassword.getText().toString();
                Email = txtEmail.getText().toString();

                if (txtFName.getText().toString().isEmpty() || txtLName.getText().toString().isEmpty() || txtUserName.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty()) {
                    errormsg.setText("Fill All the blancks to complete registration");
                } else {
                    errormsg.setText("");
                    AsyncTaskAccount action = new AsyncTaskAccount();
                    action.execute(Constant.url + "/Account/AddNewAccount");
                }
            }
        });

    }


    private class AsyncTaskAccount extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {

            DTOaccount = new DTOAccount();
            Account account = new Account();

            account.setName(Name);
            account.setUsername(UserName);
            account.setPassword(Password);
            account.setDeviceid(DeviceId);
            account.setEmail(Email);

            DTOaccount.account = account;

            DTOaccount.LicenseTypeId = LicenseTypeId;


            return POST(urls[0],DTOaccount);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("UserNameExist")){
                Toast.makeText(getBaseContext(), "This UserName is already exist..." , Toast.LENGTH_LONG).show();
            }
            else
            if(result.equals("DeviceExist")) {
                Toast.makeText(getBaseContext(), "This Device is already has an account...", Toast.LENGTH_LONG).show();
            }
            else {
                DataPreferences.StorePrefrence("license", result);
                DataPreferences.StorePrefrence("loginpassword", Password);
                DataPreferences.StorePrefrence("login","true");
                Intent intent = new Intent(getBaseContext(), WelcomePage.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private String POST(String url, DTOAccount DTOaccount) {
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            json = this.serializer.deepSerialize(DTOaccount);


            /*
            // 3. build jsonObject
            JSONObject jsonObjectaccount = new JSONObject();

            jsonObjectaccount.accumulate("Name", DTOaccount.account.getName());
            jsonObjectaccount.accumulate("UserName", DTOaccount.account.getUsername());
            jsonObjectaccount.accumulate("Password", DTOaccount.account.getPassword());
            jsonObjectaccount.accumulate("DeviceId", DTOaccount.account.getDeviceid());
            jsonObjectaccount.accumulate("Email", DTOaccount.account.getEmail());

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("account",jsonObjectaccount);
            jsonObject.accumulate("LicenseTypeId", DTOaccount.LicenseTypeId);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();*/

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
