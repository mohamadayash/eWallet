package newwalletapp.fragmentsmain;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.accounts.newwalletapp.AddNewRecordActivity;
import com.example.accounts.newwalletapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import newwalletapp.adpter.RecordsExpandListAdpater;
import newwalletapp.adpter.RecordsListAdpter;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.EditRecordTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecords extends android.support.v4.app.Fragment {


   // HashMap<String, ArrayList<HashMap<String, String>>> mapRecords;

    ExpandableListView expandableListView;
    ListView listView;


    ReadSQLiteData readSQLiteData;
    DataPrefrences dataPrefrences;

    public FragmentRecords() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Records");
        super.onCreate(savedInstanceState);
        //getActivity().getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.records));
        new SetUpDataTask().execute(null,null,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getActivity().findViewById(R.id.listViewRecords);
        expandableListView = (ExpandableListView) getActivity().findViewById(R.id.expandableListViewRecors);
        dataPrefrences = new DataPrefrences(getActivity());
        readSQLiteData = new ReadSQLiteData(getActivity());

        //new SetUpDataTask().execute(null, null, null);


    }

    private class SetUpDataTask extends AsyncTask<Void,Void,Void>{
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList;
        ArrayList<HashMap<String, String>> arrayListRecords;
        ProgressDialog progressDialog;
        ArrayList<String> groupArray;
//        ExpandableListView expandableListView;
//        ListView listView;
        String interval;
        ReadSQLiteData readSQLiteData;
        DataPrefrences dataPrefrences;
        RecordsListAdpter recordsListAdpter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), getActivity().getResources().getString(R.string.loading_data));

//            listView= (ListView) getActivity().findViewById(R.id.listViewRecords);
//            expandableListView = (ExpandableListView) getActivity().findViewById(R.id.expandableListViewRecors);
            dataPrefrences=new DataPrefrences(getActivity());
            groupArray = new ArrayList<String>();
            readSQLiteData = new ReadSQLiteData(getActivity());
        }

        @Override
        protected Void doInBackground(Void... params) {
            interval = dataPrefrences.GetStoredPrefrence("filter_interval");

            if(interval.equals("N/A"))
            {

                arrayListRecords=readSQLiteData.getAllRecords();

            }
            else if(interval.equals("day"))
            {

                mapArrayList = readSQLiteData.getAllRecordsByDay();
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentRecord","Key ="+me.getKey());
                    Log.d("FragmentRecord","mapArrayList ="+mapArrayList.get(me.getKey()));
                }
            }
            else if(interval.equals("week"))
            {
                mapArrayList = readSQLiteData.getAllRecordsByWeek();
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();

                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentRecord","Key ="+me.getKey());
                    Log.d("FragmentRecord","mapArrayList ="+mapArrayList.get(me.getKey()));
                }
            }

            else if(interval.equals("month"))
            {

                mapArrayList = readSQLiteData.getAllRecordsByMonth();
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentRecord","Key ="+me.getKey());
                    Log.d("FragmentRecord","mapArrayList ="+mapArrayList.get(me.getKey()));
                }

            }
            else if(interval.equals("year"))
            {

                mapArrayList = readSQLiteData.getAllRecordsByYear();
                Set set = mapArrayList.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentRecord","Key ="+me.getKey());
                    Log.d("FragmentRecord","mapArrayList ="+mapArrayList.get(me.getKey()));
                }

            }
            return null;
        }

       @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           progressDialog.dismiss();
           Log.d("FragmentRecord","onPostExecute"+" dialogdismiss");
            if(interval.equals("N/A"))
            {
                Log.d("FragmentRecord","onPostExecute"+" interval.equals(N/A)");
                recordsListAdpter = new RecordsListAdpter(getActivity(),arrayListRecords);
                Log.d("FragmentRecord","onPostExecute"+" recordsListAdpter");
                listView.setVisibility(View.VISIBLE);
                Log.d("FragmentRecord","onPostExecute"+" listView.setVisibility(View.VISIBLE)");
                listView.setAdapter(recordsListAdpter);
                Log.d("FragmentRecord","onPostExecute"+" listView.setAdapter(recordsListAdpter)");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("FragmentRecord","List Item Click"+arrayListRecords.get(position));
                        EditRecordTransaction editRecordTransaction= (EditRecordTransaction) getActivity();
                        Bundle b=new Bundle();
                        b.putSerializable("map",arrayListRecords.get(position));
                        editRecordTransaction.makeEditRecordTransaction(b);

/*
                        Intent i=new Intent(getActivity(), AddNewRecordActivity.class);
                        i.putExtra("map",arrayListRecords.get(position));
                        startActivity(i);
*/
                    }
                });
                Log.d("FragmentRecord","onPostExecute"+" listView.setOnItemClickListener");
                expandableListView.setVisibility(View.INVISIBLE);

            }
            else
            {
                expandableListView.setAdapter(new RecordsExpandListAdpater(getActivity(), groupArray, mapArrayList));
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Log.d("FragmentRecord","Expandable ListView Group ="+groupPosition+" Child Position ="+childPosition);
                        EditRecordTransaction editRecordTransaction= (EditRecordTransaction) getActivity();
                        Bundle b=new Bundle();
                        b.putSerializable("map",mapArrayList.get(groupArray.get(groupPosition)).get(childPosition));
                        editRecordTransaction.makeEditRecordTransaction(b);

/*
                        Intent i=new Intent(getActivity(), AddNewRecordActivity.class);
                        i.putExtra("map",mapArrayList.get(groupArray.get(groupPosition)).get(childPosition));
                        startActivity(i);
*/

                        return true;
                    }
                });

                expandableListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_records, menu);
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

            final ArrayList<HashMap<String,String>> arrayListAccount = readSQLiteData.getAllAccountsData();

            SubMenu subMenu=menuItem.getSubMenu();

            subMenu.addSubMenu(R.id.action_group_filter_account,0,0,"All");


            for(int i=0;i<arrayListAccount.size();i++)
            {

                subMenu.addSubMenu(R.id.action_group_filter_account,(i+1),(i+1),arrayListAccount.get(i).get("acc_name"));


            }
            subMenu.setGroupCheckable(R.id.action_group_filter_account,true,true);
            if(dataPrefrences.GetStoredPrefrence("filter_account").equals("N/A"))
            {
                popup.getMenu().findItem(0).setChecked(true);
            }
            else{
                String subMenuAccount=dataPrefrences.GetStoredPrefrence("filter_account");
                for(int i=0;i<arrayListAccount.size();i++)
                {
                    if(subMenuAccount.equals(arrayListAccount.get(i).get("acc_name")))
                    {
                        popup.getMenu().findItem(i+1).setChecked(true);
                    }
                }
            }

            if(dataPrefrences.GetStoredPrefrence("filter_interval").equals("N/A"))
            {
                popup.getMenu().findItem(R.id.action_filter_interval_all).setChecked(true);

            }
            else if(dataPrefrences.GetStoredPrefrence("filter_interval").equals("day"))
            {
                popup.getMenu().findItem(R.id.action_filter_interval_day).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_interval").equals("week"))
            {
                popup.getMenu().findItem(R.id.action_filter_interval_week).setChecked(true);
            }

            else if(dataPrefrences.GetStoredPrefrence("filter_interval").equals("month"))
            {
                popup.getMenu().findItem(R.id.action_filter_interval_month).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_interval").equals("year"))
            {
                popup.getMenu().findItem(R.id.action_filter_interval_year).setChecked(true);
            }

            if(dataPrefrences.GetStoredPrefrence("filter_category").equals("N/A"))
            {
                popup.getMenu().findItem(R.id.action_filter_category_all).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_category").equals("income"))
            {
                popup.getMenu().findItem(R.id.action_filter_category_income).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_category").equals("expense"))
            {
                popup.getMenu().findItem(R.id.action_filter_category_expence).setChecked(true);
            }


            if(dataPrefrences.GetStoredPrefrence("filter_sort").equals("N/A"))
            {
                popup.getMenu().findItem(R.id.action_filter_sort_new).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_sort").equals("old"))
            {
                popup.getMenu().findItem(R.id.action_filter_sort_old).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_sort").equals("high"))
            {
                popup.getMenu().findItem(R.id.action_filter_sort_high).setChecked(true);
            }
            else if(dataPrefrences.GetStoredPrefrence("filter_sort").equals("low"))
            {
                popup.getMenu().findItem(R.id.action_filter_sort_low).setChecked(true);
            }


            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem itm) {
                     int item=itm.getItemId();



                    if(item==R.id.action_filter_interval_all)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval","N/A");
                        new SetUpDataTask().execute(null,null,null);

                    }
                    else if(item==R.id.action_filter_interval_day)
                    {

                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval","day");
                        new SetUpDataTask().execute(null, null, null);
                    }
                    else if(item==R.id.action_filter_interval_week)
                    {

                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval","week");
                        new SetUpDataTask().execute(null, null, null);
                    }
                    else if(item==R.id.action_filter_interval_month)
                    {

                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval","month");
                        new SetUpDataTask().execute(null, null, null);
                    }
                    else if(item==R.id.action_filter_interval_year)
                    {

                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_interval","year");
                        new SetUpDataTask().execute(null, null, null);
                    }
                    else if(item==R.id.action_filter_category_all)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category","N/A");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_category_income)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category","income");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_category_expence)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_category","expense");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_sort_new)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort","N/A");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_sort_old)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort","old");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_sort_high)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort","high");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else if(item==R.id.action_filter_sort_low)
                    {
                        if(itm.isChecked())
                        {
                            itm.setChecked(false);
                        }
                        else
                        {
                            itm.setChecked(true);
                        }
                        dataPrefrences.StorePrefrence("filter_sort","low");
                        new SetUpDataTask().execute(null, null, null);

                    }
                    else{
                        for (int i=0;i<arrayListAccount.size()+1;i++)
                        {
                            if(itm.isChecked())
                            {
                                itm.setChecked(false);
                            }
                            else
                            {
                                itm.setChecked(true);
                            }

                            if(item==0)
                            {
                                dataPrefrences.StorePrefrence("filter_account","N/A");
                                Log.d("Check Item ","item index"+item);
                                new SetUpDataTask().execute(null, null, null);
                                break;

                            }
                            else{
                               if(item==i)
                               {
                                   dataPrefrences.StorePrefrence("filter_account",arrayListAccount.get(i-1).get("acc_name"));
                                   Log.d("Check Item ","item index"+item+"Account Name ="+arrayListAccount.get(i-1).get("acc_name"));
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

        }



        return super.onOptionsItemSelected(item);
    }
}
