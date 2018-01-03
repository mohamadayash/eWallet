package com.example.omarsaad.appfortesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends Activity {

    DataPreferences DataPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        DataPreferences = new DataPreferences(this);

        Button nextActiv = (Button) findViewById(R.id.btnNext);
        nextActiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DataPreferences.GetStoredPrefrence("LicensePageStartPrevious").equals("true") && DataPreferences.GetStoredPrefrence("login").equals("true")
                        && !(DataPreferences.GetStoredPrefrence("license").equals("N/A")) && !(DataPreferences.GetStoredPrefrence("license").equals("")) )
                {
                    Intent intent = new Intent(getBaseContext(), WelcomePage.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}