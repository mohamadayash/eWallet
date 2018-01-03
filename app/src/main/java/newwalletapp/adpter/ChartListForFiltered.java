package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.ReadSQLiteData;
import newwalletapp.fragmentsmain.FragmentChartFilterd;
import newwalletapp.interfaces.FilterInterface;

/**
 * Created by ahmedchoteri on 16-03-15.
 */
public class ChartListForFiltered extends BaseAdapter {
    Context context;
    ArrayList<String> arrayListGroupItems;
    HashMap<String, ArrayList<HashMap<String, String>>> arrayListMapItems;
    String currencySymbol;
    public ChartListForFiltered(Context con, ArrayList<String> groupArray, HashMap<String,ArrayList<HashMap<String,String>>> arrayListMap) {

        this.context = con;
        this.arrayListMapItems = arrayListMap;


        ReadSQLiteData readSQLiteData=new ReadSQLiteData(context);
        currencySymbol=readSQLiteData.getCurrencySymbol();

        this.arrayListGroupItems = groupArray;

        if(arrayListGroupItems==null)
        {
            arrayListGroupItems=new ArrayList<String>();
            arrayListGroupItems.add("Data Sorted by Day");
        }
    }

    @Override
    public int getCount() {
        return arrayListGroupItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String headerTitle = arrayListGroupItems.get(position);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.groupitem_record, null);
        }
        LinearLayout linearLayout= (LinearLayout) convertView.findViewById(R.id.linearLayoutExpandableListTitle);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Hiiii",Toast.LENGTH_LONG).show();
                Bundle b=new Bundle();

                b.putString("currency",currencySymbol);
                b.putSerializable("map",arrayListMapItems.get(arrayListGroupItems.get(position)));

                FragmentChartFilterd fragment=new FragmentChartFilterd();
                fragment.setArguments(b);
                FilterInterface chartFilterInterface= (FilterInterface) context;
                chartFilterInterface.makeFilterTransaction(fragment);
/*
                android.app.FragmentManager fragmentManager =((Activity)context).getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
*/
                 //.newInstance(currencySymbol, arrayListMapItems.get(arrayListGroupItems.get(position)));
            }
        });
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textViewRecordGroupTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        return convertView;

    }



}
