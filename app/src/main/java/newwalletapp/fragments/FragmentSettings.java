package newwalletapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.accounts.newwalletapp.MainActivity;
import com.example.accounts.newwalletapp.R;

import newwalletapp.backup.DataBackup;
import newwalletapp.backup.ScheduleBackup;
import newwalletapp.database.DataPrefrences;
import newwalletapp.extras.Constants;
import newwalletapp.interfaces.AccountsInterface;
import newwalletapp.interfaces.CategoriesInterface;
import newwalletapp.interfaces.CurrienciesInterface;
import newwalletapp.interfaces.PasswordTransaction;


/**
 * Created by ahmedchoteri on 16-02-15.
 */
public class FragmentSettings extends Fragment {

    String currentCheck = "";
    int FlagForpassword = 0;
    ListView listViewBasic, listViewOther;
    ArrayAdapter<String> adpBasic, adpOther;
    FragmentManager fragmentManager;
    DataPrefrences dataPrefrences;
    FragmentAccounts fragmentAccounts = new FragmentAccounts();
    FragmentCurrencies fragmentCurrencies = new FragmentCurrencies();
    FragmentCategories fragmentCategories = new FragmentCategories();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;

    }

    @Override
    public void onResume() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings);
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataPrefrences = new DataPrefrences(getActivity());
        fragmentManager = getFragmentManager();
        listViewBasic = (ListView) getActivity().findViewById(R.id.listViewBasicSettings);
        listViewOther = (ListView) getActivity().findViewById(R.id.listViewBasicOtherSettings);
        adpBasic = new ArrayAdapter<String>(getActivity(), R.layout.listitem_setting, getResources().getStringArray(R.array.settings_basic));
        listViewBasic.setAdapter(adpBasic);
        adpOther = new ArrayAdapter<String>(getActivity(), R.layout.listitem_setting, getResources().getStringArray(R.array.settings_other));
        listViewOther.setAdapter(adpOther);
        listViewBasic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    AccountsInterface accountsInterface = (AccountsInterface) getActivity();
                    accountsInterface.beginAccountsFragmentTransaction();
                    //fragmentManager.beginTransaction().replace(R.id.layout_settings_container,fragmentAccounts).addToBackStack("accounts").commit();
                } else if (position == 1) {
                    CurrienciesInterface currienciesInterface = (CurrienciesInterface) getActivity();
                    currienciesInterface.beginCurrienciesFragmentTransaction();
                    //fragmentManager.beginTransaction().replace(R.id.layout_settings_container,fragmentCurrencies).addToBackStack("currencies").commit();
                } else if (position == 2) {
                    CategoriesInterface categoriesInterface = (CategoriesInterface) getActivity();
                    categoriesInterface.beginCategoriesFragmentTransaction();
                    //fragmentManager.beginTransaction().replace(R.id.layout_settings_container,fragmentCurrencies).addToBackStack("currencies").commit();
                    //fragmentManager.beginTransaction().replace(R.id.layout_settings_container,fragmentCategories).addToBackStack("categories").commit();
                }
            }
        });
        listViewOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    int previoslyCheckedId = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getActivity().getResources().getString(R.string.dialog_backup_interval));
                    final String checkedRedio = dataPrefrences.GetStoredPrefrence("backup_interval");
                    currentCheck = dataPrefrences.GetStoredPrefrence("backup_interval");
                    Log.d("FragmentSettings", "checkedRedio=" + checkedRedio);
                    final RadioGroup rg = new RadioGroup(getActivity());
                    rg.setOrientation(RadioGroup.VERTICAL);

                    final RadioButton radioButtonBackupDaily = new RadioButton(getActivity());
                    radioButtonBackupDaily.setId(R.id.radio_backup_daily);
                    radioButtonBackupDaily.setText(getActivity().getResources().getString(R.string.backup_daily));
                    radioButtonBackupDaily.setPadding(10, 5, 20, 5);
                    rg.addView(radioButtonBackupDaily);
                    if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_daily))) {
                        rg.check(radioButtonBackupDaily.getId());
                        previoslyCheckedId = radioButtonBackupDaily.getId();
                    }

                    final RadioButton radioButtonBackupWeekly = new RadioButton(getActivity());
                    radioButtonBackupWeekly.setId(R.id.radio_backup_weekly);
                    radioButtonBackupWeekly.setText(getActivity().getResources().getString(R.string.backup_weekly));
                    radioButtonBackupWeekly.setPadding(10, 5, 20, 5);
                    rg.addView(radioButtonBackupWeekly);
                    if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_weekly))) {
                        rg.check(radioButtonBackupWeekly.getId());
                        previoslyCheckedId = radioButtonBackupDaily.getId();
                    }

                    final RadioButton radioButtonBackupMonthly = new RadioButton(getActivity());
                    radioButtonBackupMonthly.setId(R.id.radio_backup_monthly);
                    radioButtonBackupMonthly.setText(getActivity().getResources().getString(R.string.backup_monthly));
                    radioButtonBackupMonthly.setPadding(10, 5, 20, 5);
                    rg.addView(radioButtonBackupMonthly);
                    if (checkedRedio.equals(getActivity().getResources().getString(R.string.backup_monthly))) {
                        //radioButtonBackupMonthly.setChecked(true);
                        rg.check(radioButtonBackupMonthly.getId());
                        previoslyCheckedId = radioButtonBackupDaily.getId();
                    }

                    rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                            currentCheck = radioButton.getText().toString();
                            Toast.makeText(getActivity(), "" + radioButton.getText(), Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.setView(rg);
                    builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
                    builder.setNegativeButton(getActivity().getResources().getString(R.string.backup_now), null);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Button buttonOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    final int finalPrevioslyCheckedId = previoslyCheckedId;
                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int radioButtonId = rg.getCheckedRadioButtonId();
                            Log.d("FragmentSettings", "Positive Button Clicked ID " + radioButtonId);

                            if (!currentCheck.equals(checkedRedio)) {

                                ScheduleBackup scheduleBackup = new ScheduleBackup(getActivity());
                                Log.d("FragmentSettings", "Check Radiobuttn Id " + radioButtonId + " Previously Checked id " + finalPrevioslyCheckedId);
                                if (radioButtonId == radioButtonBackupDaily.getId()) {
                                    Log.d("FragmentSettings", "Backup Interval Change to " + getActivity().getResources().getString(R.string.backup_daily));
                                    dataPrefrences.StorePrefrence("backup_interval", getResources().getString(R.string.backup_daily));
                                    scheduleBackup.changeAlarmInterval(getActivity().getResources().getString(R.string.backup_daily));
                                    dialog.dismiss();

                                } else if (radioButtonId == radioButtonBackupWeekly.getId()) {
                                    Log.d("FragmentSettings", "Backup Interval Change to " + getActivity().getResources().getString(R.string.backup_weekly));
                                    dataPrefrences.StorePrefrence("backup_interval", getResources().getString(R.string.backup_weekly));
                                    scheduleBackup.changeAlarmInterval(getActivity().getResources().getString(R.string.backup_weekly));
                                    dialog.dismiss();

                                } else if (radioButtonId == radioButtonBackupMonthly.getId()) {
                                    Log.d("FragmentSettings", "Backup Interval Change to " + getActivity().getResources().getString(R.string.backup_monthly));
                                    dataPrefrences.StorePrefrence("backup_interval", getResources().getString(R.string.backup_monthly));
                                    scheduleBackup.changeAlarmInterval(getActivity().getResources().getString(R.string.backup_monthly));
                                    dialog.dismiss();

                                }
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    Button buttonBackupNow = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    buttonBackupNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataBackup dataBackup = new DataBackup(getActivity());
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.database_export) + " " + dataBackup.exportDatabase(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });


                } else if (position == 1) {


                    FlagForpassword = 0;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(getActivity().getResources().getString(R.string.dialog_password));
                    final CheckBox checkBoxPassword = new CheckBox(getActivity());
                    checkBoxPassword.setText(getActivity().getString(R.string.enable_password));
                    checkBoxPassword.setPadding(10, 5, 20, 5);
                    if (dataPrefrences.GetStoredPrefrence("password").equals("N/A")) {
                        checkBoxPassword.setChecked(false);
                    } else {
                        checkBoxPassword.setChecked(true);
                    }
                    checkBoxPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            FlagForpassword = 1;
                        }
                    });

                    builder.setView(checkBoxPassword);
                    builder.setCancelable(false);
                    builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
                    builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (FlagForpassword != 0) {
                                PasswordTransaction passwordTransaction = (PasswordTransaction) getActivity();
                                passwordTransaction.begingPassWordFragmentTransaction();
                                dialog.dismiss();
                            } else {
                                dialog.dismiss();
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

                } else if (position == 2) {
                    final String[] languages = {"English", "العربية"};
                    final int langpos = (dataPrefrences.GetStoredPrefrence("currentlanguage").equals("en_US")||dataPrefrences.GetStoredPrefrence("currentlanguage").equals("N/A"))?0:1;
                    final AlertDialog.Builder dialogsetlanguage = new AlertDialog.Builder(getActivity());
                    dialogsetlanguage.setTitle(R.string.setLanguage_dialog);
                    dialogsetlanguage.setCancelable(true).setSingleChoiceItems(languages, langpos, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int pos) {
                            String currentlanguage = "en_US";
                            if (pos == 0) {
                                currentlanguage = "en_US";
                            } else if (pos == 1) {
                                currentlanguage = "ar";
                            }
                            dataPrefrences.StorePrefrence("currentlanguage",currentlanguage);
                            Constants.SetLanguage(getActivity(), currentlanguage);
                            Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            dialog1.cancel();
                        }
                    })
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int id) {
                                    dialog1.cancel();

                                }
                            });
                    AlertDialog alertDialog = dialogsetlanguage.create();
                    alertDialog.show();
                }else if(position == 3){
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}