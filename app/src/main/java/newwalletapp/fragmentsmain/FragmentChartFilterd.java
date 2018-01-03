package newwalletapp.fragmentsmain;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.adpter.ChartCategoryListAdpter;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.BeginPopStack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentChartFilterd extends android.support.v4.app.Fragment {


    private PieChart mChart;
    private Typeface tf;
    DataPrefrences dataPrefrences;
    String[] categories;
    ArrayList<HashMap<String,String>> mapArrayList;
    String currencySymbol;
    Context mContext;
    ListView listViewChartItems;


    public FragmentChartFilterd() {

    }
    @Override
    public void onAttach(Activity activity) {
        mContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }/*

    public static FragmentChartFilterd  newInstance(String currency,ArrayList<HashMap<String,String>> arrayListMap)
    {
        FragmentChartFilterd fragmentChartFilterd = new FragmentChartFilterd();
        Bundle b=new Bundle();
        b.putString("currency",currency);
        b.putSerializable("map",arrayListMap);
        return fragmentChartFilterd;

    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        Log.d("FragmentChartFilterd", "onActivityCreated");
        currencySymbol=getArguments().getString("currency");
        Log.d("FragmentChartFilterd",currencySymbol);
        mapArrayList= (ArrayList<HashMap<String, String>>) getArguments().getSerializable("map");
        Log.d("FragmentChartFilterd", "" + mapArrayList);
/*
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("FragmentChartFilterd","Create OptionsMenu");


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("FragmentChartFilterd","Clicked OptionsMenu");
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            Toast.makeText(getActivity(),"Hiii",Toast.LENGTH_LONG).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        return inflater.inflate(R.layout.fragment_filteredchart, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("FragmentChartFilterd","onActivityCreated");
        PieChart mChart = (PieChart)getActivity().findViewById(R.id.chartViewFiltered);
        listViewChartItems= (ListView) getActivity().findViewById(R.id.listViewFilteredChartItems);
        listViewChartItems.setAdapter(new ChartCategoryListAdpter(getActivity(),currencySymbol ,mapArrayList));

        //ListView listView=(ListView)getActivity().findViewById(R.id.listViewChartExpandableList);
        Typeface tf=Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mChart.setUsePercentValues(true);
        mChart.setHoleColorTransparent(true);
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




        PieDataSet dataSet;
        PieData data;
        DataPrefrences dataPrefrences;
        double totalAmount=0.0;


        for(int i=0;i<mapArrayList.size();i++)
        {
            totalAmount=totalAmount+Double.parseDouble(mapArrayList.get(i).get("amount"));
        }
        mChart.setCenterText("Amount "+totalAmount+currencySymbol);
        String[] categories = new String[mapArrayList.size()];
        String[] colors = new String[mapArrayList.size()];
        for (int i = 0; i < mapArrayList.size(); i++) {
            //log("categories[" + i + "] =" + mapArrayList.get(i).get("category"));
            categories[i] = mapArrayList.get(i).get("category");
        }
        for (int i = 0; i < mapArrayList.size(); i++) {
            //log("colors[" + i + "] =" + mapArrayList.get(i).get("color"));
            colors[i] = mapArrayList.get(i).get("color");
        }

        float mult = categories.length;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        for (int i = 0; i < categories.length; i++) {

            //yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
            yVals1.add(new Entry((float) ((totalAmount) * (Float.parseFloat(mapArrayList.get(i).get("amount")))) / 100, i));
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

        mChart.setData(data);
        mChart.invalidate();
        mChart.highlightValues(null);
        mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        mChart.invalidate();

    }
}
