package newwalletapp.fragmentsmain;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.example.accounts.newwalletapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import newwalletapp.adpter.ChartCategoryListAdpter;
import newwalletapp.adpter.ChartExpandListAdpater;
import newwalletapp.adpter.ChartListForFiltered;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChart extends android.support.v4.app.Fragment {


    private PieChart mChart;
    private Typeface tf;
    DataPrefrences dataPrefrences;
    String[] categories;
    ReadSQLiteData readSQLiteData;
    ListView listViewChartItems;


    public FragmentChart() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chart, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataPrefrences = new DataPrefrences(getActivity());
        readSQLiteData = new ReadSQLiteData(getActivity());
        listViewChartItems= (ListView) getActivity().findViewById(R.id.listViewChartItems);
        mChart = (PieChart) getActivity().findViewById(R.id.chartView);
        mChart.setUsePercentValues(true);
        mChart.setHoleColorTransparent(true);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        mChart.setDescription("");

        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // mChart.setUnit(" â‚¬");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {

            }

            @Override
            public void onNothingSelected() {

            }
        });
        // mChart.setTouchEnabled(false);



        //setData(dataPrefrences.GetStoredPrefrence("filter_interval"));
        new SetUpDataTask().execute(null,null,null);

        mChart.animateXY(1500, 1500);

        // mChart.spin(2000, 0, 360);

/*
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setTypeface(tf);

        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setFormSize(12);
        l.setTextSize(10);
*/

    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(          getActivity().getResources().getString(R.string.chart));
    }

    private void setData(String filter) {
        if (filter.equals("N/A")) {
            mChart.setCenterText("All Records");
            mChart.setCenterTextSize(20);
            mChart.setDrawSliceText(false);
            ArrayList<HashMap<String, String>> mapArrayList = readSQLiteData.getCategoriesFromRecords();
            listViewChartItems.setAdapter(new ChartCategoryListAdpter(getActivity(),readSQLiteData.getCurrencySymbol(),mapArrayList));

            if (mapArrayList.size()>0)
            {
                log("mapArrayList.size() = "+mapArrayList.size());
                String[] categories=new String[mapArrayList.size()];
                String[] colors=new String[mapArrayList.size()];
                for(int i=0;i<mapArrayList.size();i++)
                {
                    log("categories["+i+"] ="+mapArrayList.get(i).get("category"));
                    categories[i]=mapArrayList.get(i).get("category");
                }
                for (int i=0;i<mapArrayList.size();i++)
                {
                    log("colors["+i+"] ="+mapArrayList.get(i).get("color"));
                    colors[i]=mapArrayList.get(i).get("color");
                }

                    float mult = categories.length;

                ArrayList<Entry> yVals1 = new ArrayList<Entry>();

                // IMPORTANT: In a PieChart, no values (Entry) should have the same
                // xIndex (even if from different DataSets), since no values can be
                // drawn above each other.
                double totalIncome=readSQLiteData.getTotalOfAllIncome();
                double totalExpense=readSQLiteData.getTotalOfAllExpense();
                for (int i = 0; i < categories.length; i++) {

                    //yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
                    yVals1.add(new Entry((float) ((totalExpense+totalIncome)*(Float.parseFloat(mapArrayList.get(i).get("amount"))))/100, i));
                }


                ArrayList<String> xVals = new ArrayList<String>();

                for (int i = 0; i < categories.length; i++)
                {
                    //xVals.add(mParties[i % mParties.length]);
                    xVals.add(categories[i]);

                }


                PieDataSet dataSet = new PieDataSet(yVals1, "Records");
                dataSet.setSliceSpace(1f);

                // add a lot of colors

                ArrayList<Integer> colorsList = new ArrayList<Integer>();

                for (int i=0;i<colors.length;i++)
                {
                    colorsList.add(Color.parseColor(colors[i]));
                }
               colorsList.add(ColorTemplate.getHoloBlue());

                dataSet.setColors(colorsList);

                PieData data = new PieData(xVals, dataSet);

                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);
                data.setValueTypeface(tf);
                mChart.setData(data);

                // undo all highlights
                mChart.highlightValues(null);

                mChart.invalidate();



            }
            else
            {
                mChart.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class SetUpDataTask extends AsyncTask<Void,Void,Void>{

        private PieChart mChart;
        private Typeface tf;
        PieDataSet dataSet;
        PieData data;
        String interval;
        //ExpandableListView expandableListView;
        ListView listViewFilterItems;
        ProgressDialog progressDialog;
        LinearLayout linearLayoutSingleChart;
        DataPrefrences dataPrefrences;
        double totalIncome,totalExpense;
        ReadSQLiteData readSQLiteData;
        int FlagForNull=0;
        ListView listViewChartItems;
        ArrayList<HashMap<String, String>> mapArrayList;
        ArrayList<String> groupArray;
        ChartListForFiltered chartListForFiltered;
        ChartExpandListAdpater chartExpandListAdpater;
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayListForExpand;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", "Loading Data...");
            dataPrefrences = new DataPrefrences(getActivity());
            groupArray=new ArrayList<String>();
            interval=dataPrefrences.GetStoredPrefrence("filter_interval");
            listViewFilterItems=(ListView)getActivity().findViewById(R.id.listViewFilteredChart);
            //expandableListView= (ExpandableListView) getActivity().findViewById(R.id.expandableListViewChart);
            linearLayoutSingleChart= (LinearLayout) getActivity().findViewById(R.id.linearLayoutSingleChart);
            readSQLiteData = new ReadSQLiteData(getActivity());

            if(interval.equals("N/A"))
            {
                linearLayoutSingleChart.setVisibility(View.VISIBLE);
                //expandableListView.setVisibility(View.INVISIBLE);
                listViewFilterItems.setVisibility(View.INVISIBLE);

                listViewChartItems= (ListView) getActivity().findViewById(R.id.listViewChartItems);
                mChart = (PieChart) getActivity().findViewById(R.id.chartView);
                mChart.setUsePercentValues(true);
                mChart.setHoleColorTransparent(true);
                tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
                mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf"));
                mChart.setHoleRadius(45f);
                mChart.setTransparentCircleRadius(50f);
                mChart.setDescription("");
                mChart.setDrawCenterText(true);
                mChart.setDrawHoleEnabled(true);
                mChart.setRotationAngle(0);
                mChart.setRotationEnabled(true);
                mChart.setCenterText("All Records");
                mChart.setCenterTextSize(20);
                mChart.setDrawSliceText(false);

            }
            else{
                Log.d("FragmentChart","interval.equals(N/A)");
                linearLayoutSingleChart.setVisibility(View.INVISIBLE);
                //expandableListView.setVisibility(View.VISIBLE);
                listViewFilterItems.setVisibility(View.VISIBLE);

            }

        }

        @Override
        protected Void doInBackground(Void... params) {

            if(interval.equals("N/A")) {
                mapArrayList = readSQLiteData.getCategoriesFromRecords();
                //log("readSQLiteData.getCategoriesFromRecordsYear() "+readSQLiteData.getCategoriesFromRecordsYear());
                totalIncome = readSQLiteData.getTotalOfAllIncome();
                totalExpense = readSQLiteData.getTotalOfAllExpense();
                if(totalExpense!=0 || totalIncome!=0)
                {
                    if (mapArrayList.size() > 0) {
                        log("mapArrayList.size() = " + mapArrayList.size());
                        String[] categories = new String[mapArrayList.size()];
                        String[] colors = new String[mapArrayList.size()];
                        for (int i = 0; i < mapArrayList.size(); i++) {
                            log("categories[" + i + "] =" + mapArrayList.get(i).get("category"));
                            categories[i] = mapArrayList.get(i).get("category");
                        }
                        for (int i = 0; i < mapArrayList.size(); i++) {
                            log("colors[" + i + "] =" + mapArrayList.get(i).get("color"));
                            colors[i] = mapArrayList.get(i).get("color");
                        }

                        float mult = categories.length;

                        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

                        // IMPORTANT: In a PieChart, no values (Entry) should have the same
                        // xIndex (even if from different DataSets), since no values can be
                        // drawn above each other.

                        for (int i = 0; i < categories.length; i++) {
                            //yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
                            yVals1.add(new Entry((float) ((totalExpense + totalIncome) * (Float.parseFloat(mapArrayList.get(i).get("amount")))) / 100, i));
                        }
                        ArrayList<String> xVals = new ArrayList<String>();

                        for (int i = 0; i < categories.length; i++) {
                            //xVals.add(mParties[i % mParties.length]);
                            xVals.add(categories[i]);

                        }
                        dataSet = new PieDataSet(yVals1, "Records");
                        dataSet.setSliceSpace(1f);

                        // add a lot of colors

                        ArrayList<Integer> colorsList = new ArrayList<Integer>();

                        for (int i = 0; i < colors.length; i++) {
                            colorsList.add(Color.parseColor(colors[i]));
                        }
                        colorsList.add(ColorTemplate.getHoloBlue());

                        dataSet.setColors(colorsList);
                        data = new PieData(xVals, dataSet);

                        data.setValueFormatter(new PercentFormatter());
                        data.setValueTextSize(11f);
                        data.setValueTextColor(Color.WHITE);
                        data.setValueTypeface(tf);
                    }

                }
                else
                {
                    FlagForNull=1;
                }
            }
            else if(interval.equals("day"))
            {
                mapArrayListForExpand=readSQLiteData.getCategoriesFromRecordsDay();
                Log.d("FragmentChart","mapArrayListForExpand"+mapArrayListForExpand);
                Set set = mapArrayListForExpand.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentChart","Key ="+me.getKey());
                    Log.d("FragmentChart","mapArrayList ="+mapArrayListForExpand.get(me.getKey()));
                }

                chartListForFiltered=new ChartListForFiltered(getActivity(),groupArray,mapArrayListForExpand);
                //chartExpandListAdpater=new ChartExpandListAdpater(getActivity(),groupArray,mapArrayListForExpand);
            }
            else if(interval.equals("week"))
            {
                mapArrayListForExpand=readSQLiteData.getCategoriesFromRecordsWeek();
                Log.d("FragmentChart","mapArrayListForExpand"+mapArrayListForExpand);
                Set set = mapArrayListForExpand.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentChart","Key ="+me.getKey());
                    Log.d("FragmentChart","mapArrayList ="+mapArrayListForExpand.get(me.getKey()));
                }
                chartListForFiltered=new ChartListForFiltered(getActivity(),groupArray,mapArrayListForExpand);
                //chartExpandListAdpater=new ChartExpandListAdpater(getActivity(),groupArray,mapArrayListForExpand);

            }
            else if(interval.equals("month"))
            {
                mapArrayListForExpand=readSQLiteData.getCategoriesFromRecordsMonth();
                Log.d("FragmentChart","mapArrayListForExpand"+mapArrayListForExpand);
                Set set = mapArrayListForExpand.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentChart","Key ="+me.getKey());
                    Log.d("FragmentChart","mapArrayList ="+mapArrayListForExpand.get(me.getKey()));
                }
                chartListForFiltered=new ChartListForFiltered(getActivity(),groupArray,mapArrayListForExpand);
                //chartExpandListAdpater=new ChartExpandListAdpater(getActivity(),groupArray,mapArrayListForExpand);

            }
            else if(interval.equals("year"))
            {
                mapArrayListForExpand=readSQLiteData.getCategoriesFromRecordsYear();
                Log.d("FragmentChart","mapArrayListForExpand"+mapArrayListForExpand);
                Set set = mapArrayListForExpand.entrySet();
                Iterator i = set.iterator();
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry)i.next();
                    groupArray.add(me.getKey()+"");
                    Log.d("FragmentChart","Key ="+me.getKey());
                    Log.d("FragmentChart","mapArrayList ="+mapArrayListForExpand.get(me.getKey()));
                }
                chartListForFiltered=new ChartListForFiltered(getActivity(),groupArray,mapArrayListForExpand);
                //chartExpandListAdpater=new ChartExpandListAdpater(getActivity(),groupArray,mapArrayListForExpand);

            }




            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(interval.equals("N/A")) {

                if(FlagForNull==0)
                {
                    listViewChartItems.setAdapter(new ChartCategoryListAdpter(getActivity(), readSQLiteData.getCurrencySymbol(), mapArrayList));
                    mChart.setData(data);

                    mChart.highlightValues(null);
                    mChart.animateXY(1500, 1500);
                    // mChart.spin(2000, 0, 360);
                    Legend l = mChart.getLegend();
                    l.setEnabled(false);
                    mChart.invalidate();
                }

                // undo all highlights


            }
            else{

                listViewFilterItems.setAdapter(chartListForFiltered);
                //expandableListView.setAdapter(chartExpandListAdpater);
            }

        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_records,menu);
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

            MenuItem menuItem=popup.getMenu().findItem(R.id.action_filter_account);

            final ArrayList<HashMap<String,String>> arrayListAccount=readSQLiteData.getAllAccountsData();

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
    public void log(String msg)
    {
        Log.d("FragmentChart",msg);
    }
}
