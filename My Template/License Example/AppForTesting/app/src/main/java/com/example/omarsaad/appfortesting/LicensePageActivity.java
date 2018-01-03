package com.example.omarsaad.appfortesting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.ArrayList;

import flexjson.JSONDeserializer;

public class LicensePageActivity extends Activity {

    ArrayList<LicenseType> arrlicensetypes ;
    RadioGroup rg ;
    DataPreferences DataPreferences ;
    String device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_page);

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        DataPreferences = new DataPreferences(this);

        AsyncTaskGetAvailableLicense action = new AsyncTaskGetAvailableLicense();
        action.execute(Constant.url + "/LicenseType?deviceid=" + device_id );

    }


    private class AsyncTaskGetAvailableLicense extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            return Constant.GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result == "")
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
            else
                GetAvailableLicense(result);
        }
    }

    private void GetAvailableLicense(String result) {

        arrlicensetypes = new JSONDeserializer<ArrayList<LicenseType>>().deserialize(result);
        int nbOflicensetype = arrlicensetypes.size();

        RadioButton[] rb = new RadioButton[nbOflicensetype];
        rg = (RadioGroup)findViewById(R.id.radiogroup);
        rg.setOrientation(RadioGroup.VERTICAL);

        for(int i=0 ; i < nbOflicensetype;i++){
            rb[i] = new RadioButton(getBaseContext());
            rb[i].setText(arrlicensetypes.get(i).getType());
            rb[i].setId(i);
            rb[i].setTextSize(18);
            rb[i].setTag(arrlicensetypes.get(i));
            rb[i].setTextColor(Color.BLACK);
            rg.addView(rb[i]);
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {

                final LicenseType lt = (LicenseType)(rg.getChildAt(checkedId)).getTag();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LicensePageActivity.this);
                // set title
                alertDialogBuilder.setTitle("Your License");
                // set dialog message
                alertDialogBuilder.setMessage("Click yes take your license For " + lt.getType());

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DataPreferences.StorePrefrence("LicensePageStartPrevious", "true");
                        if(DataPreferences.GetStoredPrefrence("login").equals("true")){

                            AsyncTaskGetAnotherLicense action = new AsyncTaskGetAnotherLicense();
                            action.execute(Constant.url + "/LicenseType/GetAnotherLicense?DeviceId=" + device_id + "&LicenseTypeId=" + lt.getLicenseTypeId());

                        }else{
                            Intent intent = new Intent(LicensePageActivity.this, RegistrationActivity.class);
                            intent.putExtra("LicenseTypeId", lt.getLicenseTypeId());
                            startActivity(intent);
                            LicensePageActivity.this.finish();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    private class AsyncTaskGetAnotherLicense extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            return Constant.GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result == "")
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();
            else {
                DataPreferences.StorePrefrence("license",result);
                Intent intent = new Intent(LicensePageActivity.this, WelcomePage.class);
                startActivity(intent);
                LicensePageActivity.this.finish();
            }
        }
    }
}
