package newwalletapp.fragmentsmain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


import com.example.accounts.newwalletapp.MainActivity;
import com.example.accounts.newwalletapp.R;
import com.example.accounts.newwalletapp.SplashActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import newwalletapp.adpter.CurrencyAdpter;
import newwalletapp.backup.ImportBackup;
import newwalletapp.currency.CurrencyData;
import newwalletapp.database.DataPrefrences;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;

public class FragmentGateWay extends Fragment {

    TextView textViewStartStrip,textViewLanguageStrip;
    DataPrefrences dataPrefrences;
    ArrayAdapter<String> adapterCurrency;
    ArrayList<HashMap<String,String>> arrayListMapCurrency;
    String[] arrayCurrency;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.fragment_gateway, container,false);

        return rootView;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        dataPrefrences = new DataPrefrences(getActivity());
        textViewStartStrip = (TextView) getActivity().findViewById(R.id.textViewStartStrip);
/*		textViewLanguageStrip= (TextView) getActivity().findViewById(R.id.textViewLanguageStrip);
		textViewLanguageStrip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(getActivity().getResources().getString(R.string.dialog_select_language));
				final RadioGroup rg = new RadioGroup(getActivity());
				rg.setOrientation(RadioGroup.VERTICAL);

				final RadioButton radioButtonLanguageEnglish = new RadioButton(getActivity());
				radioButtonLanguageEnglish.setId(R.id.radio_language_english);
				//radioButtonLanguageEnglish.setText(getActivity().getResources().getString(R.string.language_english));
				radioButtonLanguageEnglish.setPadding(10, 5, 20, 5);
				rg.addView(radioButtonLanguageEnglish);
				rg.check(radioButtonLanguageEnglish.getId());

				if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_daily))) {
					rg.check(radioButtonBackupDaily.getId());
					previoslyCheckedId = radioButtonBackupDaily.getId();
				}


				final RadioButton radioButtonLanguageArabic = new RadioButton(getActivity());
				radioButtonLanguageArabic.setId(R.id.radio_language_arabic);
				//radioButtonLanguageArabic.setText(getActivity().getResources().getString(R.string.language_arabic));
				radioButtonLanguageArabic.setPadding(10, 5, 20, 5);
				rg.addView(radioButtonLanguageArabic);
				builder.setView(rg);
				builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
				builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
				final AlertDialog dialog = builder.create();
				dialog.show();


				if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_weekly))) {
					rg.check(radioButtonBackupWeekly.getId());
					previoslyCheckedId = radioButtonBackupDaily.getId();
				}

			}
		});
*/
        final CurrencyData currencyData = new CurrencyData(getActivity());
        arrayListMapCurrency=currencyData.getCurrencyList();
        arrayCurrency=new String[arrayListMapCurrency.size()];
        for (int i=0;i<arrayListMapCurrency.size();i++)
        {
            arrayCurrency[i]=arrayListMapCurrency.get(i).get("code")+" "+arrayListMapCurrency.get(i).get("country");
        }


        adapterCurrency = new ArrayAdapter<String>(getActivity(),R.layout.listitem_country,arrayCurrency){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    CheckedTextView checkedTxtView = (CheckedTextView) super.getView(position, convertView, parent);
                    String yourValue = arrayCurrency[position];
                    checkedTxtView.setText(yourValue);
                    checkedTxtView.setTextSize(18);
                    //checkedTxtView.setTextAlignment(3);
                    checkedTxtView.setTextColor(getResources().getColor(android.R.color.black));
                    return checkedTxtView;
                }

        };
        textViewStartStrip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final ImportBackup importBackup = new ImportBackup(getActivity());
                if (!importBackup.isBackupAvailable())
                {
                    currencyDialog();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getActivity().getResources().getString(R.string.import_backup));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
                    builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnNegative=dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    btnNegative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            currencyDialog();
                        }
                    });
                    Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AsyncTask<Void,Void,Void>(){
                                ProgressDialog proDialog;

                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    proDialog = new ProgressDialog(getActivity());
                                    proDialog.setMessage(getActivity().getResources().getString(R.string.please_wait));
                                    proDialog.setCancelable(false);
                                    proDialog.show();

                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    importBackup.restoreDb();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    dataPrefrences.StorePrefrence("start", "ok");
                                    dialog.dismiss();
                                    proDialog.dismiss();
                                    startActivity(new Intent(getActivity(),MainActivity.class));

                                }
                            }.execute();


                        }
                    });
                    //importBackup.importDataBase();
                }


            }
        });
    }

    public void currencyDialog()
    {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customTitleView = inflater.inflate(R.layout.dialog_currency_customtitle, null);
        customTitleView.setMinimumHeight(140);
        builder.setCustomTitle(customTitleView);
        builder.setTitle(getActivity().getResources().getString(R.string.select_currency));

        final ListView listViewCurrency = new ListView(getActivity());
        listViewCurrency.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listViewCurrency.setAdapter(adapterCurrency);
        listViewCurrency.setItemChecked(0,true);

        builder.setView(listViewCurrency);
        builder.setCancelable(false);
        builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
        builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
        final AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.show();
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        //btnPositive.setTextColor(Integer.parseInt("#ffffff"));
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void, Void, Void>() {

                    ProgressDialog proDialog;

                    @Override
                    protected void onPreExecute() {
                        // TODO Auto-generated method stub
                        super.onPreExecute();

                        proDialog = new ProgressDialog(getActivity());
                        proDialog.setMessage(getActivity().getResources().getString(R.string.please_wait));
                        proDialog.setCancelable(false);
                        proDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        StoreSQLiteData storeSQLiteData = new StoreSQLiteData(getActivity());

                        storeSQLiteData.storeCurrencyData(arrayListMapCurrency.get(listViewCurrency.getCheckedItemPosition()),"master");
                        addDefaultCategories();
                        storeSQLiteData.AddNewAccount(getActivity().getResources().getString(R.string.my_account),"master");
                        dataPrefrences.StorePrefrence("budget_account_filter","My Account");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        // TODO Auto-generated method stub
                        super.onPostExecute(result);
                        proDialog.dismiss();
                        dialog.dismiss();
                        dataPrefrences.StorePrefrence("start", "ok");
                        startActivity(new Intent(getActivity(),MainActivity.class));

                    }
                }.execute(null, null, null);

                dialog.dismiss();
            }

        });
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        //btnNegative.setTextColor(Integer.parseInt("#ffffff"));
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void addDefaultCategories()
    {
        StoreSQLiteData storeSQLiteData=new StoreSQLiteData(getActivity());
        HashMap<String,String> map;
        String strExpence="expense",strIncome="income";


        map=new HashMap<String, String>();
        map.put("name", "Car");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","57");
        map.put("color","#F90320");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Groceries");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","59");
        map.put("color","#2F82E0");

        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Eating Out");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","52");
        map.put("color","#09DD8B");

        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Salary,Income");
        map.put("des", "");
        map.put("type", strIncome);
        map.put("icon","56");
        map.put("color","#02F124");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Sports");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","4");
        map.put("color","#F1F41C");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Transport");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","46");
        map.put("color","#B9BA8E");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Entertainment, culture");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","16");
        map.put("color","#4FF6E0");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Wardrobe");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","55");
        map.put("color","#0650CA");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Personal");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","53");
        map.put("color","#CA7906");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Kids");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","11");
        map.put("color","#CA06B5");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Pets");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","14");
        map.put("color","#96E8FC");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Household, utilities");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","25");
        map.put("color","#4E078B");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Phone, internet");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","37");
        map.put("color","#8B0736");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Electronics");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","49");
        map.put("color","#06FCCB");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Mortgage, rent");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","10");
        map.put("color","#7C807F");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

        map=new HashMap<String, String>();
        map.put("name", "Loans,insurance");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","54");
        map.put("color","#0C00FF");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);



        map=new HashMap<String, String>();
        map.put("name", "Other");
        map.put("des", "");
        map.put("type", strExpence);
        map.put("icon","26");
        map.put("color","#47013C");
        map.put("parent", "");
        storeSQLiteData.AddNewCategoury(map);

    }
}