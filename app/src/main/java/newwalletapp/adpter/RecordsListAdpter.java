package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.ReadSQLiteData;

/**
 * Created by ahmedchoteri on 02-03-15.
 */
public class RecordsListAdpter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arrayListMap;
    ReadSQLiteData readSQLiteData;
    String currencySymbol;

    public RecordsListAdpter(Context con,ArrayList<HashMap<String,String>> arrayList)
    {
        this.context=con;
        readSQLiteData = new ReadSQLiteData(con);
        currencySymbol = readSQLiteData.getCurrencySymbol();
        this.arrayListMap = arrayList;
    }


    @Override
    public int getCount() {
        return arrayListMap.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        final String icon = arrayListMap.get(position).get("icon");
        final String recordColor = arrayListMap.get(position).get("color");
        final String recordName = arrayListMap.get(position).get("category");
        final String recordAmount = arrayListMap.get(position).get("amount");
         String symbol = arrayListMap.get(position).get("symbol");
        final String recordDate =arrayListMap.get(position).get("date");
        final String type =arrayListMap.get(position).get("type");
        final String currencytype=arrayListMap.get(position).get("currencytype");
        Log.d("RecordListAdpter","currencytype ="+currencytype);
        if(currencySymbol.equals(symbol))
        {
            symbol="";
        }
        final String childamount=arrayListMap.get(position).get("childamount");
        Log.d("RecordListAdpter","childamount ="+childamount);

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
        TextView textViewRecordChildAmount=(TextView) convertView.findViewById(R.id.textViewRecordsChildAmount);
//        if(currencytype.equals("child"))
        {
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


}
