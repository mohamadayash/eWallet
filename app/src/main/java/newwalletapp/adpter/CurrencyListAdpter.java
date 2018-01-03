package newwalletapp.adpter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.interfaces.AddCurrencyInterface;
import newwalletapp.interfaces.DeleteCategory;
import newwalletapp.interfaces.DeleteCurrencyInterface;

/**
 * Created by ahmedchoteri on 23-02-15.
 */
public class CurrencyListAdpter extends BaseAdapter {


    Context context;
    ArrayList<HashMap<String,String>> arrayListCurrency;

    public CurrencyListAdpter(Context con,ArrayList<HashMap<String,String>> arrayList)
    {
        this.context=con;
        this.arrayListCurrency=arrayList;
    }

    @Override
    public int getCount() {
        return arrayListCurrency.size();
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


        View v = convertView;

        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.listitem_currency, null);
        }

        TextView textViewCurrencyName= (TextView) v.findViewById(R.id.textViewCurrencyListName);
        textViewCurrencyName.setText(arrayListCurrency.get(position).get("name"));
        TextView textViewCurrencySymbol= (TextView) v.findViewById(R.id.textViewCurrencyListSymbol);
        textViewCurrencySymbol.setText("Currency Code : "+arrayListCurrency.get(position).get("code"));
        TextView textViewCurrencyRate= (TextView) v.findViewById(R.id.textViewCurrencyListRate);
        ImageView imageViewEdit= (ImageView) v.findViewById(R.id.imageViewCurrencyListEdit);
        ImageView imageViewDelete= (ImageView) v.findViewById(R.id.imageViewCurrencyListDelete);
        if(!arrayListCurrency.get(position).get("type").equals("master"))
        {
            textViewCurrencyRate.setText("Rate : "+arrayListCurrency.get(position).get("rate"));
            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCurrencyInterface addCurrencyInterface= (AddCurrencyInterface) context;
                    Bundle b=new Bundle();
                    b.putString("id",arrayListCurrency.get(position).get("id"));
                    log("Bundle put id"+arrayListCurrency.get(position).get("id"));
                    b.putString("name",arrayListCurrency.get(position).get("name"));
                    log("Bundle put name="+arrayListCurrency.get(position).get("name"));
                    b.putString("code",arrayListCurrency.get(position).get("code"));
                    log("Bundel put code="+arrayListCurrency.get(position).get("code"));
                    b.putString("symbol",arrayListCurrency.get(position).get("symbol"));
                    log("Bundel put symbol="+arrayListCurrency.get(position).get("symbol"));
                    b.putString("rate",arrayListCurrency.get(position).get("rate"));
                    log("Bundel put rate="+arrayListCurrency.get(position).get("rate"));


                    addCurrencyInterface.beginAddCurrencyTransaction(b);


                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    DeleteCurrencyInterface deleteCurrencyInterface= (DeleteCurrencyInterface) context;
                                    log("Deleted Currency id="+arrayListCurrency.get(position).get("id"));
                                    deleteCurrencyInterface.deleteCurrency(arrayListCurrency.get(position).get("id"),arrayListCurrency.get(position).get("code"),arrayListCurrency.get(position).get("name"));

                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.dialog_currency_delete_message)).setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();


                }
            });

        }
        else
        {
            textViewCurrencyRate.setText("Reference Currency");
            imageViewEdit.setVisibility(View.INVISIBLE);
            imageViewDelete.setVisibility(View.INVISIBLE);
        }




        return v;
    }

    void log(String msg)
    {
        Log.d("CurrencyListAdapter",msg);
    }

}
