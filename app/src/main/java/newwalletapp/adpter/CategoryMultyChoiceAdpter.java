package newwalletapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;

/**
 * Created by ahmedchoteri on 23-03-15.
 */
public class CategoryMultyChoiceAdpter extends BaseAdapter {

    String[] arrayCategori;
    Context context;
    int selected;

    public CategoryMultyChoiceAdpter(Context con,String[] array)
    {
        this.context=con;
        this.arrayCategori=array;

    }
    @Override
    public int getCount() {
        return arrayCategori.length;
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
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_category_multichoice, null);
        }

        TextView textViewName= (TextView) convertView.findViewById(R.id.textViewMultichoiceCategoryName);
        textViewName.setText(arrayCategori[position]);
        final CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.checkboxMultichoiceCategory);
        if(selected==position)
        {
            if(!checkBox.isChecked())
            {
                checkBox.setChecked(true);
            }
            else
            {
                checkBox.setChecked(false);
            }

        }



        return convertView;
    }

    public void setSelectedItem(int position) {
        // TODO Auto-generated method stub
        this.selected=position;
        notifyDataSetChanged();

    }

}
