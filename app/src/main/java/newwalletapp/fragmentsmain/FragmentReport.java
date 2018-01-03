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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import newwalletapp.adpter.ReportFilteredAdpter;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.backup.ExportReportData;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReport extends android.support.v4.app.Fragment {


    DataPrefrences dataPrefrences;
    ReadSQLiteData readSQLiteData;
    String currencySybol, interval;
    Menu menu;
    int flagForExportMenu = 0;
    LinearLayout linearLayoutReport;
    TextView textViewIncomeCount, textViewExpenseCount, textViewIncomeAverageDay, textViewExpenseAverageDay,
            textViewIncomeAverageRecord, textViewExpenseAverageRecord, textViewIncomeTotal, textViewExpenseTotal,
            textViewStartingBalance, textViewNetEndingBalance, textViewCashflow;

    public FragmentReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Reports");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataPrefrences = new DataPrefrences(getActivity());
        readSQLiteData = new ReadSQLiteData(getActivity());
        currencySybol = readSQLiteData.getCurrencySymbol();
        linearLayoutReport = (LinearLayout) getActivity().findViewById(R.id.linearLayoutReport);
        textViewIncomeCount = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeCount);
        textViewExpenseCount = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseCount);
        textViewIncomeAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageDay);
        textViewExpenseAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageDay);
        textViewIncomeAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageRecord);
        textViewExpenseAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageRecord);
        textViewIncomeTotal = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeTotal);
        textViewExpenseTotal = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseTotal);
        textViewStartingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordStartingBalance);
        textViewNetEndingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordNetEndingBalance);
        textViewCashflow = (TextView) getActivity().findViewById(R.id.textViewRecordCashFlow);
        interval = dataPrefrences.GetStoredPrefrence("filter_interval");
        new SetUpDataTask().execute(null, null, null);


    }

    @Override
    public void onResume() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.reports));
        super.onResume();
    }

    private class SetUpDataTask extends AsyncTask<Void, Void, Void> {

        TextView textViewIncomeCount, textViewExpenseCount, textViewIncomeAverageDay, textViewExpenseAverageDay,
                textViewIncomeAverageRecord, textViewExpenseAverageRecord, textViewIncomeTotal, textViewExpenseTotal,
                textViewStartingBalance, textViewNetEndingBalance, textViewCashflow;
        ReadSQLiteData readSQLiteData;
        DataPrefrences dataPrefrences;
        LinearLayout linearLayoutReport;
        ArrayList<String> groupArray;
        int incomeRecordCount;
        int dayCount;
        int expenseRecordCount;
        String currencySybol, interval;
        int FlagForNull;
        double totalIncomeForAllRecords;
        double totalExpenseForAllRecords;
        ListView listViewFiltered;
        ProgressDialog progressDialog;
        ReportFilteredAdpter reportFilteredAdpter;
        LinkedHashMap<String, HashMap<String, String>> mapListForExpand;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", "Loading Data...");
            linearLayoutReport = (LinearLayout) getActivity().findViewById(R.id.linearLayoutReport);
            listViewFiltered = (ListView) getActivity().findViewById(R.id.listViewFilteredReport);
            dataPrefrences = new DataPrefrences(getActivity());
            groupArray = new ArrayList<String>();
            readSQLiteData = new ReadSQLiteData(getActivity());
            interval = dataPrefrences.GetStoredPrefrence("filter_interval");
            if (interval.equals("N/A")) {
                textViewIncomeCount = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeCount);
                textViewExpenseCount = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseCount);
                textViewIncomeAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageDay);
                textViewExpenseAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageDay);
                textViewIncomeAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageRecord);
                textViewExpenseAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageRecord);
                textViewIncomeTotal = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeTotal);
                textViewExpenseTotal = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseTotal);
                textViewStartingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordStartingBalance);
                textViewNetEndingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordNetEndingBalance);
                textViewCashflow = (TextView) getActivity().findViewById(R.id.textViewRecordCashFlow);
            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            currencySybol = readSQLiteData.getCurrencySymbol();
            if (interval.equals("N/A")) {
                dayCount = readSQLiteData.getDayCountForAllRecords();
                if (dayCount != 0) {
                    log("Day Count =" + dayCount);
                    incomeRecordCount = readSQLiteData.getAllIncomeRecordCount();

                    expenseRecordCount = readSQLiteData.getAllExpenseRecordCount();
                    totalIncomeForAllRecords = readSQLiteData.getTotalOfAllIncome();
                    log("totalIncomeForAllRecords " + totalIncomeForAllRecords);
                    totalExpenseForAllRecords = readSQLiteData.getTotalOfAllExpense();
                    log("totalExpenseForAllRecords " + totalExpenseForAllRecords);
                    currencySybol = readSQLiteData.getCurrencySymbol();

                } else {
                    FlagForNull = 1;
                }
            } else if (interval.equals("day")) {

                mapListForExpand = readSQLiteData.getReportForDaily();
                Log.d("FragmentReport", "mapListForExpand" + mapListForExpand);
                Set set = mapListForExpand.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentChart", "Key =" + me.getKey());
                    Log.d("FragmentChart", "mapArrayList =" + mapListForExpand.get(me.getKey()));
                }

                reportFilteredAdpter = new ReportFilteredAdpter(getActivity(), groupArray, mapListForExpand);
                //chartListForFiltered=new ChartListForFiltered(getActivity(),groupArray,mapArrayListForExpand);


            } else if (interval.equals("week")) {
                mapListForExpand = readSQLiteData.getReportForWeekly();
                Log.d("FragmentReport", "mapListForExpand" + mapListForExpand);
                Set set = mapListForExpand.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentChart", "Key =" + me.getKey());
                    Log.d("FragmentChart", "mapArrayList =" + mapListForExpand.get(me.getKey()));
                }

                reportFilteredAdpter = new ReportFilteredAdpter(getActivity(), groupArray, mapListForExpand);

            } else if (interval.equals("month")) {
                mapListForExpand = readSQLiteData.getReportForMonthly();
                Log.d("FragmentReport", "mapListForExpand" + mapListForExpand);
                Set set = mapListForExpand.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentChart", "Key =" + me.getKey());
                    Log.d("FragmentChart", "mapArrayList =" + mapListForExpand.get(me.getKey()));
                }

                reportFilteredAdpter = new ReportFilteredAdpter(getActivity(), groupArray, mapListForExpand);

            } else if (interval.equals("year")) {
                mapListForExpand = readSQLiteData.getReportForYearly();
                Log.d("FragmentReport", "mapListForExpand" + mapListForExpand);
                Set set = mapListForExpand.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    groupArray.add(me.getKey() + "");
                    Log.d("FragmentChart", "Key =" + me.getKey());
                    Log.d("FragmentChart", "mapArrayList =" + mapListForExpand.get(me.getKey()));
                }

                reportFilteredAdpter = new ReportFilteredAdpter(getActivity(), groupArray, mapListForExpand);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            if (interval.equals("N/A")) {
                MenuItem menuItem = menu.findItem(R.id.action_export);
                menuItem.setVisible(true);
                flagForExportMenu=1;

                if (FlagForNull == 0) {
                    linearLayoutReport.setVisibility(View.VISIBLE);
                    listViewFiltered.setVisibility(View.INVISIBLE);
                    textViewIncomeCount.setText("" + incomeRecordCount);
                    textViewExpenseCount.setText("" + expenseRecordCount);
                    if (totalIncomeForAllRecords > 0) {
                        textViewIncomeAverageDay.setText("" + decimalFormat.format(totalIncomeForAllRecords / dayCount) + " " + currencySybol);
                        textViewIncomeAverageRecord.setText("" + decimalFormat.format(totalIncomeForAllRecords / incomeRecordCount) + " " + currencySybol);
                    } else {
                        textViewIncomeAverageDay.setText("0" + currencySybol);
                        textViewIncomeAverageRecord.setText("0" + currencySybol);
                    }

                    if (totalExpenseForAllRecords > 0) {
                        textViewExpenseAverageDay.setText("" + decimalFormat.format(totalExpenseForAllRecords / dayCount) + " " + currencySybol);
                        textViewExpenseAverageRecord.setText("" + decimalFormat.format(totalExpenseForAllRecords / expenseRecordCount) + " " + currencySybol);
                    } else {
                        textViewExpenseAverageDay.setText("0" + currencySybol);
                        textViewExpenseAverageRecord.setText("0" + currencySybol);
                    }


                    textViewIncomeTotal.setText("" + decimalFormat.format(totalIncomeForAllRecords) + " " + currencySybol);
                    textViewExpenseTotal.setText("" + decimalFormat.format(totalExpenseForAllRecords) + " " + currencySybol);
                    textViewStartingBalance.setText("0" + " " + currencySybol);
                    textViewNetEndingBalance.setText("" + decimalFormat.format(totalIncomeForAllRecords - totalExpenseForAllRecords) + " " + currencySybol);
                    textViewCashflow.setText("" + decimalFormat.format(totalIncomeForAllRecords - totalExpenseForAllRecords) + " " + currencySybol);

                } else {
                    linearLayoutReport.setVisibility(View.VISIBLE);
                    listViewFiltered.setVisibility(View.INVISIBLE);
                    textViewIncomeCount.setText("0");
                    textViewExpenseCount.setText("0");
                    textViewIncomeAverageDay.setText("0" + currencySybol);
                    textViewExpenseAverageDay.setText("0" + currencySybol);
                    textViewIncomeAverageRecord.setText("0" + currencySybol);
                    textViewExpenseAverageRecord.setText("0" + currencySybol);
                    textViewIncomeTotal.setText("0" + currencySybol);
                    textViewExpenseTotal.setText("0" + currencySybol);
                    textViewStartingBalance.setText("0" + " " + currencySybol);
                    textViewNetEndingBalance.setText("0" + currencySybol);
                    textViewCashflow.setText("0" + currencySybol);

                }


            } else {
                //menuItemExport.setVisible(false);
                MenuItem menuItem = menu.findItem(R.id.action_export);
                menuItem.setVisible(false);
                flagForExportMenu=0;
                linearLayoutReport.setVisibility(View.INVISIBLE);
                listViewFiltered.setVisibility(View.VISIBLE);
                listViewFiltered.setAdapter(reportFilteredAdpter);

            }


        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_records, menu);
        MenuItem menuItem = menu.findItem(R.id.action_export);
        if (this.menu==null){
            this.menu = menu;
            if (interval.equals("N/A")) {
                menuItem.setVisible(true);
                flagForExportMenu=1;
            } else {
                menuItem.setVisible(false);
                flagForExportMenu=0;
                log("onCreateOptionsMenu MenuItem action_export " + false);
            }
        }
        else{
            if (flagForExportMenu==0){
                menuItem.setVisible(false);
            }
            else{
                menuItem.setVisible(true);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            View menuItemView = getActivity().findViewById(R.id.action_filter);
            final PopupMenu popup = new PopupMenu(getActivity(), menuItemView);
            popup.getMenuInflater().inflate(R.menu.menu_filter_popup, popup.getMenu());

            MenuItem menuItem = popup.getMenu().findItem(R.id.action_filter_account);

            final ArrayList<HashMap<String, String>> arrayListAccount = readSQLiteData.getAllAccountsData();

            SubMenu subMenu = menuItem.getSubMenu();

            subMenu.addSubMenu(R.id.action_group_filter_account, 0, 0, "All");


            for (int i = 0; i < arrayListAccount.size(); i++) {

                subMenu.addSubMenu(R.id.action_group_filter_account, (i + 1), (i + 1), arrayListAccount.get(i).get("acc_name"));


            }
            subMenu.setGroupCheckable(R.id.action_group_filter_account, true, true);
            if (dataPrefrences.GetStoredPrefrence("filter_account").equals("N/A")) {
                popup.getMenu().findItem(0).setChecked(true);

            } else {
                String subMenuAccount = dataPrefrences.GetStoredPrefrence("filter_account");
                for (int i = 0; i < arrayListAccount.size(); i++) {
                    if (subMenuAccount.equals(arrayListAccount.get(i).get("acc_name"))) {
                        popup.getMenu().findItem(i + 1).setChecked(true);
                    }
                }
            }

            if (dataPrefrences.GetStoredPrefrence("filter_interval").equals("N/A")) {
                popup.getMenu().findItem(R.id.action_filter_interval_all).setChecked(true);


            } else if (dataPrefrences.GetStoredPrefrence("filter_interval").equals("day")) {
                popup.getMenu().findItem(R.id.action_filter_interval_day).setChecked(true);


            } else if (dataPrefrences.GetStoredPrefrence("filter_interval").equals("week")) {
                popup.getMenu().findItem(R.id.action_filter_interval_week).setChecked(true);
                popup.getMenu().findItem(R.id.action_filter_interval_day).setChecked(true);

            } else if (dataPrefrences.GetStoredPrefrence("filter_interval").equals("month")) {
                popup.getMenu().findItem(R.id.action_filter_interval_month).setChecked(true);

            } else if (dataPrefrences.GetStoredPrefrence("filter_interval").equals("year")) {
                popup.getMenu().findItem(R.id.action_filter_interval_year).setChecked(true);

            }

            if (dataPrefrences.GetStoredPrefrence("filter_category").equals("N/A")) {
                popup.getMenu().findItem(R.id.action_filter_category_all).setChecked(true);

            } else if (dataPrefrences.GetStoredPrefrence("filter_category").equals("income")) {
                popup.getMenu().findItem(R.id.action_filter_category_income).setChecked(true);

            } else if (dataPrefrences.GetStoredPrefrence("filter_category").equals("expense")) {
                popup.getMenu().findItem(R.id.action_filter_category_expence).setChecked(true);
            }


            if (dataPrefrences.GetStoredPrefrence("filter_sort").equals("N/A")) {
                popup.getMenu().findItem(R.id.action_filter_sort_new).setChecked(true);
            } else if (dataPrefrences.GetStoredPrefrence("filter_sort").equals("old")) {
                popup.getMenu().findItem(R.id.action_filter_sort_old).setChecked(true);
            } else if (dataPrefrences.GetStoredPrefrence("filter_sort").equals("high")) {
                popup.getMenu().findItem(R.id.action_filter_sort_high).setChecked(true);
            } else if (dataPrefrences.GetStoredPrefrence("filter_sort").equals("low")) {
                popup.getMenu().findItem(R.id.action_filter_sort_low).setChecked(true);
            }


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem itm) {
                    int item = itm.getItemId();


                    if (item == R.id.action_filter_interval_all) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval", "N/A");
                        new SetUpDataTask().execute(null, null, null);
                        MenuItem menuItemExport = menu.findItem(R.id.action_export);
                        menuItemExport.setVisible(true);


                    } else if (item == R.id.action_filter_interval_day) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval", "day");
                        new SetUpDataTask().execute(null, null, null);
                        MenuItem menuItemExport = menu.findItem(R.id.action_export);
                        menuItemExport.setVisible(false);

                    } else if (item == R.id.action_filter_interval_week) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval", "week");
                        new SetUpDataTask().execute(null, null, null);
                        MenuItem menuItemExport = menu.findItem(R.id.action_export);
                        menuItemExport.setVisible(false);

                    } else if (item == R.id.action_filter_interval_month) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval", "month");
                        new SetUpDataTask().execute(null, null, null);
                        MenuItem menuItemExport = menu.findItem(R.id.action_export);
                        menuItemExport.setVisible(false);

                    } else if (item == R.id.action_filter_interval_year) {

                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval", "year");
                        new SetUpDataTask().execute(null, null, null);
                        MenuItem menuItemExport = menu.findItem(R.id.action_export);
                        menuItemExport.setVisible(false);

                    } else if (item == R.id.action_filter_category_all) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category", "N/A");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_category_income) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category", "income");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_category_expence) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category", "expense");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_sort_new) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort", "N/A");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_sort_old) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort", "old");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_sort_high) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort", "high");
                        new SetUpDataTask().execute(null, null, null);

                    } else if (item == R.id.action_filter_sort_low) {
                        if (itm.isChecked()) {
                            itm.setChecked(false);
                        } else {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort", "low");
                        new SetUpDataTask().execute(null, null, null);

                    } else {
                        for (int i = 0; i < arrayListAccount.size() + 1; i++) {
                            if (itm.isChecked()) {
                                itm.setChecked(false);
                            } else {
                                itm.setChecked(true);
                            }

                            if (item == 0) {
                                dataPrefrences.StorePrefrence("filter_account", "N/A");
                                Log.d("Check Item ", "item index" + item);
                                new SetUpDataTask().execute(null, null, null);
                                break;
                            } else {
                                if (item == i) {
                                    dataPrefrences.StorePrefrence("filter_account", arrayListAccount.get(i - 1).get("acc_name"));
                                    Log.d("Check Item ", "item index" + item + "Account Name =" + arrayListAccount.get(i - 1).get("acc_name"));
                                    new SetUpDataTask().execute(null, null, null);
                                    break;
                                }
                            }
                        }
                    }
                    return true;
                }
            });

            popup.show();//showing popup menu

        } else if (id == R.id.action_export) {
            HashMap<String, String> exportMap = new HashMap<>();
            exportMap.put("income_count", textViewIncomeCount.getText().toString());
            exportMap.put("expense_count", textViewExpenseCount.getText().toString());
            exportMap.put("income_average_day", textViewIncomeAverageDay.getText().toString());
            exportMap.put("expense_average_day", textViewExpenseAverageDay.getText().toString());
            exportMap.put("income_average_record", textViewIncomeAverageRecord.getText().toString());
            exportMap.put("expense_average_record", textViewExpenseAverageRecord.getText().toString());
            exportMap.put("income_total", textViewIncomeTotal.getText().toString());
            exportMap.put("expense_total", textViewExpenseTotal.getText().toString());
            exportMap.put("starting_balance", textViewStartingBalance.getText().toString());
            exportMap.put("netending_balance", textViewNetEndingBalance.getText().toString());
            exportMap.put("cashflow", textViewCashflow.getText().toString());

            ExportReportData exportReportData = new ExportReportData(getActivity());
            String str = exportReportData.exportReport("", exportMap);
            Toast.makeText(getActivity(), getActivity().getString(R.string.toast_export) + " " + str, Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }


    void log(String msg) {
        Log.d("FragmentReport", "" + msg);
    }
}
