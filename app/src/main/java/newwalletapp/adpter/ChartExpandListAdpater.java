package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;

/**
 * Created by ahmedchoteri on 15-03-15.
 */
public class ChartExpandListAdpater extends BaseExpandableListAdapter {

    Context context;
    ArrayList<String> arrayListGroupItems;
    HashMap<String, ArrayList<HashMap<String, String>>> arrayListMapItems;
    String currencySymbol;
    public ChartExpandListAdpater(Context con, ArrayList<String> groupArray, HashMap<String,ArrayList<HashMap<String,String>>> arrayListMap) {

        this.context = con;
        this.arrayListMapItems = arrayListMap;
        ReadSQLiteData readSQLiteData=new ReadSQLiteData(context);
        currencySymbol=readSQLiteData.getCurrencySymbol();
        log("arrayListMap ="+arrayListMap);
        this.arrayListGroupItems = groupArray;
        log("groupArray ="+groupArray);
        if(arrayListGroupItems==null)
        {
            arrayListGroupItems=new ArrayList<String>();
            arrayListGroupItems.add("Data Sorted by Day");
        }
    }

    @Override
    public int getGroupCount() {
        log("getGroupCount ="+ arrayListGroupItems.size());
        return arrayListGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        log("getChildrenCount "+groupPosition+" ="+arrayListMapItems.get(this.arrayListGroupItems.get(groupPosition)).size());
        return 1;
        //return arrayListGroupItems.size();
        //return this.arrayListMapItems.get(this.arrayListGroupItems.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = arrayListGroupItems.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.groupitem_record, null);
        }
        LinearLayout linearLayout= (LinearLayout) convertView.findViewById(R.id.linearLayoutExpandableListTitle);
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textViewRecordGroupTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        if(isExpanded)
        {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
        }
        else
        {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandableitem_chart, null);
        }
        ArrayList<HashMap<String,String>> mapArrayList =arrayListMapItems.get(arrayListGroupItems.get(groupPosition)) ;
       PieChart mChart = (PieChart)convertView.findViewById(R.id.chartViewExpandableList);
        ListView listView= (ListView) convertView.findViewById(R.id.listViewChartExpandableList);
        listView.setAdapter(new ChartCategoryListAdpter(context,currencySymbol ,mapArrayList));
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
       Typeface tf=Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
       mChart.setUsePercentValues(true);
       mChart.setHoleColorTransparent(true);
        mChart.setCenterTextTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf"));
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
       double totalIncome=0,totalExpense=0,totalAmount=0.0;
       ReadSQLiteData readSQLiteData;
       ListView listViewChartItems;

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
        //mChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        mChart.invalidate();





        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    void log(String msg)
    {
        Log.d("ChartExpandListAdpater",msg);
    }
}
