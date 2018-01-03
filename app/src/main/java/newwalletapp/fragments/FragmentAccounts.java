package newwalletapp.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import newwalletapp.adpter.AccountsAdpter;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;
import newwalletapp.interfaces.AccountsInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccounts extends android.support.v4.app.Fragment{

String Tag="FragmentAccounts";
    ListView listViewAccounts;
    FragmentManager fragmentManager;
    ReadSQLiteData readSQLiteData;
    AccountsAdpter accountsAdpter;
    TextView textViewEmptyListView;
    public FragmentAccounts() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_accounts);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onResume() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_accounts);
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fragmentManager=getFragmentManager();
        readSQLiteData=new ReadSQLiteData(getActivity());
        listViewAccounts= (ListView) getActivity().findViewById(R.id.listViewAccounts);
        textViewEmptyListView= (TextView) getActivity().findViewById(R.id.textViewAccountsDefaultListText);
        listViewAccounts.setEmptyView(textViewEmptyListView);
        accountsAdpter=new AccountsAdpter(getActivity(),readSQLiteData.getAllAccountsData());
        listViewAccounts.setAdapter(accountsAdpter);


        super.onActivityCreated(savedInstanceState);
    }

    public void updateAccounts(Context context) {
        Log.d(Tag,"updateAccounts() method call from Setting Activity");
        //listViewAccounts= (ListView) getActivity().findViewById(R.id.listViewAccounts);
        //readSQLiteData=new ReadSQLiteData(context);
        accountsAdpter=new AccountsAdpter(context,readSQLiteData.getAllAccountsData());
        accountsAdpter.notifyDataSetChanged();
        listViewAccounts.setAdapter(accountsAdpter);
        //listViewAccounts.setAdapter(new AccountsAdpter(getActivity(),readSQLiteData.getAllAccountsData()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings_child,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getActivity().getResources().getString(R.string.dialog_account_add_title));
            final EditText editTextAccount = new EditText(getActivity());
            editTextAccount.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
            editTextAccount.setPadding(20, 5, 20, 5);
            editTextAccount.setHint(getActivity().getResources().getString(R.string.dialog_account_hint));
            builder.setView(editTextAccount);
            builder.setCancelable(false);
            builder.setPositiveButton(getActivity().getResources().getString(R.string.add_account), null);
            builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
            final AlertDialog dialog = builder.create();
            dialog.show();
            // Button btnAccountDialogPositiv,btnAccountDialogNagetive;
            Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editTextAccount.getText().toString().equals("")) {
                        StoreSQLiteData storeSQLiteData=new StoreSQLiteData(getActivity());
                        if (storeSQLiteData.AddNewAccount(editTextAccount.getText().toString(),"child")==1)
                        {
                            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.account_available),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AccountsInterface accountsInterface = (AccountsInterface) getActivity();
                            accountsInterface.updateAccountsList(getActivity());
                            dialog.dismiss();

                        }
                    }


                }
            });
            Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }


        return super.onOptionsItemSelected(item);


    }
}
