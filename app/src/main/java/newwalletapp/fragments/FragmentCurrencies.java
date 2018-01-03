package newwalletapp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.accounts.newwalletapp.R;

import newwalletapp.adpter.CurrencyListAdpter;
import newwalletapp.database.DeleteSQLiteData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.interfaces.AddCurrencyInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCurrencies extends android.support.v4.app.Fragment {


    FragmentManager fragmentManager;
    ListView listViewCurrencyList;
    CurrencyListAdpter currencyListAdpter;
    ReadSQLiteData readSQLiteData;
    public FragmentCurrencies() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_currencies);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currencies, container, false);
    }

    @Override
    public void onResume() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_currencies);
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fragmentManager=getFragmentManager();
        readSQLiteData=new ReadSQLiteData(getActivity());
        currencyListAdpter=new CurrencyListAdpter(getActivity(),readSQLiteData.getAllStoredCurrencies());
        listViewCurrencyList= (ListView) getActivity().findViewById(R.id.listViewCurrenciesList);

        listViewCurrencyList.setAdapter(currencyListAdpter);
        super.onActivityCreated(savedInstanceState);
    }

    public void deleteCurrency(String idCurrency,String code,String name)
    {
        DeleteSQLiteData deleteSQLiteData=new DeleteSQLiteData(getActivity());
        deleteSQLiteData.deleteCurrencyData(idCurrency,code,name);
        currencyListAdpter=new CurrencyListAdpter(getActivity(),readSQLiteData.getAllStoredCurrencies());
        currencyListAdpter.notifyDataSetChanged();
        listViewCurrencyList.setAdapter(currencyListAdpter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings_child,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_add) {
            AddCurrencyInterface addCurrencyInterface= (AddCurrencyInterface) getActivity();
            addCurrencyInterface.beginAddCurrencyTransaction(null);

        }
            return super.onOptionsItemSelected(item);
    }



}
