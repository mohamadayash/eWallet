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

import com.example.accounts.newwalletapp.R;

/**
 * Created by ahmedchoteri on 21-02-15.
 */
public class CategoriesIconAdpter extends BaseAdapter {


    Context context;
    Drawable[] arrayDrawables;
    public CategoriesIconAdpter(Context con,Drawable[] drawables)
    {
        this.context=con;
        this.arrayDrawables=drawables;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayDrawables.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub griditem_categoryicon
        View v = convertView;

        Log.d("Adpter","Check Adpter");
        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=mInflater.inflate(R.layout.griditem_categoryicon,null);


        }
        ImageView imageViewIcon=(ImageView)v.findViewById(R.id.imageViewGridCategoryIcon);
        //imageViewIcon.setBackground();
        imageViewIcon.setImageDrawable(arrayDrawables[position]);
        //imageViewIcon.setBackgroundDrawable();

        return v;
    }
}
