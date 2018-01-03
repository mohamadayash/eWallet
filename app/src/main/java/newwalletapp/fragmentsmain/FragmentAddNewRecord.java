package newwalletapp.fragmentsmain;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.accounts.newwalletapp.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import newwalletapp.adpter.CategoriesSpinnerAdpter;
import newwalletapp.database.DeleteSQLiteData;
import newwalletapp.database.ReadSQLiteData;
import newwalletapp.database.StoreSQLiteData;
import newwalletapp.database.UpdateSQLiteData;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddNewRecord extends android.support.v4.app.Fragment {

    EditText editTextAmmount, editTextDescription;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    Spinner spinnerAccount, spinnerCurrency, spinnerCategory;
    Button buttonIncomeExpence, buttonTime, buttonDate;
    ArrayList<HashMap<String, String>> arrayListAccounts, arrayListCurrency, arrayListCategory;
    ArrayAdapter<String> arrayAdapterAccounts, arrayAdapterCurrency;
    ReadSQLiteData readSQLiteData;
    CategoriesSpinnerAdpter categoriesSpinnerAdpter;
    String time, selectedDate, selectedAccount, selectedCurrency, selectedCategory, recordType;
    int year, month, day, hours, minuts;
    String CurrencyType = "master", CurrencyRate;
    String recId;
    int FlagForShowData = 0;
    Menu myMenu;
    //Bundle extrasValues;
    String[] arrayAccounts;
    String[] arrayCurrency;


    public FragmentAddNewRecord() {
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
        return inflater.inflate(R.layout.fragment_add_new_record, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectedDate = getDate();
        Calendar cal = Calendar.getInstance();
        hours = cal.get(Calendar.HOUR_OF_DAY);
        minuts = cal.get(Calendar.MINUTE);

        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(selectedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cal.setTime(date);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);


        readSQLiteData = new ReadSQLiteData(getActivity());
        editTextAmmount = (EditText)getActivity().findViewById(R.id.editTextAddNewEntryEnterAmmount);
        editTextDescription = (EditText)getActivity().findViewById(R.id.editTextAddNewEntryDescription);

        buttonTime = (Button)getActivity().findViewById(R.id.buttonAddNewEntryTime);

        buttonTime.setText(TimeIntToString(hours, minuts));
        time = TimeIntToString(hours, minuts);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),mTimeSetListener,hours,minuts,true);
                timePickerDialog.setCancelable(false);
                timePickerDialog.show();
                //getActivity().showDialog(TIME_DIALOG_ID);
            }
        });

        buttonDate = (Button)getActivity().findViewById(R.id.buttonAddNewEntryDate);
        buttonDate.setText(selectedDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),mDateSetListener,year,month,day);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();

               // getActivity().showDialog(DATE_DIALOG_ID);

            }
        });

        buttonIncomeExpence = (Button)getActivity().findViewById(R.id.buttonAddNewEntryIncomeExpence);
        arrayListAccounts = readSQLiteData.getAllAccountsData();
        arrayListCategory = readSQLiteData.getAllCategories();
        arrayListCurrency = readSQLiteData.getAllStoredCurrencies();

        arrayAccounts = new String[arrayListAccounts.size()];
        for (int i = 0; i < arrayAccounts.length; i++) {
            arrayAccounts[i] = arrayListAccounts.get(i).get("acc_name");
        }

        arrayCurrency = new String[arrayListCurrency.size()];
        for (int i = 0; i < arrayCurrency.length; i++) {
            arrayCurrency[i] = arrayListCurrency.get(i).get("code") + "  " + arrayListCurrency.get(i).get("name");

        }

        arrayAdapterAccounts = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayAccounts);
        spinnerAccount = (Spinner) getActivity().findViewById(R.id.spinnerAddNewEntryAccounts);
        spinnerAccount.setAdapter(arrayAdapterAccounts);
        spinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedAccount = arrayAccounts[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayListCategory = readSQLiteData.getAllCategories();
        categoriesSpinnerAdpter = new CategoriesSpinnerAdpter(getActivity(), arrayListCategory);
        spinnerCategory = (Spinner)getActivity(). findViewById(R.id.spinnerAddNewEntryCaregory);
        spinnerCategory.setAdapter(categoriesSpinnerAdpter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String type = arrayListCategory.get(position).get("type");
                recordType = arrayListCategory.get(position).get("type");
                selectedCategory = arrayListCategory.get(position).get("name");
                if (type.equals("expense")) {
                    buttonIncomeExpence.setText(getResources().getString(R.string.expense));
                    GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.lightred));
                    buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_minus, 0, 0, 0);
                } else {
                    buttonIncomeExpence.setText(getResources().getString(R.string.income));
                    GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.lightgreen));
                    buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_plus, 0, 0, 0);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayAdapterCurrency = new ArrayAdapter<String>(getActivity(), R.layout.spinneritem_simple, arrayCurrency);
        spinnerCurrency = (Spinner)getActivity().findViewById(R.id.spinnerAddNewEntryCurrency);
        spinnerCurrency.setAdapter(arrayAdapterCurrency);
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrency = arrayCurrency[position];
                CurrencyType = arrayListCurrency.get(position).get("type");
                CurrencyRate = arrayListCurrency.get(position).get("rate");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonIncomeExpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonIncomeExpence.getText().toString().equals(getResources().getString(R.string.expense))) {
                    buttonIncomeExpence.setText(getResources().getString(R.string.income));
                    GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.lightgreen));
                    buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_plus, 0, 0, 0);
                    recordType = "income";
                } else {
                    buttonIncomeExpence.setText(getResources().getString(R.string.expense));
                    GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                    bgShape.setColor(getResources().getColor(R.color.lightred));
                    buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_minus, 0, 0, 0);
                    recordType = "expense";
                }
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
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_record_detail));
            setUpDataForShow(b);
            lockViews(false);
        }
        else
        {
            log("check bundle =" + b);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.action_addnew_record));
            makeBlank();

        }

        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.myMenu = menu;
        MenuItem menuItem = menu.findItem(R.id.action_addnew_record);
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
    void setUpDataForShow(Bundle extras) {
        log("setUpDataForShow");
        if (extras != null) {
            FlagForShowData = 1;
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.show_record_detail));
            HashMap<String, String> map = (HashMap<String, String>) extras.getSerializable("map");
            recId = map.get("id");
            for (int i = 0; i < arrayAccounts.length; i++) {
                if (arrayAccounts[i].equals(map.get("account"))) {
                    spinnerAccount.setSelection(i);
                    break;
                }
            }

            if(map.get("childamount").equals(null) || map.get("childamount").equals(""))
            {
                editTextAmmount.setText("" + map.get("amount"));
            }
            else {
                editTextAmmount.setText("" + map.get("childamount"));
            }

            for (int i = 0; i < arrayCurrency.length; i++) {
                if (arrayCurrency[i].equals(map.get("currency"))) {
                    spinnerCurrency.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < arrayListCategory.size(); i++) {
                if (arrayListCategory.get(i).get("name").equals(map.get("category"))) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }

            if (map.get("type").equals("expense")) {
                buttonIncomeExpence.setText(getResources().getString(R.string.expense));
                GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                bgShape.setColor(getResources().getColor(R.color.lightred));
                buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_minus, 0, 0, 0);
            } else {
                buttonIncomeExpence.setText(getResources().getString(R.string.income));
                GradientDrawable bgShape = (GradientDrawable)buttonIncomeExpence.getBackground();
                bgShape.setColor(getResources().getColor(R.color.lightgreen));
                buttonIncomeExpence.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_button_plus, 0, 0, 0);
            }
            editTextDescription.setText(map.get("desc"));
            buttonTime.setText(map.get("time"));

            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
            try {
                java.sql.Time timeValue1 = new java.sql.Time(formatTime.parse(map.get("time")).getTime());
                this.hours = timeValue1.getHours();
                this.minuts = timeValue1.getMinutes();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");


            try {
                Calendar calendar = Calendar.getInstance();
                Date dt = formatDate.parse(map.get("date"));
                calendar.setTime(dt);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            buttonDate.setText(map.get("date"));
            //lockViews(false);


        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //this.myMenu.findItem(R.id.action_add_item).setEnabled(false);
        int id = item.getItemId();
        log("onOptionsItemSelected");
        if (id == R.id.action_add_item) {
            //Toast.makeText(AddNewRecordActivity.this, "Hiii", Toast.LENGTH_LONG).show();
            if (!editTextAmmount.getText().toString().equals("")) {
                //this.myMenu.findItem(R.id.action_add_item).setEnabled(false);
                HashMap<String, String> mapRecord = new HashMap<String, String>();

                mapRecord.put("account", selectedAccount);
                log("account = " + selectedAccount);

                mapRecord.put("currency", selectedCurrency);
                log("currency =" + selectedCurrency);

                mapRecord.put("category", selectedCategory);
                log("category =" + selectedCategory);

                mapRecord.put("type", recordType);
                log("type =" + recordType);

                if (!editTextDescription.getText().toString().equals("")) {
                    mapRecord.put("des", editTextDescription.getText().toString());
                    log("des =" + editTextDescription.getText().toString());
                } else {
                    mapRecord.put("des", "");
                    log("des =" + "");
                }
                mapRecord.put("time", time);
                log("time =" + time);
                mapRecord.put("date", selectedDate);
                log("date =" + selectedDate);
                mapRecord.put("sdate", "" + ConverDateTimeForSort(convertDateFormat(year, month, day), time));
                log("sdate = " + "" + ConverDateTimeForSort(convertDateFormat(year, month, day), time));
                mapRecord.put("week", getWeekOfYear(year, month, day));
                log("week =" + getWeekOfYear(year, month, day));
                mapRecord.put("month", getMonth(month));
                log("month =" + getMonth(month));
                mapRecord.put("year", "" + year);

                log("year =" + "" + year);
                mapRecord.put("cur_type", CurrencyType);
                log("cur_type =" + CurrencyType);
                if (!CurrencyType.equals("master")) {
                    DecimalFormat decimalFormat = new DecimalFormat("##.##");
                    double rate = Double.parseDouble(CurrencyRate);
                    log("rate = " + Double.parseDouble(CurrencyRate));
                    double amount = Double.parseDouble(editTextAmmount.getText().toString());
                    log("amount" + Double.parseDouble(editTextAmmount.getText().toString()));
                    double convertedAmount = amount / rate;
                    log("convertedAmount=" + amount / rate);
                    mapRecord.put("amount", "" + decimalFormat.format(convertedAmount));
                    log("amount" + "" + decimalFormat.format(convertedAmount));
                    mapRecord.put("childamount", editTextAmmount.getText().toString());
                    log("childamount " + editTextAmmount.getText().toString());

                } else {
                    mapRecord.put("amount", editTextAmmount.getText().toString());
                    log("amount =" + editTextAmmount.getText().toString());
                    mapRecord.put("childamount", "");
                    log("childamount");
                }


                StoreSQLiteData storeSQLiteData = new StoreSQLiteData(getActivity());
                storeSQLiteData.AddNewRecord(mapRecord);
                lockViews(true);
                makeBlank();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAmmount.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextDescription.getWindowToken(), 0);

                getActivity().onBackPressed();


            } else {

                Toast.makeText(getActivity(), "" + getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_record_edit) {
            lockViews(true);
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.edit_record_detail));
            MenuItem item_update = myMenu.findItem(R.id.action_record_edit_update);
            item_update.setVisible(true);
            MenuItem item_cancel = myMenu.findItem(R.id.action_record_edit_cancel);
            item_cancel.setVisible(true);
            MenuItem item_edit = myMenu.findItem(R.id.action_record_edit);
            item_edit.setVisible(false);
            MenuItem item_delete = myMenu.findItem(R.id.action_record_delete);
            item_delete.setVisible(false);
            lockViews(true);

        } else if (id == R.id.action_record_edit_cancel) {
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_record_detail));
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
        } else if (id == R.id.action_record_edit_update) {
            if (!editTextAmmount.getText().toString().equals("")) {
                HashMap<String, String> mapRecord = new HashMap<String, String>();

                mapRecord.put("account", selectedAccount);
                log("account = " + selectedAccount);
                mapRecord.put("currency", selectedCurrency);
                log("currency =" + selectedCurrency);
                mapRecord.put("category", selectedCategory);
                log("category =" + selectedCategory);
                mapRecord.put("type", recordType);
                log("type =" + recordType);
                if (!editTextDescription.getText().toString().equals("")) {
                    mapRecord.put("des", editTextDescription.getText().toString());
                    log("des =" + editTextDescription.getText().toString());
                } else {
                    mapRecord.put("des", "");
                    log("des =" + "");
                }
                mapRecord.put("time", time);
                log("time =" + time);
                mapRecord.put("date", selectedDate);
                log("date =" + selectedDate);
                mapRecord.put("sdate", "" + ConverDateTimeForSort(convertDateFormat(year, month, day), time));
                log("sdate = " + "" + ConverDateTimeForSort(convertDateFormat(year, month, day), time));
                mapRecord.put("week", getWeekOfYear(year, month, day));
                log("week =" + getWeekOfYear(year, month, day));
                mapRecord.put("month", getMonth(month));
                log("month =" + getMonth(month));
                mapRecord.put("year", "" + year);

                log("year =" + "" + year);
                mapRecord.put("cur_type", CurrencyType);
                log("cur_type =" + CurrencyType);
                if (!CurrencyType.equals("master")) {
                    DecimalFormat decimalFormat = new DecimalFormat("##.##");
                    double rate = Double.parseDouble(CurrencyRate);
                    log("rate = " + Double.parseDouble(CurrencyRate));
                    double amount = Double.parseDouble(editTextAmmount.getText().toString());
                    log("amount" + Double.parseDouble(editTextAmmount.getText().toString()));
                    double convertedAmount = amount / rate;
                    log("convertedAmount=" + amount / rate);
                    mapRecord.put("amount", "" + decimalFormat.format(convertedAmount));
                    log("amount" + "" + decimalFormat.format(convertedAmount));
                    mapRecord.put("childamount", editTextAmmount.getText().toString());
                    log("childamount " + editTextAmmount.getText().toString());

                } else {
                    mapRecord.put("amount", editTextAmmount.getText().toString());
                    log("amount =" + editTextAmmount.getText().toString());
                    mapRecord.put("childamount", "");
                    log("childamount");
                }
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                UpdateSQLiteData updateSQLiteData=new UpdateSQLiteData(getActivity());
                updateSQLiteData.updateRecord(recId, mapRecord);
                Toast.makeText(getActivity(), "" + (getResources().getString(R.string.record_update_toast)), Toast.LENGTH_LONG).show();
                lockViews(true);
                makeBlank();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextAmmount.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(editTextDescription.getWindowToken(), 0);

                getActivity().onBackPressed();


            }
            else
            {
                Toast.makeText(getActivity(), ""+getResources().getString(R.string.enter_amount), Toast.LENGTH_LONG).show();
            }
        }else if (id == R.id.action_record_delete)
        {

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.delete_record_detail));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.alert_message_delete)).setPositiveButton(getResources().getString(R.string.alert_yes), deleteDialogClickListener)
                    .setNegativeButton(getResources().getString(R.string.alert_no), deleteDialogClickListener).show();
        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    DialogInterface.OnClickListener deleteDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    DeleteSQLiteData deleteSQLiteData=new DeleteSQLiteData(getActivity());
                    deleteSQLiteData.deleteRecord(recId);
                    Toast.makeText(getActivity(),getResources().getString(R.string.record_detete_toast),Toast.LENGTH_LONG).show();
                    lockViews(true);
                    makeBlank();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextAmmount.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(editTextDescription.getWindowToken(), 0);

                    getActivity().onBackPressed();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.show_record_detail));
                    break;
            }
        }
    };

    void lockViews(Boolean flag) {

        spinnerCurrency.setClickable(flag);
        spinnerAccount.setClickable(flag);

        spinnerCategory.setClickable(flag);
        buttonIncomeExpence.setClickable(flag);

        buttonTime.setClickable(flag);
        buttonDate.setClickable(flag);
        if (flag) {
            editTextAmmount.setFocusableInTouchMode(flag);
            editTextAmmount.setFocusable(flag);
            editTextDescription.setFocusableInTouchMode(flag);
            editTextDescription.setFocusable(flag);
            editTextAmmount.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTextAmmount, InputMethodManager.SHOW_IMPLICIT);
        } else {
            editTextAmmount.setFocusable(flag);
            editTextDescription.setFocusable(flag);
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(getActivity().getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextAmmount.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editTextDescription.getWindowToken(), 0);
        }

    }

    public String getCurrentTime() {
        String time = "";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        time = sdf.format(new Date());
        return time;
    }

    public String getMonth(int mm) {

        return new DateFormatSymbols().getMonths()[month];
    }

    public String getWeekOfYear(int yy, int mm, int dd) {

        Calendar ca1 = Calendar.getInstance();
        ca1.set(yy, mm, dd);

        return "" + ca1.get(Calendar.WEEK_OF_YEAR);

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int mYear,
                                      int mMonthOfYear, int mDayOfMonth) {
                    selectedDate = convertDateFormat(mYear, (mMonthOfYear + 1), mDayOfMonth);
                    log(selectedDate);
                    buttonDate.setText(selectedDate);
                    year = mYear;
                    month = mMonthOfYear;
                    day = mDayOfMonth;
                }
            };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hours = hourOfDay;
            minuts = minute;
            buttonTime.setText(TimeIntToString(hourOfDay, minute));
            time = TimeIntToString(hourOfDay, minute);
        }
    };

    public String convertDateFormat(int year, int month, int day) {
        String dd = "" + day, mm = "" + month, yy = "" + year;

        if (month < 10) {
            mm = "";
            mm = "0" + month;
        }
        if (day < 10) {
            dd = "";
            dd = "0" + day;
        }
        return dd + "-" + mm + "-" + yy;
    }

    public String getDate() {
        Calendar c = Calendar.getInstance(); // "dd:MMMM:yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(c.getTime());

        // date=date.replaceAll(":", "");
        return date.trim();
    }

    public String TimeIntToString(int hh, int mm) {
        String time = null;
        if (hh < 10) {
            time = "0" + hh;
        } else {
            time = "" + hh;
        }

        if (mm < 10) {
            time = time + ":0" + mm;
        } else {
            time = time + ":" + mm;
        }

        // Log.d(TAG, "TimeToString " + time);
        return time;
    }

    long ConverDateTimeForSort(String dt, String tm) {
        String str = dt + " " + tm;
        try {
            DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date d = f.parse(str);
            long milliseconds = d.getTime();
            return milliseconds;
        } catch (Exception e) {
            return 0;
        }


    }

    void makeBlank()
    {
        this.selectedDate = getDate();
        Calendar cal = Calendar.getInstance();
        this.hours = cal.get(Calendar.HOUR_OF_DAY);
        this.minuts = cal.get(Calendar.MINUTE);
        this.FlagForShowData=0;

        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(selectedDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cal.setTime(date);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);

        this.editTextAmmount.setText("");
        log("make blank");
        this.editTextDescription.setText("");
        this.buttonTime.setText(TimeIntToString(hours, minuts));
        this.time = TimeIntToString(hours, minuts);
        this.buttonDate.setText(selectedDate);
        this.spinnerAccount.setSelection(0);
        this.spinnerCategory.setSelection(0);
        this.spinnerCurrency.setSelection(0);

    }


/*
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);

            case TIME_DIALOG_ID:

                return new TimePickerDialog(getActivity(), mTimeSetListener, hours, minuts, true);
        }

        return super.onCreateDialog(id);
    }
*/



    void log(String msg)
    {
        Log.d("FragmentAddNewRecord",msg);
    }
}
