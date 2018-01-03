package newwalletapp.adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import newwalletapp.interfaces.AddCategory;
import newwalletapp.interfaces.CategoriesInterface;
import newwalletapp.interfaces.DeleteCategory;

/**
 * Created by ahmedchoteri on 18-02-15.
 */
public class CategoriesAdpter extends BaseAdapter {
    String Tag="CategoriesAdpter";
    ArrayList<HashMap<String,String>> arrayList;
    Context context;

    public CategoriesAdpter(Context con,ArrayList<HashMap<String,String>> arrayMap)
    {
        log("CategoriesAdpter Constructor");
        this.context=con;
        this.arrayList=arrayMap;
    }


    @Override
    public int getCount() {

        return arrayList.size();
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

        //View v = convertView;
        Log.d("Adpter", "Check CategoriesAdpter");
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (convertView == null) {

            v = new View(context);


            v = inflater.inflate(R.layout.listitem_categories, null);



        } else {

            v= (View) convertView;

        }
        LinearLayout linearLayoutImageBackground= (LinearLayout) v.findViewById(R.id.linearLayoutCategoriesIconBackground);
        log("Position = "+position+ "Color = "+arrayList.get(position).get("color"));
        linearLayoutImageBackground.setBackgroundColor(Color.parseColor(arrayList.get(position).get("color").trim()));
        ImageView imageViewIcon= (ImageView) v.findViewById(R.id.imageViewCategoriesIcon);
        Drawable drawable = context.getResources().getDrawable(context.getResources()
                .getIdentifier("categoury_w_"+arrayList.get(position).get("icon"), "drawable", context.getPackageName()));
        log("Position = "+position+ "icon ="+arrayList.get(position).get("icon"));
        imageViewIcon.setImageDrawable(drawable);
        TextView textViewCategoriesName= (TextView) v.findViewById(R.id.textViewCategoryName);
        textViewCategoriesName.setText(arrayList.get(position).get("name"));
        log("Position = "+position+ "Name ="+arrayList.get(position).get("name"));
        TextView textViewCategoriesType= (TextView) v.findViewById(R.id.textViewCategoryType);
        textViewCategoriesType.setText(arrayList.get(position).get("type"));
        log("Position = "+position+ "type = "+arrayList.get(position).get("type"));

        ImageView imageViewEdit= (ImageView) v.findViewById(R.id.imageViewCategoryEdit);
        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putSerializable("map",arrayList.get(position));
                AddCategory addCategory= (AddCategory) context;
                addCategory.beginAddCategoryTransaction(b);
            }
        });

        ImageView imageViewDelete= (ImageView) v.findViewById(R.id.imageViewCategoryDelete);
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DeleteCategory deleteCategory= (DeleteCategory) context;
                                deleteCategory.DeleteCategory(arrayList.get(position).get("id"),arrayList.get(position).get("name"));

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.alert_category_delete)).setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        return v;
    }

     void log(String msg)
    {
        Log.d(Tag,msg);
    }
}
