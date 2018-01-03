package com.example.accounts.newwalletapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import newwalletapp.database.DataPrefrences;
import newwalletapp.extras.Constants;
import newwalletapp.fragments.FragmentPassword;
import newwalletapp.fragmentsmain.FragmentSplash;
import newwalletapp.interfaces.PasswordInterface;

public class SplashActivity  extends FragmentActivity implements PasswordInterface{

    Fragment fragmentSplash = new FragmentSplash();
    Fragment fragmentPassword = new FragmentPassword();
    DataPrefrences dataPrefrences;
    FragmentTransaction ft;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub

        super.onCreate(arg0);
        dataPrefrences = new DataPrefrences(getApplicationContext());
        if (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("N/A")) {
            Constants.SetLanguage(this, "ar");
            dataPrefrences.StorePrefrence("currentlanguage", "ar");
            setTitle(R.string.app_name);
        } else {
            String currentlanguage = dataPrefrences.GetStoredPrefrence("currentlanguage");
            Constants.SetLanguage(this, currentlanguage);
            setTitle(R.string.app_name);
        }

        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_gateway);

        ft = getSupportFragmentManager().beginTransaction();
        if (dataPrefrences.GetStoredPrefrence("password").equals("N/A")) {
            fragmentManager.beginTransaction().replace(R.id.gateway_content, fragmentSplash).commit();
/*
            ft.replace(R.id.gateway_content, fragmentSplash);
            ft.commit();
*/
        } else {
            Bundle b = new Bundle();
            b.putInt("flag", 1);
            fragmentPassword.setArguments(b);
            fragmentManager.beginTransaction().replace(R.id.gateway_content, fragmentPassword).commit();
/*
            ft.replace(R.id.gateway_content, fragmentPassword);
            ft.commit();
*/

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void beginTransactionFromPassword() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.gateway_content, fragmentSplash).commit();
/*
        ft.replace(R.id.gateway_content, fragmentSplash);
        ft.commit();
*/
    }
}