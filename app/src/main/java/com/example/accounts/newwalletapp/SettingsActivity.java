package com.example.accounts.newwalletapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;

import newwalletapp.database.DataPrefrences;
import newwalletapp.fragments.FragmentAccounts;
import newwalletapp.fragments.FragmentAddCategory;
import newwalletapp.fragments.FragmentAddCurrency;
import newwalletapp.fragments.FragmentCategories;
import newwalletapp.fragments.FragmentCurrencies;
import newwalletapp.fragments.FragmentPassword;
import newwalletapp.fragments.FragmentSettings;
import newwalletapp.interfaces.AccountsInterface;
import newwalletapp.interfaces.AddCategory;
import newwalletapp.interfaces.AddCurrencyInterface;
import newwalletapp.interfaces.BeginPopStack;
import newwalletapp.interfaces.CategoriesInterface;
import newwalletapp.interfaces.CurrienciesInterface;
import newwalletapp.interfaces.DeleteCategory;
import newwalletapp.interfaces.DeleteCurrencyInterface;
import newwalletapp.interfaces.PasswordTransaction;


/**
 * Created by ahmedchoteri on 16-02-15.
 */
public class SettingsActivity extends ActionBarActivity implements AccountsInterface, CategoriesInterface, CurrienciesInterface, AddCategory, AddCurrencyInterface, BeginPopStack ,DeleteCurrencyInterface,DeleteCategory,PasswordTransaction {
    Toolbar toolbar;
    String Tag = "SettingsActivity";
    FragmentManager fragmentManager;
    FragmentSettings fragmentSettings = new FragmentSettings();
    FragmentAccounts fragmentAccounts = new FragmentAccounts();
    FragmentAddCurrency fragmentAddCurrency; //= new FragmentAddCurrency();
    FragmentCurrencies fragmentCurrencies;// = new FragmentCurrencies();
    FragmentCategories fragmentCategories;// = new FragmentCategories();
    FragmentAddCategory fragmentAddCategory;// = new FragmentAddCategory();
    FragmentPassword fragmentPassword;
    DataPrefrences dataPrefrences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.app_bar_preferences);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.layout_settings_container, fragmentSettings).addToBackStack("settings").commit();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {


            onBackPressed();

/*
            //

            if (fragmentManager.getBackStackEntryCount() >= 2) {
//                NavUtils.navigateUpFromSameTask(this);
                fragmentManager.beginTransaction();
                fragmentManager.popBackStackImmediate();

            } else {
                this.finish();
            }*/
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateAccountsList(Context context) {
        Log.d(Tag, "updateAccountsList Interface Called");

        fragmentAccounts.updateAccounts(context);
    }

    @Override
    public void beginAccountsFragmentTransaction() {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentAccounts).addToBackStack("accounts").commit();
    }

    @Override
    public void beginCategoriesFragmentTransaction() {

        fragmentCategories = new FragmentCategories();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentCategories).addToBackStack("categories").commit();

    }

    @Override
    public void beginCurrienciesFragmentTransaction() {

        fragmentCurrencies = new FragmentCurrencies();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentCurrencies).addToBackStack("currencies").commit();
    }


    @Override
    public void beginAddCategoryTransaction() {
        Log.d("SettingsActivity", "beginAddCategoryTransaction bundle =");
        fragmentAddCategory = new FragmentAddCategory();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentAddCategory).addToBackStack("addcategories").commit();
    }

    @Override
    public void beginAddCategoryTransaction(Bundle bundle) {


        fragmentAddCategory = new FragmentAddCategory();

        fragmentAddCategory.setArguments(bundle);

        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentAddCategory, "EditCategory").addToBackStack("addcategories").commit();

    }

    @Override
    public void beginAddCurrencyTransaction(Bundle b) {

        fragmentAddCurrency = new FragmentAddCurrency();
        fragmentAddCurrency.setArguments(b);
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentAddCurrency).addToBackStack("addcurrencies").commit();

    }


    @Override
    public void beginPopStackTransaction() {

        onBackPressed();
/*
       if (fragmentManager.getBackStackEntryCount() >= 2) {

            fragmentManager.beginTransaction();
            fragmentManager.popBackStackImmediate();

        } else {
            this.finish();
        }
*/

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("count", "Count = " + fragmentManager.getBackStackEntryCount());
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public void deleteCurrency(String id, String code, String name) {
        fragmentCurrencies.deleteCurrency(id, code, name);
    }

    @Override
    public void DeleteCategory(String id, String name) {
        fragmentCategories.deleteCategory(id, name);
    }

    @Override
    public void begingPassWordFragmentTransaction() {
        fragmentPassword=new FragmentPassword();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.layout_settings_container, fragmentPassword).addToBackStack("accounts").commit();

    }
}
