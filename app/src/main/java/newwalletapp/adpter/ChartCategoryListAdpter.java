package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ahmedchoteri on 09-03-15.
 */
public class ChartCategoryListAdpter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arrayListMap;
    String symbol;
    public ChartCategoryListAdpter(Context con,String currencySymbol,ArrayList<HashMap<String,String>> map)
    {

        this.context=con;
        this.arrayListMap=map;
        this.symbol=currencySymbol;
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
        View v;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            v = new View(context);
            v = inflater.inflate(R.layout.listitem_chartitem_category, null);
        } else {

            v = (View) convertView;

        }

        LinearLayout linearLayout= (LinearLayout) v.findViewById(R.id.linearLayoutChartCategoryItemColor);
        TextView textViewName= (TextView) v.findViewById(R.id.textViewChartCategoryItemName);
        TextView textViewAmount= (TextView) v.findViewById(R.id.textViewChartCategoryItemAmount);

        linearLayout.setBackgroundColor(Color.parseColor(arrayListMap.get(position).get("color")));
        textViewName.setText(""+arrayListMap.get(position).get("category"));
        textViewAmount.setText(""+arrayListMap.get(position).get("amount")+" "+symbol);



        return v;
    }
}
