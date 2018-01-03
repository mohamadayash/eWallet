package newwalletapp.fragmentsmain;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.text.DecimalFormat;
import java.util.HashMap;

import newwalletapp.database.ReadSQLiteData;
import newwalletapp.backup.ExportReportData;

import static android.view.View.INVISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentReportFiltered extends android.support.v4.app.Fragment {

    TextView textViewIncomeCount, textViewExpenseCount, textViewIncomeAverageDay, textViewExpenseAverageDay,
            textViewIncomeAverageRecord, textViewExpenseAverageRecord, textViewIncomeTotal, textViewExpenseTotal,
            textViewStartingBalance, textViewNetEndingBalance, textViewCashflow;
    ReadSQLiteData readSQLiteData;
    int incomeRecordCount;
    int dayCount;
    int expenseRecordCount;
    String title;
    double totalIncomeForAllRecords;
    double totalExpenseForAllRecords;
    public FragmentReportFiltered() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        HashMap<String,String> map= (HashMap<String,String>) getArguments().getSerializable("map");
        title=getArguments().getString("title");
        log("Title ="+title);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(title);
        dayCount=Integer.parseInt(map.get("daycount"));

        log("Day Count =" + dayCount);
        incomeRecordCount=Integer.parseInt(map.get("incomerecordcount"));
        expenseRecordCount=Integer.parseInt(map.get("expenserecordcount"));
        totalIncomeForAllRecords=Double.parseDouble(map.get("totalincome"));
        log("totalIncomeForAllRecords " + totalIncomeForAllRecords);
        totalExpenseForAllRecords=Double.parseDouble(map.get("totalexpense"));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readSQLiteData=new ReadSQLiteData(getActivity());
        String currencySybol = readSQLiteData.getCurrencySymbol();
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        LinearLayout linearLayout= (LinearLayout) getActivity().findViewById(R.id.linearLayoutReport);
        linearLayout.setVisibility(View.VISIBLE);
        ListView listView= (ListView) getActivity().findViewById(R.id.listViewFilteredReport);
        listView.setVisibility(INVISIBLE);


        textViewIncomeCount = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeCount);
        textViewExpenseCount = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseCount);
        textViewIncomeAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageDay);
        textViewExpenseAverageDay = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageDay);
        textViewIncomeAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeAverageRecord);
        textViewExpenseAverageRecord = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseAverageRecord);
        textViewIncomeTotal = (TextView) getActivity().findViewById(R.id.textViewRecordIncomeTotal);
        textViewExpenseTotal = (TextView) getActivity().findViewById(R.id.textViewRecordExpenseTotal);
        textViewStartingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordStartingBalance);
        textViewNetEndingBalance = (TextView) getActivity().findViewById(R.id.textViewRecordNetEndingBalance);
        textViewCashflow = (TextView) getActivity().findViewById(R.id.textViewRecordCashFlow);

        textViewIncomeCount.setText(""+incomeRecordCount);
        textViewExpenseCount.setText(""+expenseRecordCount);
        if(totalIncomeForAllRecords>0)
        {
            textViewIncomeAverageDay.setText("" + decimalFormat.format(totalIncomeForAllRecords / dayCount) + " " + currencySybol);
            textViewIncomeAverageRecord.setText("" + decimalFormat.format(totalIncomeForAllRecords / incomeRecordCount) + " " + currencySybol);
        }
        else
        {
            textViewIncomeAverageDay.setText("0"  + currencySybol);
            textViewIncomeAverageRecord.setText("0" +  currencySybol);
        }

        if(totalExpenseForAllRecords>0)
        {
            textViewExpenseAverageDay.setText("" + decimalFormat.format(totalExpenseForAllRecords / dayCount) + " " + currencySybol);
            textViewExpenseAverageRecord.setText("" + decimalFormat.format(totalExpenseForAllRecords / expenseRecordCount) + " " + currencySybol);
        }
        else
        {
            textViewExpenseAverageDay.setText("0" + currencySybol);
            textViewExpenseAverageRecord.setText("0"  + currencySybol);
        }
        textViewIncomeTotal.setText( "" + decimalFormat.format(totalIncomeForAllRecords) + " " + currencySybol);
        textViewExpenseTotal.setText("" + decimalFormat.format(totalExpenseForAllRecords) + " " + currencySybol);
        textViewStartingBalance.setText("0" + " " + currencySybol);
        textViewNetEndingBalance.setText( "" + decimalFormat.format(totalIncomeForAllRecords - totalExpenseForAllRecords) + " " + currencySybol);
        textViewCashflow.setText("" + decimalFormat.format(totalIncomeForAllRecords - totalExpenseForAllRecords) + " " + currencySybol);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_records, menu);
        MenuItem menuItemFilter=menu.findItem(R.id.action_filter);
        menuItemFilter.setVisible(false);
        MenuItem menuItemExport=menu.findItem(R.id.action_export);
        menuItemExport.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.action_export)
        {
            HashMap<String,String> exportMap=new HashMap<>();
            exportMap.put("income_count",textViewIncomeCount.getText().toString());
            exportMap.put("expense_count",textViewExpenseCount.getText().toString());
            exportMap.put("income_average_day",textViewIncomeAverageDay.getText().toString());
            exportMap.put("expense_average_day",textViewExpenseAverageDay.getText().toString());
            exportMap.put("income_average_record",textViewIncomeAverageRecord.getText().toString());
            exportMap.put("expense_average_record",textViewExpenseAverageRecord.getText().toString());
            exportMap.put("income_total",textViewIncomeTotal.getText().toString());
            exportMap.put("expense_total",textViewExpenseTotal.getText().toString());
            exportMap.put("starting_balance",textViewStartingBalance.getText().toString());
            exportMap.put("netending_balance",textViewNetEndingBalance.getText().toString());
            exportMap.put("cashflow",textViewCashflow.getText().toString());

            ExportReportData exportReportData=new ExportReportData(getActivity());
            String str=exportReportData.exportReport("_"+title,exportMap);
            Toast.makeText(getActivity(), getActivity().getString(R.string.toast_export)+" " + str, Toast.LENGTH_LONG).show();

        }
        return super.onOptionsItemSelected(item);
    }

    void log(String msg)
   {
       Log.d("FragmentReportFiltered",msg);
   }
}
