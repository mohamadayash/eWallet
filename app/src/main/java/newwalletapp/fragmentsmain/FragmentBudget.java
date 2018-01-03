package newwalletapp.fragmentsmain;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import newwalletapp.adpter.BudgetIntervalAdpter;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.BudgetInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBudget extends android.support.v4.app.Fragment {


    ReadSQLiteData readSQLiteData;
    DataPrefrences dataPrefrences;

    public FragmentBudget() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readSQLiteData = new ReadSQLiteData(getActivity());
        dataPrefrences = new DataPrefrences(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem menuItem=menu.findItem(R.id.action_addnew_record);
        menuItem.setVisible(false);

        inflater.inflate(R.menu.menu_budget, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.budgets));
        new SetUpDataTask().execute(null,null,null);
    }

    private class SetUpDataTask extends AsyncTask<Void, Void, Void> {

        DataPrefrences dataPrefrences;
        ReadSQLiteData readSQLiteData;
        ListView listViewBudgetIntervalList;
        BudgetIntervalAdpter budgetIntervalAdpter;
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList;
        ArrayList<String> groupArray;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", "Loading Data...");
            dataPrefrences = new DataPrefrences(getActivity());
            readSQLiteData = new ReadSQLiteData(getActivity());
            groupArray = new ArrayList<String>();
            listViewBudgetIntervalList = (ListView) getActivity().findViewById(R.id.listViewBudgetList);
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("N/A") || dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("week")) {
                mapArrayList = readSQLiteData.getWeeklyBudget();
                Log.d("FragmentBudget", "mapList = " + mapArrayList);
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentRecord", "Key =" + me.getKey());
                    Log.d("FragmentRecord", "mapArrayList =" + mapArrayList.get(me.getKey()));
                }

            } else if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("month")) {
                mapArrayList = readSQLiteData.getMonthlyBudget();
                Log.d("FragmentBudget", "mapList = " + mapArrayList);
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentRecord", "Key =" + me.getKey());
                    Log.d("FragmentRecord", "mapArrayList =" + mapArrayList.get(me.getKey()));
                }


            } else if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("year")) {
                mapArrayList = readSQLiteData.getYearlyBudget();
                Log.d("FragmentBudget", "mapList = " + mapArrayList);
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentRecord", "Key =" + me.getKey());
                    Log.d("FragmentRecord", "mapArrayList =" + mapArrayList.get(me.getKey()));
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
             progressDialog.dismiss();
            budgetIntervalAdpter = new BudgetIntervalAdpter(getActivity(), groupArray, mapArrayList);
            listViewBudgetIntervalList.setAdapter(budgetIntervalAdpter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            BudgetInterface budgetInterface = (BudgetInterface) getActivity();
            budgetInterface.beginBudgetTransaction(null);
        } else if (id == R.id.action_filter) {
            View menuItemView = getActivity().findViewById(R.id.action_filter);
            final PopupMenu popup = new PopupMenu(getActivity(), menuItemView);
            popup.getMenuInflater().inflate(R.menu.menu_budget_filter_popup, popup.getMenu());
            MenuItem menuItem = popup.getMenu().findItem(R.id.action_filter_account);
            final ArrayList<HashMap<String, String>> arrayListAccount = readSQLiteData.getAllAccountsData();
            SubMenu subMenu = menuItem.getSubMenu();

            subMenu.addSubMenu(R.id.action_group_filter_account, 0, 0, "All");
            for (int i = 0; i < arrayListAccount.size(); i++) {

                subMenu.addSubMenu(R.id.action_group_filter_account, (i + 1), (i + 1), arrayListAccount.get(i).get("acc_name"));


            }
            subMenu.setGroupCheckable(R.id.action_group_filter_account, true, true);
            String subMenuAccount = dataPrefrences.GetStoredPrefrence("budget_account_filter");
            if (subMenuAccount.equals("N/A")) {
                popup.getMenu().findItem(0).setChecked(true);
            } else {
                for (int i = 0; i < arrayListAccount.size(); i++) {
                    if (subMenuAccount.equals(arrayListAccount.get(i).get("acc_name"))) {
                        popup.getMenu().findItem(i+1).setChecked(true);
                        break;
                    }
                }
            }
            if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("N/A")) {
                popup.getMenu().findItem(R.id.action_filter_interval_week).setChecked(true);
                dataPrefrences.StorePrefrence("filter_budget_interval", "week");
            } else if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("week")) {
                popup.getMenu().findItem(R.id.action_filter_interval_week).setChecked(true);
            } else if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("month")) {
                popup.getMenu().findItem(R.id.action_filter_interval_month).setChecked(true);
            } else if (dataPrefrences.GetStoredPrefrence("filter_budget_interval").equals("year")) {
                popup.getMenu().findItem(R.id.action_filter_interval_year).setChecked(true);
            }


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem itm) {
                    int item = itm.getItemId();
                    if (item == R.id.action_filter_interval_week) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_budget_interval", "week");
                         new SetUpDataTask().execute(null, null, null);
                    } else if (item == R.id.action_filter_interval_month) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_budget_interval", "month");
                        new SetUpDataTask().execute(null, null, null);
                    } else if (item == R.id.action_filter_interval_year) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_budget_interval", "year");
                        new SetUpDataTask().execute(null, null, null);
                    } else {
                        for (int i = 0; i < arrayListAccount.size() + 1; i++) {
                            if (itm.isChecked()) {
                                itm.setChecked(false);
                            } else {
                                itm.setChecked(true);
                            }

                            if(item==0)
                            {
                                dataPrefrences.StorePrefrence("budget_account_filter","N/A");
                                Log.d("Check Item ", "All Accounts");
                                 new SetUpDataTask().execute(null, null, null);
                                break;

                            }
                            else {
                                if (item == i) {
                                    dataPrefrences.StorePrefrence("budget_account_filter", arrayListAccount.get(i-1).get("acc_name"));
                                    Log.d("Check Item ", "item index" + item + "Account Name =" + arrayListAccount.get(i-1).get("acc_name"));
                                    new SetUpDataTask().execute(null, null, null);
                                    break;

                                }

                            }

                        }
                    }


                    return true;
                }
            });
            popup.show();

        }


        return super.onOptionsItemSelected(item);
    }
}
