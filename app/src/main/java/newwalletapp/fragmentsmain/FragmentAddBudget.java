package newwalletapp.fragmentsmain;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.database.DeleteSQLiteData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;
import newwalletapp.database.UpdateSQLiteData;
import newwalletapp.interfaces.BeginPopStack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddBudget extends android.support.v4.app.Fragment {


    Spinner spinnerBudgetAccount, spinnerBudgetInterval;
    Button buttonBudgetCategory;
    String selectedAccount, selectedInterval;
    EditText editTextAmount, editTextName;
    ArrayAdapter<String> adapterAccounts, adapterInterval, adapterCategories;
    //CategoryMultyChoiceAdpter adapterCategories;
    Menu myMenu;
    ArrayList<Integer> checkIndex;
    ArrayList<String> checkedCategories;
    ReadSQLiteData readSQLiteData;
    ArrayList<HashMap<String, String>> arrayListAccounts;
    ArrayList<HashMap<String, String>> arrayListCategories;
    String[] arrayAccounts;
    String[] arrayCategories;
    String oldBudgetName;
    String[] arrayInterval = {"Week", "Month", "Year"};
    int FlagForShowData = 0,FlagForCategoryList=0;
    String recId;
    public FragmentAddBudget() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_budget, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readSQLiteData = new ReadSQLiteData(getActivity());
        spinnerBudgetAccount = (Spinner) getActivity().findViewById(R.id.spinnerAddBudgetAccounts);
        buttonBudgetCategory = (Button) getActivity().findViewById(R.id.buttonAddBudgetCategory);
        spinnerBudgetInterval = (Spinner) getActivity().findViewById(R.id.spinnerAddBudgetInterval);
        editTextAmount = (EditText) getActivity().findViewById(R.id.editTextAddBudgetEnterAmmount);
        editTextName = (EditText) getActivity().findViewById(R.id.editTextAddBudgetName);

        arrayListAccounts = readSQLiteData.getAllAccountsData();
        arrayAccounts = new String[arrayListAccounts.size()];
        arrayListCategories = readSQLiteData.getAllCategories();
        arrayCategories = new String[arrayListCategories.size()];

        //arrayAccounts[0] = "All Accounts";
        for (int i = 0; i < arrayListAccounts.size(); i++) {
            arrayAccounts[i] = arrayListAccounts.get(i).get("acc_name");
        }

        adapterAccounts = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayAccounts);

        spinnerBudgetAccount.setAdapter(adapterAccounts);
        spinnerBudgetAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedAccount = arrayAccounts[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //arrayCategories[0]=getActivity().getResources().getString(R.string.select_category);

        for (int i = 0; i < arrayListCategories.size(); i++) {
            arrayCategories[i] = arrayListCategories.get(i).get("name");
        }
        adapterCategories = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, arrayCategories);
        //adapterCategories=new CategoryMultyChoiceAdpter(getActivity(),arrayCategories);
        buttonBudgetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(getActivity().getResources().getString(R.string.select_category));
                final ListView listViewCategories = new ListView(getActivity());
                listViewCategories.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                listViewCategories.setAdapter(adapterCategories);
                listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //adapterCategories.setSelectedItem(position);

                        Log.d("Listview Item check ", "Position " + position + "is checked " + listViewCategories.isItemChecked(position));
                        if (listViewCategories.isItemChecked(position)) {
                            listViewCategories.setItemChecked(position, true);
                        } else {
                            listViewCategories.setItemChecked(position, false);
                        }


                    }
                });
                if (checkIndex != null) {
                    for (int i = 0; i < checkIndex.size(); i++) {
                        listViewCategories.setItemChecked(checkIndex.get(i), true);
                    }
                }
                builder.setView(listViewCategories);
                builder.setCancelable(false);
                builder.setPositiveButton(getActivity().getResources().getString(R.string.ok), null);
                builder.setNegativeButton(getActivity().getResources().getString(R.string.cancel), null);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkIndex = new ArrayList<Integer>();
                        checkedCategories = new ArrayList<String>();
                        for (int i = 0; i < arrayCategories.length; i++) {
                            if (listViewCategories.isItemChecked(i)) {
                                checkIndex.add(i);
                                log("ListView Checked Items Count=" + listViewCategories.getCheckedItemCount());
                                log("ListView Checked Items =" + arrayCategories[i]);
                                checkedCategories.add(arrayCategories[i]);
                            }
                        }
                        dialog.dismiss();
                    }

                });
                if (FlagForCategoryList==1)
                {
                    btnPositive.setEnabled(false);
                }
                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

        adapterInterval=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayInterval);
        spinnerBudgetInterval.setAdapter(adapterInterval);
        spinnerBudgetInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedInterval=arrayInterval[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onResume() {
        Bundle b=getArguments();
        if(b!=null)
        {

            log("check bundle ="+b);
            // this.extrasValues=b;
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_budget));
            setUpDataForShow(b);
            FlagForCategoryList=1;
            lockViews(false);
        }
        else
        {
            log("check bundle ="+b);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.action_addnew_budget));
            makeBlank();

        }

        super.onResume();
    }
    void setUpDataForShow(Bundle extras)
    {
        if (extras != null) {
            FlagForShowData = 1;
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.show_budget));
            HashMap<String, String> map = (HashMap<String, String>) extras.getSerializable("map");
            recId = map.get("bud_id");
            for (int i = 0; i < arrayAccounts.length; i++) {
                if (arrayAccounts[i].equals(map.get("bud_account"))) {
                    spinnerBudgetAccount.setSelection(i);
                    break;
                }
            }
            editTextAmount.setText(map.get("bud_amount"));
            editTextName.setText(map.get("bud_name"));
            oldBudgetName=map.get("bud_name");
            //interval
            for (int i = 0; i < arrayInterval.length; i++) {
                if (arrayInterval[i].equals(map.get("interval"))) {
                    spinnerBudgetInterval.setSelection(i);
                    break;
                }
            }
            checkedCategories = new ArrayList<String>();
            String[] cat=readSQLiteData.getBudgetCategories(map.get("bud_id"));
            for (int i=0;i<cat.length;i++)
            {
                checkedCategories.add(cat[i]);
                log("Budget Categories =" + cat[i]);
            }
            checkIndex = new ArrayList<Integer>();
            for (int i=0;i<arrayCategories.length;i++)
            {
                for (int j=0;j<cat.length;j++)
                {
                    if (cat[j].equals(arrayCategories[i]))
                    {
                        checkIndex.add(i);
                        log("checkIndex ="+i);
                        break;
                    }
                }
            }





        }
    }

    void lockViews(boolean flag)
    {
        spinnerBudgetAccount.setClickable(flag);
        spinnerBudgetInterval.setClickable(flag);
        if (flag) {
            editTextAmount.setFocusableInTouchMode(flag);
            editTextAmount.setFocusable(flag);
            editTextName.setFocusableInTouchMode(flag);
            editTextName.setFocusable(flag);
            editTextAmount.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextAmount, InputMethodManager.SHOW_IMPLICIT);
        } else {
            editTextAmount.setFocusable(flag);
            editTextName.setFocusable(flag);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
        }

    }

    void makeBlank()
    {

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        myMenu = menu;
        MenuItem menuItem = myMenu.findItem(R.id.action_addnew_record);
        menuItem.setVisible(false);
        if (FlagForShowData == 0) {
            getActivity().getMenuInflater().inflate(R.menu.save_item, myMenu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.menu_record_edit, myMenu);
            MenuItem item_update = myMenu.findItem(R.id.action_record_edit_update);
            item_update.setVisible(false);
            MenuItem item_cancel = myMenu.findItem(R.id.action_record_edit_cancel);
            item_cancel.setVisible(false);

        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {

            int flagForSave = 0;
            if (editTextName.getText().toString().equals("")) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_name), Toast.LENGTH_LONG).show();
            } else if (editTextAmount.getText().toString().equals("")) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();

            } else if (checkedCategories == null || checkedCategories.size()==0) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.select_category), Toast.LENGTH_LONG).show();

            }

            if (flagForSave == 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", editTextName.getText().toString());
                map.put("amount", editTextAmount.getText().toString());
                map.put("account", selectedAccount);
                map.put("interval",selectedInterval);

                StoreSQLiteData storeSQLiteData=new StoreSQLiteData(getActivity());

                if(storeSQLiteData.AddBudget(map,checkedCategories)!=0)
                {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.budget_available),Toast.LENGTH_LONG).show();
                }
                else
                {
                    editTextAmount.setText("");
                    editTextName.setText("");
                    int count=checkedCategories.size();
                    checkedCategories.clear();
                    checkIndex.clear();
                    spinnerBudgetAccount.setSelection(0);
                    spinnerBudgetInterval.setSelection(0);
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.budget_added),Toast.LENGTH_LONG).show();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
                    getActivity().onBackPressed();
                }
            }

        }else if (itemId == R.id.action_record_edit)
        {
            lockViews(true);
            FlagForCategoryList=0;
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.edit_budget_detail));
            MenuItem item_update = myMenu.findItem(R.id.action_record_edit_update);
            item_update.setVisible(true);
            MenuItem item_cancel = myMenu.findItem(R.id.action_record_edit_cancel);
            item_cancel.setVisible(true);
            MenuItem item_edit = myMenu.findItem(R.id.action_record_edit);
            item_edit.setVisible(false);
            MenuItem item_delete = myMenu.findItem(R.id.action_record_delete);
            item_delete.setVisible(false);
            lockViews(true);

        }else if(itemId == R.id.action_record_edit_cancel)
        {
            FlagForCategoryList=1;
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_budget));
            MenuItem item_update = myMenu.findItem(R.id.action_record_edit_update);
            item_update.setVisible(false);
            MenuItem item_cancel = myMenu.findItem(R.id.action_record_edit_cancel);
            item_cancel.setVisible(false);
            MenuItem item_edit = myMenu.findItem(R.id.action_record_edit);
            item_edit.setVisible(true);
            MenuItem item_delete = myMenu.findItem(R.id.action_record_delete);
            item_delete.setVisible(true);
            //setUpDataForShow(extrasValues);
            lockViews(false);

        }else if (itemId == R.id.action_record_edit_update)
        {
            int flagForSave = 0;
            if (editTextName.getText().toString().equals("")) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_name), Toast.LENGTH_LONG).show();
            } else if (editTextAmount.getText().toString().equals("")) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();

            } else if (checkedCategories == null || checkedCategories.size()==0) {
                flagForSave = 1;
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.select_category), Toast.LENGTH_LONG).show();

            }

            if (flagForSave == 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", editTextName.getText().toString());
                map.put("amount", editTextAmount.getText().toString());
                map.put("account", selectedAccount);
                map.put("interval",selectedInterval);

                //StoreSQLiteData storeSQLiteData=new StoreSQLiteData(getActivity());

                UpdateSQLiteData updateSQLiteData=new UpdateSQLiteData(getActivity());

                if(updateSQLiteData.updateBudget(recId,oldBudgetName,map,checkedCategories)!=0)
                {
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.budget_available),Toast.LENGTH_LONG).show();
                }
                else
                {
                    editTextAmount.setText("");
                    editTextName.setText("");
                    int count=checkedCategories.size();
                    checkedCategories.clear();
                    checkIndex.clear();
                    spinnerBudgetAccount.setSelection(0);
                    spinnerBudgetInterval.setSelection(0);
                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.budget_updated),Toast.LENGTH_LONG).show();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
                    getActivity().onBackPressed();
                }
            }

        }
        else if (itemId == R.id.action_record_delete)
        {

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.delete_record_detail));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.alert_message_budget_delete)).setPositiveButton(getResources().getString(R.string.alert_yes), deleteDialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.alert_no), deleteDialogClickListener).show();
        }


        return super.onOptionsItemSelected(item);

    }
    DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    DeleteSQLiteData deleteSQLiteData=new DeleteSQLiteData(getActivity());

                    Toast.makeText(getActivity(),getResources().getString(R.string.budget_detete_toast),Toast.LENGTH_LONG).show();
                    lockViews(true);
                    makeBlank();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextAmount.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);

                    getActivity().onBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_record_detail));
                    break;
            }
        }
    };

    void log(String msg) {
        Log.d("FragmentAddBudget", msg);
    }
}
