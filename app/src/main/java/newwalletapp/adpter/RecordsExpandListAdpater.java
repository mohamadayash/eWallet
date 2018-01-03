package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.ReadSQLiteData;

/**
 * Created by ahmedchoteri on 01-03-15.
 */
public class RecordsExpandListAdpater extends BaseExpandableListAdapter {


    Context context;
    ArrayList<String> arrayListGroupItems;
    HashMap<String, ArrayList<HashMap<String, String>>> arrayListMapItems;
    ReadSQLiteData readSQLiteData;
    String currencySymbol;
    public RecordsExpandListAdpater(Context con, ArrayList<String> groupArray, HashMap<String,ArrayList<HashMap<String,String>>> arrayListMap) {

        this.context = con;
        readSQLiteData=new ReadSQLiteData(con);
        currencySymbol=readSQLiteData.getCurrencySymbol();
        this.arrayListMapItems = arrayListMap;
        this.arrayListGroupItems = groupArray;
        if(arrayListGroupItems==null)
        {
            arrayListGroupItems=new ArrayList<String>();
            arrayListGroupItems.add("Data Sorted by Day");
        }
    }


    @Override
    public int getGroupCount() {

        return arrayListGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.arrayListMapItems.get(this.arrayListGroupItems.get(groupPosition)).size();
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
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
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
        ImageView imageView= (ImageView) convertView.findViewById(R.id.imageViewRecordGroupOpen);
        imageView.setVisibility(View.INVISIBLE);


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
        final String icon = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("icon");
        final String recordColor = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("color");
        final String recordName = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("category");
        final String recordAmount = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("amount");
        String symbol = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("symbol");
        final String recordDate = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("date");
        final String type = arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("type");
        final String currencytype=arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("currencytype");
        Log.d("RecordListAdpter", "currencytype =" + currencytype);
        final String childamount=arrayListMapItems.get(arrayListGroupItems.get(groupPosition)).get(childPosition).get("childamount");
        Log.d("RecordListAdpter","childamount ="+childamount);
        if(currencySymbol.equals(symbol))
        {
            symbol="";
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_record, null);
        }

        LinearLayout linearLayoutIcon= (LinearLayout) convertView.findViewById(R.id.linearLayoutRecordsIconBackground);
        linearLayoutIcon.setBackgroundColor(Color.parseColor(recordColor));

        ImageView imageViewIcon= (ImageView) convertView.findViewById(R.id.imageViewRecordIcon);
        Drawable drawable = context.getResources().getDrawable(context.getResources()
                .getIdentifier("categoury_w_"+icon, "drawable", context.getPackageName()));

        imageViewIcon.setImageDrawable(drawable);

        TextView textViewRecordName = (TextView) convertView.findViewById(R.id.textViewRecordsName);
        textViewRecordName.setText(recordName);

        TextView textViewRecordAmount = (TextView) convertView.findViewById(R.id.textViewRecordsAmount);
        textViewRecordAmount.setText(context.getResources().getString(R.string.amount)+": "+recordAmount+" "+currencySymbol);

        if(type.equals("income"))
        {
            textViewRecordAmount.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            textViewRecordAmount.setTextColor(context.getResources().getColor(R.color.red));
        }

        TextView textViewRecordDate = (TextView) convertView.findViewById(R.id.textViewRecordsDate);
        textViewRecordDate.setText(recordDate);

//        if(currencytype.equals("child"))
        {
            TextView textViewRecordChildAmount=(TextView) convertView.findViewById(R.id.textViewRecordsChildAmount);
            textViewRecordChildAmount.setVisibility(View.VISIBLE);
            textViewRecordChildAmount.setText(childamount+" "+symbol);
            if(type.equals("income"))
            {
                textViewRecordChildAmount.setTextColor(context.getResources().getColor(R.color.green));
            }
            else
            {
                textViewRecordChildAmount.setTextColor(context.getResources().getColor(R.color.red));
            }

        }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
