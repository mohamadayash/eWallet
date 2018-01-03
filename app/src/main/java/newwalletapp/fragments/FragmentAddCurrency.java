package newwalletapp.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.currency.CurrencyData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;
import newwalletapp.database.UpdateSQLiteData;
import newwalletapp.interfaces.BeginPopStack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddCurrency extends android.support.v4.app.Fragment {


    EditText editTextCurrencyRate;
    TextView textViewCurrencyCode,textViewCyrrencySymbol;
    Spinner spinnerCurrency;
    String arrayCurrency[];
    //ArrayList<HashMap<String,String>> arrayListCurrency;
    ArrayList<HashMap<String,String>> filteredArrayListCurrency;
    HashMap<String,String> selectedCurrencyMap;
    String recId;
    int flagForEdit=0;
    ReadSQLiteData readSQLiteData;
     public FragmentAddCurrency() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        log("onCreate");
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Add Currency");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        log("onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_currency, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readSQLiteData=new ReadSQLiteData(getActivity());
        log("onActivityCreated");
        textViewCurrencyCode= (TextView) getActivity().findViewById(R.id.textViewAddCurrencyCode);

        textViewCyrrencySymbol= (TextView) getActivity().findViewById(R.id.textViewAddCurrencySymbol);

        editTextCurrencyRate= (EditText) getActivity().findViewById(R.id.editTextAddCurrencyRate);


        spinnerCurrency= (Spinner) getActivity().findViewById(R.id.spinnerAddCurrency);

        ArrayList<HashMap<String,String>> storedCurrencies=readSQLiteData.getAllStoredCurrencies();

        CurrencyData currencyData=new CurrencyData(getActivity());
        filteredArrayListCurrency=new ArrayList<>();
        ArrayList<HashMap<String,String>> arrayListCurrency=currencyData.getCurrencyList();
//        arrayCurrency=new String[arrayListCurrency.size()-storedCurrencies.size()];
        ArrayList<String> filteredCurrency=new ArrayList<String>();
        Bundle bundle=getArguments();
        String bundleCurrencyName = null;
        int flagForBundle=0;
        if (bundle!=null)
        {
            flagForBundle=1;
            bundleCurrencyName=bundle.getString("name");
        }

        for (int i=0;i<arrayListCurrency.size();i++)
        {
            int flag=0;
            for (int j=0;j<storedCurrencies.size();j++)
            {
                if(storedCurrencies.get(j).get("name").equals(arrayListCurrency.get(i).get("country")))
                {
                    if (flagForBundle==1)
                    {
                        if (!bundleCurrencyName.equals(arrayListCurrency.get(i).get("country")))
                        {
                            flag=1;
                            break;
                        }
                    }
                    else
                    {
                        flag=1;
                        break;
                    }
                }
            }

            if(flag==0)
            {
             filteredCurrency.add(arrayListCurrency.get(i).get("code")+"  "+arrayListCurrency.get(i).get("country"));
                log("filteredCurrency ="+filteredCurrency);
             filteredArrayListCurrency.add(arrayListCurrency.get(i));
            }
        }
        arrayCurrency=new String[filteredCurrency.size()];
        for(int i=0;i<filteredCurrency.size();i++)
        {
            arrayCurrency[i]=filteredCurrency.get(i);
        }

/*
        for(int i=0;i<arrayListCurrency.size();i++)
        {
            arrayCurrency[i]=arrayListCurrency.get(i).get("code")+"  "+arrayListCurrency.get(i).get("country");
        }
*/

        ArrayAdapter<String> spinnerAdpter=new ArrayAdapter<String>(getActivity(),R.layout.spinneritem_simple,arrayCurrency);
        spinnerCurrency.setAdapter(spinnerAdpter);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewCurrencyCode.setText(getActivity().getResources().getString(R.string.currency_code) + " : " + filteredArrayListCurrency.get(position).get("code"));
                textViewCyrrencySymbol.setText(getActivity().getResources().getString(R.string.currency_symbol) + " : " + filteredArrayListCurrency.get(position).get("symbol"));
                selectedCurrencyMap=filteredArrayListCurrency.get(position);
                selectedCurrencyMap.put("type","child");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onResume() {
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            flagForEdit=1;
            String selectedCurrency=bundle.getString("code")+"  "+bundle.getString("name");
            for(int i=0;i<filteredArrayListCurrency.size();i++)
            {
                if(selectedCurrency.equals(filteredArrayListCurrency.get(i).get("code")+"  "+filteredArrayListCurrency.get(i).get("country")))
                {
                    spinnerCurrency.setSelection(i);
                    break;
                }
            }
            recId=bundle.getString("id");
            textViewCurrencyCode.setText(getActivity().getResources().getString(R.string.currency_code) + " : " + bundle.getString("code"));
            log("Get Bundle Code ="+getActivity().getResources().getString(R.string.currency_code) + " : "+ bundle.getString("code"));
            textViewCyrrencySymbol.setText(getActivity().getResources().getString(R.string.currency_symbol) + " : " +bundle.getString("symbol"));
            log("Get Bundle Symbol ="+getActivity().getResources().getString(R.string.currency_symbol) + " : " +bundle.getString("symbol"));
            editTextCurrencyRate.setText(bundle.getString("rate"));
            log("Get Bundle Rate ="+bundle.getString("rate"));

        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save_item,menu);
            super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {

            if(!editTextCurrencyRate.getText().toString().equals("") )
            {


                selectedCurrencyMap.put("rate",editTextCurrencyRate.getText().toString());
                if(flagForEdit==0)
                {
                    StoreSQLiteData storeSQLiteData=new StoreSQLiteData(getActivity());
                    storeSQLiteData.AddNewCurrency(selectedCurrencyMap);
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.currency_added_toast),Toast.LENGTH_LONG).show();

                }
                else
                {

                    UpdateSQLiteData updateSQLiteData=new UpdateSQLiteData(getActivity());
                    updateSQLiteData.updateCurrency(recId,selectedCurrencyMap);
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.currency_updated_toast),Toast.LENGTH_LONG).show();
                }




                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextCurrencyRate.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                BeginPopStack beginPopStack= (BeginPopStack) getActivity();
                beginPopStack.beginPopStackTransaction();

            }
            else
            {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.add_currency_rate_toast),Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }



    void log(String msg)
    {
        Log.d("FragmentAddCurrency",msg);
    }
}
