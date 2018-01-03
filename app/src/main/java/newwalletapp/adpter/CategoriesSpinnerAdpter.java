package newwalletapp.adpter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

/**
 * Created by ahmedchoteri on 20-02-15.
 */
public class CategoriesSpinnerAdpter extends BaseAdapter {

    ArrayList<HashMap<String, String>> arrayListCategories;
    Context context;

    public CategoriesSpinnerAdpter(Context con, ArrayList<HashMap<String, String>> arrayMap) {
        this.arrayListCategories = arrayMap;
        this.context = con;
    }

    @Override
    public int getCount() {
        return arrayListCategories.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            v = new View(context);


            v = inflater.inflate(R.layout.spinneritem_category, null);


        } else {

            v = (View) convertView;

        }

        if(arrayListCategories.get(position)==null)
        {

            LinearLayout linearLayout= (LinearLayout) v.findViewById(R.id.linearLayoutSpinnerItemCategoryIconBack);
            linearLayout.setVisibility(View.INVISIBLE);
            TextView textView= (TextView) v.findViewById(R.id.textViewSpinnerItemCategoryName);
            textView.setText(context.getResources().getString(R.string.no_parent_category));
            ImageView imageView= (ImageView) v.findViewById(R.id.imageViewSpinnerItemCategoryIcon);
            imageView.setVisibility(View.INVISIBLE);

        }
        else
        {
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearLayoutSpinnerItemCategoryIconBack);
            linearLayout.setBackgroundColor(Color.parseColor(arrayListCategories.get(position).get("color")));
            ImageView imageView= (ImageView) v.findViewById(R.id.imageViewSpinnerItemCategoryIcon);
            Drawable drawable = context.getResources().getDrawable(context.getResources()
                    .getIdentifier("categoury_w_"+arrayListCategories.get(position).get("icon"), "drawable", context
                            .getPackageName()));
            imageView.setImageDrawable(drawable);
            TextView textView= (TextView) v.findViewById(R.id.textViewSpinnerItemCategoryName);
            textView.setText(arrayListCategories.get(position).get("name"));

        }

        return v;
    }

}
