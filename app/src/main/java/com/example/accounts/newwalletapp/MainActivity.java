package com.example.accounts.newwalletapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import newwalletapp.backup.ScheduleBackup;
import newwalletapp.database.DataPrefrences;
import newwalletapp.fragments.FragmentPassword;
import newwalletapp.fragmentsmain.FragmentAddBudget;
import newwalletapp.fragmentsmain.FragmentAddNewRecord;
import newwalletapp.fragmentsmain.FragmentBudget;
import newwalletapp.fragmentsmain.FragmentDashBoard;
import newwalletapp.fragmentsmain.FragmentDrawer;
import newwalletapp.interfaces.BeginPopStack;
import newwalletapp.interfaces.BudgetInterface;
import newwalletapp.interfaces.EditRecordTransaction;
import newwalletapp.interfaces.FilterInterface;
import newwalletapp.interfaces.UpdateDashboard;


public class MainActivity extends ActionBarActivity implements FilterInterface, BeginPopStack,BudgetInterface,EditRecordTransaction{

    Toolbar toolbar;
    public static String PACKAGE_NAME;
    FragmentManager fragmentManager;
    FragmentDrawer fragmentDrawer;
    Fragment fragmentDashBord = new FragmentDashBoard();
    //Fragment fragmentPassword = new FragmentPassword();
    DataPrefrences dataPrefrences;
    FragmentAddBudget fragmentAddBudget = new FragmentAddBudget();
    FragmentAddNewRecord fragmentAddNewRecord = new FragmentAddNewRecord();
    public static int FlagForBudgetBackpress=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        dataPrefrences = new DataPrefrences(getApplicationContext());
        if (dataPrefrences.GetStoredPrefrence("backup_interval").equals("N/A"))
        {
            dataPrefrences.StorePrefrence("backup_interval",getResources().getString(R.string.backup_weekly));
            ScheduleBackup scheduleBackup = new ScheduleBackup(getApplicationContext());
            scheduleBackup.changeAlarmInterval(getResources().getString(R.string.backup_weekly));
        }
        PACKAGE_NAME = getApplicationContext().getPackageName();
        dataPrefrences.StorePrefrence("package_name",PACKAGE_NAME);
        fragmentManager = getSupportFragmentManager();

        fragmentDrawer = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
        fragmentDrawer.setUp(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentDashBord).commit();//.addToBackStack("deshboard").commit();to call back the dashbord fragment

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("MainActivity", "onSaveInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "OnResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy()");
        dataPrefrences.StorePrefrence("selectedfragment", "N/A");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            if (fragmentDrawer.mDrawerToggle.isDrawerIndicatorEnabled()) {
                return fragmentDrawer.mDrawerToggle.onOptionsItemSelected(item);
            } else {
                onBackPressed();
                return true;
            }

        } else if (id == R.id.action_addnew_record) {
//            startActivity(new Intent(MainActivity.this, AddNewRecordActivity.class));
            fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);
            fragmentDrawer.mDrawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
            setSupportActionBar(toolbar);
            fragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            fragmentAddNewRecord.setArguments(null);
            if (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("ar")) {
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragment_container, fragmentAddNewRecord).addToBackStack("addnew").commit();
            }
            else
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragment_container, fragmentAddNewRecord).addToBackStack("addnew").commit();
            //fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up,R.anim.slide_down).replace(R.id.fragment_container, fragmentAddNewRecord).addToBackStack("addnew").commit();
            //.setCustomAnimations(R.animator.slide_up,R.animator.slide_down,R.animator.slide_up,R.animator.slide_down)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void makeEditRecordTransaction(Bundle b)
    {

        fragmentAddNewRecord = new FragmentAddNewRecord();
        fragmentAddNewRecord.setArguments(b);
        fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);
        fragmentDrawer.mDrawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        setSupportActionBar(toolbar);
        fragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("ar")) {
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragment_container, fragmentAddNewRecord).addToBackStack("addnew").commit();
        }
        else
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragment_container, fragmentAddNewRecord).addToBackStack("addnew").commit();
    }

    @Override
    public void makeFilterTransaction(Fragment fragment) {
        //fragmentDrawer.beginChartFilterTransaction(fragmentChartFilterd);
        //fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);
        fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);
        fragmentDrawer.mDrawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("ar")) {
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragment_container, fragment).addToBackStack("chartfilter").commit();
        }
        else
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragment_container, fragment).addToBackStack("chartfilter").commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FlagForBudgetBackpress == 0)
        {
            fragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        else
        {
            FlagForBudgetBackpress = 0;
        }

    }

    @Override
    public void beginBudgetTransaction(Bundle b) {

        if (b!=null)
        {
            FlagForBudgetBackpress=1;
        }
        fragmentDrawer.mDrawerToggle.setDrawerIndicatorEnabled(false);
        fragmentDrawer.mDrawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentDrawer.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fragmentAddBudget=new FragmentAddBudget();
        fragmentAddBudget.setArguments(b);
        if (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("ar")) {
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.fragment_container, fragmentAddBudget).addToBackStack("budget").commit();
        }
        else
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.fragment_container, fragmentAddBudget).addToBackStack("budget").commit();
    }

    @Override
    public void beginPopStackTransaction() {

    }
}
