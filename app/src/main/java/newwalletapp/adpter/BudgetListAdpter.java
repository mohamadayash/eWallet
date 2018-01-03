package newwalletapp.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.ReadSQLiteData;

/**
 * Created by ahmedchoteri on 25-03-15.
 */
public class BudgetListAdpter extends BaseAdapter {

    Context context;
    ReadSQLiteData readSQLiteData;
    ArrayList<HashMap<String,String>> arrayList;
    String currencySymbol;
    public BudgetListAdpter(Context context,ArrayList<HashMap<String,String>> list)
    {
       this.context=context;
       this.arrayList=list;
        readSQLiteData=new ReadSQLiteData(context);
        currencySymbol=readSQLiteData.getCurrencySymbol();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listitem_budget, null);
        }

        TextView textViewName= (TextView) convertView.findViewById(R.id.textViewBudgetItemName);
        textViewName.setText(arrayList.get(position).get("bud_name"));
        TextView textViewAccount= (TextView) convertView.findViewById(R.id.textViewBudgetAccountName);
        textViewAccount.setText(arrayList.get(position).get("bud_account"));
        TextView textViewAmount= (TextView) convertView.findViewById(R.id.textViewBudgetItemAmount);
        textViewAmount.setText(context.getResources().getString(R.string.amount)+": "+arrayList.get(position).get("bud_amount")+currencySymbol);
        TextView textViewExpense= (TextView) convertView.findViewById(R.id.textViewBudgetItemExpense);
        textViewExpense.setTextColor(context.getResources().getColor(R.color.red));
        textViewExpense.setText(context.getResources().getString(R.string.expense)+": "+arrayList.get(position).get("expense_amount")+currencySymbol);
        TextView textViewInterval= (TextView) convertView.findViewById(R.id.textViewBudgetItemInterval);
        textViewInterval.setText(arrayList.get(position).get("interval"));
        TextView textViewRemain= (TextView) convertView.findViewById(R.id.textViewBudgetItemRemain);
        textViewRemain.setText(context.getResources().getString(R.string.remain)+" "+arrayList.get(position).get("remain_amount")+currencySymbol);
        if((Double.parseDouble(arrayList.get(position).get("remain_amount")))>=0)
        {
            textViewRemain.setTextColor(context.getResources().getColor(R.color.green));
        }
        else
        {
            textViewRemain.setTextColor(context.getResources().getColor(R.color.red));
        }
        return convertView;
    }
}
