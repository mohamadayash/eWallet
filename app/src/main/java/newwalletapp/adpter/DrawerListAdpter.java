package newwalletapp.adpter;

import android.content.Context;
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

import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 15-02-15.
 */
public class DrawerListAdpter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> listItem;
    int selectedPosition;
    public DrawerListAdpter(Context con,ArrayList<HashMap<String,String>> arrayList)
    {
        this.context=con;
        this.listItem=arrayList;
        selectedPosition=-1;
    }
    public int getSelectedPosition()
    {
        return selectedPosition;
    }
    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelectedPosition(int pos)
    {
        selectedPosition=pos;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        selectedPosition=position;
        Log.d("Adpter","Check Adpter");
        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=mInflater.inflate(R.layout.listitem_drawer,null);
        }

        LinearLayout linearLayout= (LinearLayout) v.findViewById(R.id.linearLayoutDrawerListItem);
        if (selectedPosition!=-1 && selectedPosition==position)
        {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.transeWhite));
        }
        else
        {
            linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        ImageView imageViewListItemIcon= (ImageView) v.findViewById(R.id.imageViewDrawerListIcon);
        TextView textViewListItemText= (TextView) v.findViewById(R.id.textViewDrawerListItem);
        imageViewListItemIcon.setBackgroundResource(Integer.parseInt(listItem.get(position).get("icon")));
        textViewListItemText.setText(listItem.get(position).get("text"));

        return v;
    }
}
