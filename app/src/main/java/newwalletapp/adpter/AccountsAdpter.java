package newwalletapp.adpter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.DataPrefrences;
import newwalletapp.database.DeleteSQLiteData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.UpdateSQLiteData;
import newwalletapp.fragments.FragmentAccounts;
import newwalletapp.interfaces.AccountsInterface;

/**
 * Created by ahmedchoteri on 16-02-15.
 */
public class AccountsAdpter extends BaseAdapter {

    int selectedPosition;


    DataPrefrences dataPrefrences;
    ArrayList<HashMap<String, String>> arrayListAccounts;
    Context context;

    UpdateSQLiteData updateSQLiteData;

    public AccountsAdpter(Context con, ArrayList<HashMap<String, String>> arrayList) {

        this.context = con;

        dataPrefrences=new DataPrefrences(con);
        this.arrayListAccounts = arrayList;
        updateSQLiteData=new UpdateSQLiteData(con);

    }

    @Override
    public int getCount() {
        return arrayListAccounts.size();
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
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View v = convertView;
        Log.d("Adpter", "Check AccountsAdpter");
        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.listitem_accounts, null);
        }

        TextView textViewAccountName= (TextView) v.findViewById(R.id.textViewAccountsName);
        textViewAccountName.setText(arrayListAccounts.get(position).get("acc_name"));
        ImageView imageViewEditAccount= (ImageView) v.findViewById(R.id.imageViewAccountsEdit);
        imageViewEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Edit Position "+position,Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(context.getResources().getString(R.string.dialog_account_update_title));
                final EditText editTextAccount = new EditText(context);
                editTextAccount.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                editTextAccount.setPadding(20,5,20,5);

                editTextAccount.setHint(context.getResources().getString(R.string.dialog_account_hint));
                editTextAccount.setText(arrayListAccounts.get(position).get("acc_name"));
                builder.setView(editTextAccount);
                builder.setCancelable(false);
                builder.setPositiveButton(context.getResources().getString(R.string.update), null);
                builder.setNegativeButton(context.getResources().getString(R.string.cancel), null);

                final AlertDialog dialog = builder.create();

                dialog.show();

               // Button btnAccountDialogPositiv,btnAccountDialogNagetive;
                Button btnPositive=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!editTextAccount.getText().toString().equals(""))
                        {

                          if(updateSQLiteData.updateAccount(arrayListAccounts.get(position).get("id"),arrayListAccounts.get(position).get("acc_name"),editTextAccount.getText().toString())==1)
                          {
                              Toast.makeText(context,context.getResources().getString(R.string.account_available),Toast.LENGTH_LONG).show();
                          }
                          else
                          {
                              AccountsInterface accountsInterface= (AccountsInterface) context;
                              accountsInterface.updateAccountsList(context);
                              if(dataPrefrences.GetStoredPrefrence("budget_account_filter").equals(arrayListAccounts.get(position).get("acc_name")))
                              {
                                  dataPrefrences.StorePrefrence("budget_account_filter",editTextAccount.getText().toString());
                              }
                              if(dataPrefrences.GetStoredPrefrence("filter_account").equals(arrayListAccounts.get(position).get("acc_name")))
                              {
                                  dataPrefrences.StorePrefrence("filter_account",editTextAccount.getText().toString());
                              }

                              dialog.dismiss();

                          }
                        }


                    }
                });
                Button btnNegative=dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });






            }
        });
        ImageView imageViewDeleteAccount= (ImageView) v.findViewById(R.id.imageViewAccountsDelete);
        imageViewDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);


                final TextView textViewMessage=new TextView(context);
                textViewMessage.setText(context.getResources().getString(R.string.dialog_account_delete_message));
                textViewMessage.setPadding(20,20,0,10);
                textViewMessage.setGravity(Gravity.CENTER_VERTICAL);
                textViewMessage.setTextAppearance(context,android.R.style.TextAppearance_Medium);

                builder.setView(textViewMessage);
                builder.setCancelable(false);
                builder.setPositiveButton(context.getResources().getString(R.string.yes), null);
                builder.setNegativeButton(context.getResources().getString(R.string.no), null);

                final AlertDialog dialog = builder.create();

                dialog.show();

                // Button btnAccountDialogPositiv,btnAccountDialogNagetive;
                final Button btnPositive=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                final Button btnNegative=dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ReadSQLiteData readSQLiteData=new ReadSQLiteData(context);
                        if(!readSQLiteData.getMasterAccount().equals(arrayListAccounts.get(position).get("acc_name")))
                        {
                            DeleteSQLiteData deleteSQLiteData=new DeleteSQLiteData(context);
                            deleteSQLiteData.deleteAccout(arrayListAccounts.get(position).get("id"),arrayListAccounts.get(position).get("acc_name"));
                            AccountsInterface accountsInterface= (AccountsInterface) context;
                            accountsInterface.updateAccountsList(context);
                            if(dataPrefrences.GetStoredPrefrence("budget_account_filter").equals(arrayListAccounts.get(position).get("acc_name")))
                            {
                                dataPrefrences.StorePrefrence("budget_account_filter",readSQLiteData.getMasterAccount());
                            }
                            if(dataPrefrences.GetStoredPrefrence("filter_account").equals(arrayListAccounts.get(position).get("acc_name")))
                            {
                                dataPrefrences.StorePrefrence("filter_account","N/A");
                            }

                            dialog.dismiss();
                        }
                        else
                        {
                            textViewMessage.setText(context.getResources().getString(R.string.admin_account_msg));
                            btnPositive.setEnabled(false);
                            btnNegative.setText(context.getResources().getString(R.string.cancel));


                        }


                    }
                });

                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return v;
    }

    class AccountDialogListner implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }

}
