package newwalletapp.adpter;

import java.util.ArrayList;
import java.util.HashMap;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

public class CurrencyAdpter extends BaseAdapter{

	String Tag="CurrencyListAdpter";
	Context context;
	ArrayList<HashMap<String,String>> arrayCurrency;
	public static int selected;
	
	public CurrencyAdpter(Context con, ArrayList<HashMap<String, String>> list)
	{
		Log.d(Tag, "Constructor");
		this.context = con;
		this.arrayCurrency = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayCurrency.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		   View v = convertView;
		    if (v == null) {
		        LayoutInflater mInflater = (LayoutInflater) context
		                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.listitem_country,null);
		        v = mInflater.inflate(R.layout.listitem_country, null);

		    }
/*
		    TextView textViewCurrencyName = (TextView) v.findViewById(R.id.textViewCurrencyName);

		    textViewCurrencyName.setText(""+arrayCurrency.get(position).get("code")+" "+arrayCurrency.get(position).get("country"));
		    RadioButton radioButton=(RadioButton) v.findViewById(R.id.radioCurrency);
		    if(selected == position)
		    {
		    	radioButton.setChecked(true);
		    }
		    else
		    {
		    	radioButton.setChecked(false);
		    }*/
		return v;
	}

	public void setSelectedItem(int position) {
		// TODO Auto-generated method stub
		this.selected=position;
		notifyDataSetChanged();
		
	}
	
	public HashMap<String,String> getSelectedItem()
	{
		return arrayCurrency.get(selected);
	}
	


}
