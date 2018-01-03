package newwalletapp.fragmentsmain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.accounts.newwalletapp.AddNewRecordActivity;
import com.example.accounts.newwalletapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.adpter.RecordsListAdpter;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.EditRecordTransaction;
import newwalletapp.interfaces.UpdateDashboard;

/**
 * Created by ahmedchoteri on 11-03-15.
 */
public class FragmentDeshPage extends  android.support.v4.app.Fragment{


    private String acc_name;
    TextView textViewTotalAmmount;
    ListView listViewRecord;

/*
    public static FragmentDeshPage  newInstance(String account_name,String total,ArrayList<HashMap<String,String>> map)
    {
        Log.d("FragmentDeshPage","newInstance");
        FragmentDeshPage f = new FragmentDeshPage();
        Bundle bundle = new Bundle();
        bundle.putString("total",total);
        Log.d("FragmentDeshPage","newInstance Total ="+total);
        bundle.putString("name",account_name);
        bundle.putSerializable("map",map);
        f.setArguments(bundle);
        return f;
    }
*/
    public static FragmentDeshPage  newInstance(String account_name)
    {
        Log.d("FragmentDeshPage","newInstance");
        FragmentDeshPage f = new FragmentDeshPage();
        Bundle bundle = new Bundle();
//        bundle.putString("total",total);
//        Log.d("FragmentDeshPage","newInstance Total ="+total);
        bundle.putString("name",account_name);
//        bundle.putSerializable("map",map);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log("onCreate");
        //acc_id = getArguments().getString("id");
        acc_name = getArguments().getString("name");
        log("acc_name ="+getArguments().getString("name"));
//        total_amount=getArguments().getString("total");
//        log("total_amount="+getArguments().getString("total"));
//        arrayList=(ArrayList<HashMap<String,String>>)getArguments().getSerializable("map");
//        log("arrayList "+(ArrayList<HashMap<String,String>>)getArguments().getSerializable("map"));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        log("onCreateView");
        View v = inflater.inflate(R.layout.fragment_page_deshboard, container, false);
        textViewTotalAmmount = (TextView)v.findViewById(R.id.textViewDeshBoardTotalBalance);
        listViewRecord = (ListView)v.findViewById(R.id.listViewDeshBoardRecords);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log("onActivityCreated");
        setUpData();


    }


    void setUpData()
    {

        new AsyncTask<Void, Void, Void>() {
            RecordsListAdpter recordsListAdpter;
            String currencySymbol;
            ReadSQLiteData readSQLiteData;
            String total_amount;
            ArrayList<HashMap<String,String>> arrayList;
            DecimalFormat decimalFormat;
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                readSQLiteData = new ReadSQLiteData(getActivity());
                currencySymbol = readSQLiteData.getCurrencySymbol();
                decimalFormat = new DecimalFormat("##.##");

            }

            @Override
            protected Void doInBackground(Void... params) {
                total_amount = ""+decimalFormat.format(readSQLiteData.getTotalOfAllIncome(acc_name) - readSQLiteData.getTotalOfAllExpense(acc_name))+" "+currencySymbol;
                arrayList = readSQLiteData.getTopTenRecords(acc_name);
                recordsListAdpter = new RecordsListAdpter(getActivity(),arrayList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                textViewTotalAmmount.setText(total_amount);
                log("Array list of record onCreate"+(ArrayList<HashMap<String,String>>)getArguments().getSerializable("map"));
                listViewRecord.setAdapter(recordsListAdpter);
                listViewRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        log("ItemClicked " + arrayList.get(position));

                        EditRecordTransaction editRecordTransaction = (EditRecordTransaction) getActivity();
                        Bundle b = new Bundle();
                        b.putSerializable("map", arrayList.get(position));
                        editRecordTransaction.makeEditRecordTransaction(b);

/*
                Intent i=new Intent(getActivity(), AddNewRecordActivity.class);
                i.putExtra("map",arrayList.get(position));
                startActivity(i);
*/
                    }
                });

            }
        }.execute(null,null,null);

    }
    public void log(String msg)
    {
        Log.d("FragmentDeshPage",msg);
    }


    private class setUpDataTask extends AsyncTask<Void,Void,Void>{

        RecordsListAdpter recordsListAdpter;
        String currencySymbol;
        ReadSQLiteData readSQLiteData;
        String acc_name,total_amount;
        ArrayList<HashMap<String,String>> arrayList;
        DecimalFormat decimalFormat;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            readSQLiteData=new ReadSQLiteData(getActivity());
            currencySymbol=readSQLiteData.getCurrencySymbol();

            decimalFormat = new DecimalFormat("##.##");

        }

        @Override
        protected Void doInBackground(Void... params) {
            total_amount=""+decimalFormat.format(readSQLiteData.getTotalOfAllIncome(acc_name) - readSQLiteData.getTotalOfAllExpense(acc_name))+" "+currencySymbol;
            arrayList=readSQLiteData.getTopTenRecords(acc_name);
            recordsListAdpter=new RecordsListAdpter(getActivity(),arrayList);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textViewTotalAmmount.setText(total_amount);
            log("Array list of record onCreate"+(ArrayList<HashMap<String,String>>)getArguments().getSerializable("map"));
            listViewRecord.setAdapter(recordsListAdpter);
            listViewRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    log("ItemClicked " + arrayList.get(position));

                    EditRecordTransaction editRecordTransaction = (EditRecordTransaction) getActivity();
                    Bundle b = new Bundle();
                    b.putSerializable("map", arrayList.get(position));
                    editRecordTransaction.makeEditRecordTransaction(b);

/*
                Intent i=new Intent(getActivity(), AddNewRecordActivity.class);
                i.putExtra("map",arrayList.get(position));
                startActivity(i);
*/
                }
            });

        }
    }

}
