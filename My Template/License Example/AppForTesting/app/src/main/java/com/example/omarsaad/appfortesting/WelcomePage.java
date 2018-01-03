package com.example.omarsaad.appfortesting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Date;

public class WelcomePage extends Activity {
    DataPreferences DataPreferences;
    public String device_id ,ct;
    public Long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        DataPreferences = new DataPreferences(this);

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        time = System.currentTimeMillis();
        ct = String.valueOf(time);

        String lisc = DataPreferences.GetStoredPrefrence("license");

        GetLicenseEndDate(lisc);

    }

    private void GetLicenseEndDate(String result) {
        try{
            DataPreferences.StorePrefrence("license", result);
            String text = Constant.Decrypt(result, "My Key");
            String[] lic = text.split("♀☺♀");
            String username = lic[0];
            String key_id = lic[1];
            String end_date = lic[2];
            if(key_id.equals(device_id) && end_date.compareTo(ct) > 0){
                Date date1,date2;
                date1 = Constant.getDate(Long.valueOf(ct), "dd/MM/yyyy hh:mm:ss");
                date2 = Constant.getDate(Long.valueOf(end_date), "dd/MM/yyyy hh:mm:ss") ;
                Toast.makeText(this, "you have " + Constant.DifferenceTwoDate(date1, date2) + "for ending this license app", Toast.LENGTH_LONG).show();
            }
            else
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle("Purchase License ");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes purchase a new license")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Intent intent = new Intent(Intent.ACTION_VIEW);
                                //intent.setData(Uri.parse("market://details?id=net.ehorizon.tel"));
                                Intent intent = new Intent(WelcomePage.this, LicensePageActivity.class);
                                startActivity(intent);
                                WelcomePage.this.finish();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                                WelcomePage.this.finish();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }catch (Exception e)
        {
            e.getMessage();
        }
    }

}