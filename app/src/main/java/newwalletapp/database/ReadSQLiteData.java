package newwalletapp.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 16-02-15.
 */
public class ReadSQLiteData {

    String Tag = "ReadSQLiteData";
    Context context;
    DatabaseSqlite helper;
    DataPrefrences dataPrefrences;

    public ReadSQLiteData(Context con) {
        Log.d(Tag, "ReadSQLiteData Constructor");
        this.context = con;
        SQLiteDatabase.loadLibs(con);
        helper = new DatabaseSqlite(con);
        dataPrefrences = new DataPrefrences(con);
    }

    public ArrayList<HashMap<String, String>> getAllAccountsData() {
        Log.d(Tag, "getAllAccountsData() Method");
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabAccounts, null);
        if (c.getCount() > 0) {
            Log.d(Tag, "Cursor count =" + c.getCount());
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", c.getString(0));
                Log.d(Tag, "Id =" + c.getString(0));
                map.put("acc_name", c.getString(1));
                Log.d(Tag, "Account Name =" + c.getString(1));
                map.put("acc_type", c.getString(2));
                Log.d(Tag, "Account type =" + c.getString(2));
                arrayList.add(map);
                c.moveToNext();

            }
        }
        c.close();
        Log.d(Tag, "Cursor Closed");
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> getAllCategories() {

        log("getAllCategories Method");
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabCategory, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            log("Record Count " + c.getCount());
            for (int i = 0; i < c.getCount(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", c.getString(0));
                log("id = " + c.getString(0));
                map.put("name", c.getString(1));
                log("name = " + c.getString(1));
                map.put("des", c.getString(2));
                log("des = " + c.getString(2));
                map.put("icon", c.getString(3));
                log("icon = " + c.getString(3));
                map.put("color", c.getString(4));
                log("color = " + c.getString(4));
                map.put("type", c.getString(5));
                log("type = " + c.getString(5));
                map.put("parent", c.getString(6));
                log("parent = " + c.getString(6));
                c.moveToNext();
                log("Moveto Next");
                arrayList.add(map);
            }
            c.close();
            log("Cursor Closed");
        } else {
            log("No Categories Available");
        }
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> getAllStoredCurrencies() {
        log("getAllStoredCurrencies Method");
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabCurrencies, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            log("Record Count " + c.getCount());
            for (int i = 0; i < c.getCount(); i++) {


                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", c.getString(0));
                log("id = " + c.getString(0));
                map.put("symbol", c.getString(1));
                log("symbol = " + c.getString(1));
                map.put("code", c.getString(2));
                log("code = " + c.getString(2));
                map.put("name", c.getString(3));
                log("name = " + c.getString(3));
                map.put("rate", c.getString(4));
                log("rate = " + c.getString(4));
                map.put("type", c.getString(5));
                log("type = " + c.getString(5));
                c.moveToNext();
                log("Moveto Next");
                arrayList.add(map);
            }
            c.close();
            log("Cursor Closed");

        } else {
            log("No Categories Available");
        }
        return arrayList;
    }


    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getAllRecordsByDay() {
        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");

        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();

            for (int k = 0; k < c.getCount(); k++) {

                String strDate = c.getString(0);
                arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate + "'" + sortOrder, null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate + "'" + filterString1 + sortOrder, null);
                }

                if (cursor.getCount() > 0) {
                    log(tag + "C >0");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log(tag + "Cursor at " + (i + 1) + " position");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", cursor.getString(0));
                        log("Id " + cursor.getString(0));
                        map.put("account", cursor.getString(1));
                        log("Account " + cursor.getString(1));
                        map.put("amount", cursor.getString(2));
                        log("Amount" + cursor.getString(2));
                        map.put("currency", cursor.getString(3));
                        log("Currency" + cursor.getString(3));


                        map.put("category", cursor.getString(4));
                        log("Category" + cursor.getString(4));

                        map.put("type", cursor.getString(5));
                        log("Type" + cursor.getString(5));
                        map.put("desc", cursor.getString(6));
                        log("Description" + cursor.getString(6));
                        map.put("time", cursor.getString(7));
                        log("Time" + cursor.getString(7));
                        map.put("date", cursor.getString(8));
                        log("Date" + cursor.getString(8));
                        map.put("sdate", cursor.getString(9));
                        log("Date For Sort" + cursor.getString(9));
                        map.put("currencytype", cursor.getString(13));
                        log("CurrencyType" + cursor.getString(13));
                        map.put("childamount", cursor.getString(14));
                        log("ChildAmount" + cursor.getString(14));

                        Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + cursor.getString(4) + "'", null);
                        if (cCategory.getCount() > 0) {
                            cCategory.moveToFirst();
                            map.put("icon", cCategory.getString(3));
                            log("icon =" + cCategory.getString(3));
                            map.put("color", cCategory.getString(4));
                            log("color =" + cCategory.getString(4));
                            cCategory.close();
                        }

                        String currency_code = cursor.getString(3).substring(0, 3);
                        log("Currency Code " + currency_code);
                        Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                        if (cCurrency.getCount() > 0) {
                            cCurrency.moveToFirst();
                            map.put("symbol", cCurrency.getString(1));
                            log("Symbol =" + cCurrency.getString(1));
                            cCurrency.close();
                        }

                        cursor.moveToNext();
                        arrayList.add(map);
                    }
                    log("strDate =" + strDate + " arrayList =" + arrayList);
                    mapArrayList.put(strDate, arrayList);
                    c.moveToNext();
                }

            }
        }

        return mapArrayList;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getAllRecordsByWeek() {
        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                //String strYear = c.getString(0);
                //String
                arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + sortOrder, null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1 + sortOrder, null);
                }

                if (cursor.getCount() > 0) {
                    log(tag + "C >0");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log(tag + "Cursor at " + (i + 1) + " position");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", cursor.getString(0));
                        log("Id " + cursor.getString(0));
                        map.put("account", cursor.getString(1));
                        log("Account " + cursor.getString(1));
                        map.put("amount", cursor.getString(2));
                        log("Amount" + cursor.getString(2));
                        map.put("currency", cursor.getString(3));
                        log("Currency" + cursor.getString(3));


                        map.put("category", cursor.getString(4));
                        log("Category" + cursor.getString(4));

                        map.put("type", cursor.getString(5));
                        log("Type" + cursor.getString(5));
                        map.put("desc", cursor.getString(6));
                        log("Description" + cursor.getString(6));
                        map.put("time", cursor.getString(7));
                        log("Time" + cursor.getString(7));
                        map.put("date", cursor.getString(8));
                        log("Date" + cursor.getString(8));
                        map.put("sdate", cursor.getString(9));
                        log("Date For Sort" + cursor.getString(9));
                        map.put("currencytype", cursor.getString(13));
                        log("CurrencyType" + cursor.getString(13));
                        map.put("childamount", cursor.getString(14));
                        log("ChildAmount" + cursor.getString(14));

                        Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + cursor.getString(4) + "'", null);
                        if (cCategory.getCount() > 0) {
                            cCategory.moveToFirst();
                            map.put("icon", cCategory.getString(3));
                            log("icon =" + cCategory.getString(3));
                            map.put("color", cCategory.getString(4));
                            log("color =" + cCategory.getString(4));
                            cCategory.close();
                        }

                        String currency_code = cursor.getString(3).substring(0, 3);
                        log("Currency Code " + currency_code);
                        Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                        if (cCurrency.getCount() > 0) {
                            cCurrency.moveToFirst();
                            map.put("symbol", cCurrency.getString(1));
                            log("Symbol =" + cCurrency.getString(1));
                            cCurrency.close();
                        }

                        cursor.moveToNext();
                        arrayList.add(map);
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.clear();
                    calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(c.getString(1)));
                    calendar.set(Calendar.YEAR, Integer.parseInt(c.getString(0)));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String date = sdf.format(calendar.getTime());
                    //Date date = calendar.getTime();
                    log("strDate =" + date + " arrayList =" + arrayList);
                    mapArrayList.put("" + date, arrayList);
                    c.moveToNext();
                }
            }
        }
        return mapArrayList;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getAllRecordsByMonth() {
        String tag = "getAllRecordByMonth Method ";
        log(tag + "called");

        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }


        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                //String strYear = c.getString(0);
                //String
                arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + sortOrder, null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1 + sortOrder, null);
                }

                if (cursor.getCount() > 0) {
                    log(tag + "C >0");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log(tag + "Cursor at " + (i + 1) + " position");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", cursor.getString(0));
                        log("Id " + cursor.getString(0));
                        map.put("account", cursor.getString(1));
                        log("Account " + cursor.getString(1));
                        map.put("amount", cursor.getString(2));
                        log("Amount" + cursor.getString(2));
                        map.put("currency", cursor.getString(3));
                        log("Currency" + cursor.getString(3));


                        map.put("category", cursor.getString(4));
                        log("Category" + cursor.getString(4));

                        map.put("type", cursor.getString(5));
                        log("Type" + cursor.getString(5));
                        map.put("desc", cursor.getString(6));
                        log("Description" + cursor.getString(6));
                        map.put("time", cursor.getString(7));
                        log("Time" + cursor.getString(7));
                        map.put("date", cursor.getString(8));
                        log("Date" + cursor.getString(8));
                        map.put("sdate", cursor.getString(9));
                        log("Date For Sort" + cursor.getString(9));
                        map.put("currencytype", cursor.getString(13));
                        log("CurrencyType" + cursor.getString(13));
                        map.put("childamount", cursor.getString(14));
                        log("ChildAmount" + cursor.getString(14));


                        Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + cursor.getString(4) + "'", null);
                        if (cCategory.getCount() > 0) {
                            cCategory.moveToFirst();
                            map.put("icon", cCategory.getString(3));
                            log("icon =" + cCategory.getString(3));
                            map.put("color", cCategory.getString(4));
                            log("color =" + cCategory.getString(4));
                            cCategory.close();
                        }

                        String currency_code = cursor.getString(3).substring(0, 3);
                        log("Currency Code " + currency_code);
                        Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                        if (cCurrency.getCount() > 0) {
                            cCurrency.moveToFirst();
                            map.put("symbol", cCurrency.getString(1));
                            log("Symbol =" + cCurrency.getString(1));
                            cCurrency.close();
                        }

                        cursor.moveToNext();
                        arrayList.add(map);
                    }

                    log("strDate =" + c.getString(1) + ", " + c.getString(0) + " arrayList =" + arrayList);
                    mapArrayList.put(c.getString(1) + ", " + c.getString(0), arrayList);
                    c.moveToNext();
                }
            }
        }
        return mapArrayList;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getAllRecordsByYear() {
        String tag = "getAllRecordByMonth Method ";
        log(tag + "called");
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        Cursor c;
        if (!filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + filterString + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                //String strYear = c.getString(0);
                //String
                arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (!filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1 + sortOrder, null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'" + sortOrder, null);
                }

                if (cursor.getCount() > 0) {
                    log(tag + "C >0");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log(tag + "Cursor at " + (i + 1) + " position");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", cursor.getString(0));
                        log("Id " + cursor.getString(0));
                        map.put("account", cursor.getString(1));
                        log("Account " + cursor.getString(1));
                        map.put("amount", cursor.getString(2));
                        log("Amount" + cursor.getString(2));
                        map.put("currency", cursor.getString(3));
                        log("Currency" + cursor.getString(3));


                        map.put("category", cursor.getString(4));
                        log("Category" + cursor.getString(4));

                        map.put("type", cursor.getString(5));
                        log("Type" + cursor.getString(5));
                        map.put("desc", cursor.getString(6));
                        log("Description" + cursor.getString(6));
                        map.put("time", cursor.getString(7));
                        log("Time" + cursor.getString(7));
                        map.put("date", cursor.getString(8));
                        log("Date" + cursor.getString(8));
                        map.put("sdate", cursor.getString(9));
                        log("Date For Sort" + cursor.getString(9));
                        map.put("currencytype", cursor.getString(13));
                        log("CurrencyType" + cursor.getString(13));
                        map.put("childamount", cursor.getString(14));
                        log("ChildAmount" + cursor.getString(14));


                        Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + cursor.getString(4) + "'", null);
                        if (cCategory.getCount() > 0) {
                            cCategory.moveToFirst();
                            map.put("icon", cCategory.getString(3));
                            log("icon =" + cCategory.getString(3));
                            map.put("color", cCategory.getString(4));
                            log("color =" + cCategory.getString(4));
                            cCategory.close();
                        }

                        String currency_code = cursor.getString(3).substring(0, 3);
                        log("Currency Code " + currency_code);
                        Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                        if (cCurrency.getCount() > 0) {
                            cCurrency.moveToFirst();
                            map.put("symbol", cCurrency.getString(1));
                            log("Symbol =" + cCurrency.getString(1));
                            cCurrency.close();
                        }

                        cursor.moveToNext();
                        arrayList.add(map);
                    }

                    log("strDate =" + c.getString(0) + " arrayList =" + arrayList);
                    mapArrayList.put(c.getString(0), arrayList);
                    c.moveToNext();
                }
            }
        }
        return mapArrayList;
    }

    public ArrayList<HashMap<String, String>> getAllRecords() {

        String tag = "getAllRecordByDay Method ";
        String filterCategory, filterAccount, sortOrder, filterString = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "'";
        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
            }

        }


        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        log(tag + "called");

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + sortOrder, null);
            log("Filter String =" + filterString);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + filterString + sortOrder, null);
            log("Filter String =" + filterString);
        }


        log(tag + "Cursor Count =" + c.getCount());


        if (c.getCount() > 0) {
            log(tag + "C >0");
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                log(tag + "Cursor at " + (i + 1) + " position");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", c.getString(0));
                log("Id " + c.getString(0));
                map.put("account", c.getString(1));
                log("Account " + c.getString(1));
                map.put("amount", c.getString(2));
                log("Amount" + c.getString(2));
                map.put("currency", c.getString(3));
                log("Currency" + c.getString(3));


                map.put("category", c.getString(4));
                log("Category" + c.getString(4));

                map.put("type", c.getString(5));
                log("Type" + c.getString(5));
                map.put("desc", c.getString(6));
                log("Description" + c.getString(6));
                map.put("time", c.getString(7));
                log("Time" + c.getString(7));
                map.put("date", c.getString(8));
                log("Date" + c.getString(8));
                map.put("sdate", c.getString(9));
                log("Date For Sort" + c.getString(9));

                map.put("currencytype", c.getString(13));
                log("CurrencyType" + c.getString(13));
                map.put("childamount", c.getString(14));
                log("ChildAmount" + c.getString(14));


                Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + c.getString(4) + "'", null);
                if (cCategory.getCount() > 0) {
                    cCategory.moveToFirst();
                    map.put("icon", cCategory.getString(3));
                    log("icon =" + cCategory.getString(3));
                    map.put("color", cCategory.getString(4));
                    log("color =" + cCategory.getString(4));
                    cCategory.close();
                }

                String currency_code = c.getString(3).substring(0, 3);
                log("Currency Code " + currency_code);
                Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                if (cCurrency.getCount() > 0) {
                    cCurrency.moveToFirst();
                    map.put("symbol", cCurrency.getString(1));
                    log("Symbol =" + cCurrency.getString(1));
                    cCurrency.close();
                }

                c.moveToNext();
                arrayList.add(map);
            }
        }


        return arrayList;
    }

    public int getAllIncomeRecordCount() {

        log("getAllIncomeCount() Method");
        int count;
        String filterCategory, filterAccount, filterString = "";

        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " and " + helper.rec_account + "='" + filterAccount + "' ";
        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("expense")) {

            return 0;

        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income'", null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' " + filterString, null);
        }

        count = c.getCount();
        log("Income Record Count =" + c.getCount());
        c.close();

        return count;
    }

    public int getAllExpenseRecordCount() {

        log("getAllExpenseCount() Method");
        int count;
        String filterCategory, filterAccount, filterString = "";

        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " and " + helper.rec_account + "='" + filterAccount + "' ";
        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {

            return 0;

        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense'", null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense'" + filterString, null);
        }

        count = c.getCount();
        log("Expense Record Count =" + c.getCount());
        c.close();

        return count;
    }

    public double getTotalOfAllIncome() {
        log("getTotalOfAllIncome() Method");
        Double income = 0.0;
        String filterCategory, filterAccount, filterString = "";

        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " and " + helper.rec_account + "='" + filterAccount + "' ";
        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("expense")) {

            return 0;

        }
        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_ammount + ", " + helper.rec_currency + " from " + helper.TabRecords + " where " + helper.rec_type + "='income'", null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_ammount + ", " + helper.rec_currency + " from " + helper.TabRecords + " where " + helper.rec_type + "='income'" + filterString, null);
        }

        if (c.getCount() > 0) {
            log("AllIncome record count " + c.getCount());
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                income = income + Double.parseDouble(c.getString(0));

                c.moveToNext();
            }
        }
        log("Income Total =" + income);
        return income;
    }

    public double getTotalOfAllIncome(String account) {
        log("getTotalOfAllIncome() Method");

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_account + "='" + account + "' and " + helper.rec_type + "='income'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            if (c.getString(0).equals(null)) {
                log("Income For Account " + account + " is 0.0");
                return 0.0;

            } else {
                log("Income For Account " + account + " is " + c.getString(0));


                return Double.parseDouble(c.getString(0));

            }
        } else {
            log("Income For Account " + account + " is 0.0");
            return 0.0;
        }


    }

    public double getTotalOfAllExpense() {
        log("getTotalOfAllExpense() Method");

        Double expense = 0.0;
        String filterCategory, filterAccount, filterString = "";

        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " and " + helper.rec_account + "='" + filterAccount + "' ";
        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {

            return 0;

        }
        Cursor c;
        if (filterString.equals("")) {

            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_ammount + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense'", null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_ammount + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense'" + filterString, null);
        }
        if (c.getCount() > 0) {
            log("AllExpense record count " + c.getCount());
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                expense = expense + Double.parseDouble(c.getString(0));
                c.moveToNext();
            }
        }
        log("Expence Total =" + expense);
        return expense;
    }

    public double getTotalOfAllExpense(String account) {
        log("getTotalOfAllExpense() Method");

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_account + "='" + account + "' and " + helper.rec_type + "='expense'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            if (c.getString(0).equals(null)) {
                log("Expense For Account " + account + " is 0.0");
                return 0.0;

            } else {
                log("Expense For Account " + account + " is " + c.getString(0));

                return Double.parseDouble(c.getString(0));
            }
        } else {
            log("Expense For Account " + account + " is 0.0");
            return 0.0;
        }
    }

    public int getDayCountForAllRecords() {
        log("getDayCountForAllRecords Method");

        int dayCount = 0;
        String minDate = "", maxDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Cursor cmin = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_date + ", Min(" + helper.rec_date_for_sort + ") from " + helper.TabRecords, null);
        if (cmin.getCount() > 0) {
            log("Record Count Min Date" + cmin.getCount());
            cmin.moveToFirst();
            log("Min Date =" + cmin.getString(0));
            minDate = cmin.getString(0);
            cmin.close();
        }
        Cursor cmax = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_date + ", Max(" + helper.rec_date_for_sort + ") from " + helper.TabRecords, null);
        if (cmax.getCount() > 0) {
            log("Record Count For Max Date" + cmax.getCount());
            cmax.moveToFirst();
            log("Max Date =" + cmax.getString(0));
            maxDate = cmax.getString(0);
            cmax.close();
        }

        if (minDate == null || maxDate == null) {
            return 0;
        }
        if (!minDate.equals(maxDate)) {

            try {
                Date date1 = sdf.parse(minDate);
                Date date2 = sdf.parse(maxDate);
                long diff = date2.getTime() - date1.getTime();
                System.out.println("Days: " + (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1));
                log("Days Count = " + (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1));
                return (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1);
            } catch (ParseException e) {

                e.printStackTrace();
                log("Days Count Error " + e);
                return 0;
            }

        } else {
            log("Days Count Max=Min Return 1");
            return 1;
        }


    }

    public int getAllRecordCount() {
        int count = 0;
        log("getAllRecordCount Method");
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabRecords, null);
        log("Record Count =" + c.getCount());
        count = c.getCount();
        return count;

    }


    public String getCurrencySymbol() {
        log("getCurrencySymbol Method");
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.cur_symbol + " from " + helper.TabCurrencies + " where " + helper.cur_type + "='master'", null);
        if (c.getCount() > 0) {

            c.moveToFirst();
            log("Currency Symbol =" + c.getString(0));
            return c.getString(0);
        } else {
            log("Return Null");
            return "";
        }


    }

    public ArrayList<HashMap<String, String>> getCategoriesFromRecords() {
        log("getCategoriesFromRecords Method");
        ArrayList<HashMap<String, String>> arrayMap = new ArrayList<HashMap<String, String>>();

        ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<String> amountList = new ArrayList<String>();

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " GROUP BY " + helper.rec_category, null);
        // "SUM(IIF("+helper.rec_type+"='expense',(-1*"helper.rec_ammount+"),"+helper.rec_ammount+"))";
        // Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ", SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)   from " + helper.TabRecords + " GROUP BY " + helper.rec_category, null);

        if (c.getCount() > 0) {
            log("c.getCount()>0 with DISTINCT");
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                log("DISTINCT category =" + c.getString(0));
                categoryList.add(c.getString(0));
                log("Category = " + c.getString(0) + " Total Amount =" + c.getString(1));
                amountList.add(c.getString(1));
                c.moveToNext();

            }
        }


        if (categoryList.size() > 0) {
            log("categoryList.size() > 0");
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabCategory, null);
            if (c.getCount() > 0) {

                c.moveToFirst();
                log("select all categories, record count =" + c.getCount());
                for (int i = 0; i < c.getCount(); i++) {


                    for (int k = 0; k < categoryList.size(); k++) {
                        log("Categori List item =" + categoryList.get(k));
                    }

                    for (int j = 0; j < categoryList.size(); j++) {
                        log("Check j=" + j);

                        if (categoryList.get(j).equals(c.getString(1))) {
                            log("categoryList.get(j).equals(c.getString(1))");

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("category", c.getString(1));
                            log("category =" + c.getString(1));
                            map.put("color", c.getString(4));
                            log("color =" + c.getString(4));
                            map.put("amount", amountList.get(j));
                            log("amount =" + amountList.get(j));

                            arrayMap.add(map);
                            break;

                        }
                    }

                    c.moveToNext();

                }
            }


        }
        log("arrayMap =" + arrayMap);

        return arrayMap;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getCategoriesFromRecordsDay() {
        log("getCategoriesFromRecordsDay Method");
        String tag = "getCategoriesFromRecordsDay";
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayListFinal = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();


        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");
        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        log(tag + "called");
        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();

            for (int k = 0; k < c.getCount(); k++) {
                ArrayList<String> categoryList = new ArrayList<String>();
                ArrayList<String> amountList = new ArrayList<String>();
                String strDate = c.getString(0);
                ArrayList<HashMap<String, String>> arrayMap = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate + "'" + " GROUP BY " + helper.rec_category + sortOrder, null);
                    // cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate +"'"+ " GROUP BY " + helper.rec_category+sortOrder, null);

                } else {
                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate +"'"+filterString1+ " GROUP BY " + helper.rec_category+sortOrder, null);
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_date + "='" + strDate + "'" + filterString1 + " GROUP BY " + helper.rec_category + sortOrder, null);
                }
                if (cursor.getCount() > 0) {
                    log("c.getCount()>0 with DISTINCT");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log("DISTINCT category =" + cursor.getString(0));
                        categoryList.add(cursor.getString(0));
                        log("Category = " + cursor.getString(0) + " Total Amount =" + cursor.getString(1));
                        amountList.add(cursor.getString(1));
                        cursor.moveToNext();

                    }
                }
                if (categoryList.size() > 0) {
                    log("categoryList.size() > 0");
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabCategory, null);
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        log("select all categories, record count =" + cursor.getCount());
                        for (int i = 0; i < cursor.getCount(); i++) {


                            for (int z = 0; z < categoryList.size(); z++) {
                                log("Categori List item =" + categoryList.get(z));
                            }

                            for (int j = 0; j < categoryList.size(); j++) {
                                log("Check j=" + j);

                                if (categoryList.get(j).equals(cursor.getString(1))) {
                                    log("categoryList.get(j).equals(c.getString(1))");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("category", cursor.getString(1));
                                    log("category =" + cursor.getString(1));
                                    map.put("color", cursor.getString(4));
                                    log("color =" + cursor.getString(4));
                                    map.put("amount", amountList.get(j));
                                    log("amount =" + amountList.get(j));
                                    arrayMap.add(map);
                                    break;
                                }
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                mapArrayListFinal.put(strDate, arrayMap);
                c.moveToNext();
            }
        }
        log("mapArrayListFinal =" + mapArrayListFinal);
        return mapArrayListFinal;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getCategoriesFromRecordsWeek() {
        log("getCategoriesFromRecordsWeek Method");
        String tag = "getCategoriesFromRecordsWeek";
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayListFinal = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();


        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");
        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        log(tag + "called");
        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();

            for (int k = 0; k < c.getCount(); k++) {
                ArrayList<String> categoryList = new ArrayList<String>();
                ArrayList<String> amountList = new ArrayList<String>();

                ArrayList<HashMap<String, String>> arrayMap = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {

                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + " GROUP BY " + helper.rec_category + sortOrder, null);
                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and "+helper.rec_week+"='"+c.getString(1)+"'"+ " GROUP BY " + helper.rec_category+sortOrder, null);
                } else {

                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and "+helper.rec_week+"='"+c.getString(1)+ "'"+filterString1+ " GROUP BY " + helper.rec_category+sortOrder, null);
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1 + " GROUP BY " + helper.rec_category + sortOrder, null);
                }
                if (cursor.getCount() > 0) {
                    log("c.getCount()>0 with DISTINCT");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log("DISTINCT category =" + cursor.getString(0));
                        categoryList.add(cursor.getString(0));
                        log("Category = " + cursor.getString(0) + " Total Amount =" + cursor.getString(1));
                        amountList.add(cursor.getString(1));
                        cursor.moveToNext();

                    }
                }
                if (categoryList.size() > 0) {
                    log("categoryList.size() > 0");
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabCategory, null);
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        log("select all categories, record count =" + cursor.getCount());
                        for (int i = 0; i < cursor.getCount(); i++) {


                            for (int z = 0; z < categoryList.size(); z++) {
                                log("Categori List item =" + categoryList.get(z));
                            }

                            for (int j = 0; j < categoryList.size(); j++) {
                                log("Check j=" + j);

                                if (categoryList.get(j).equals(cursor.getString(1))) {
                                    log("categoryList.get(j).equals(c.getString(1))");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("category", cursor.getString(1));
                                    log("category =" + cursor.getString(1));
                                    map.put("color", cursor.getString(4));
                                    log("color =" + cursor.getString(4));
                                    map.put("amount", amountList.get(j));
                                    log("amount =" + amountList.get(j));
                                    arrayMap.add(map);
                                    break;
                                }
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(c.getString(1)));
                calendar.set(Calendar.YEAR, Integer.parseInt(c.getString(0)));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf.format(calendar.getTime());
                //Date date = calendar.getTime();
                log("strDate =" + date + " arrayList =" + arrayMap);
                mapArrayListFinal.put("" + date, arrayMap);
                c.moveToNext();
            }
        }
        log("mapArrayListFinal =" + mapArrayListFinal);
        return mapArrayListFinal;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getCategoriesFromRecordsMonth() {
        log("getCategoriesFromRecordsMonth Method");
        String tag = "getCategoriesFromRecordsMonth";
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayListFinal = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();


        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");
        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        log(tag + "called");
        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();

            for (int k = 0; k < c.getCount(); k++) {
                ArrayList<String> categoryList = new ArrayList<String>();
                ArrayList<String> amountList = new ArrayList<String>();

                ArrayList<HashMap<String, String>> arrayMap = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + " GROUP BY " + helper.rec_category + sortOrder, null);
                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and "+helper.rec_month+"='"+c.getString(1)+"'"+ " GROUP BY " + helper.rec_category+sortOrder, null);
                } else {

                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and "+helper.rec_month+"='"+c.getString(1)+ "'"+filterString1+ " GROUP BY " + helper.rec_category+sortOrder, null);
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1 + " GROUP BY " + helper.rec_category + sortOrder, null);
                }
                if (cursor.getCount() > 0) {
                    log("c.getCount()>0 with DISTINCT");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log("DISTINCT category =" + cursor.getString(0));
                        categoryList.add(cursor.getString(0));
                        log("Category = " + cursor.getString(0) + " Total Amount =" + cursor.getString(1));
                        amountList.add(cursor.getString(1));
                        cursor.moveToNext();

                    }
                }
                if (categoryList.size() > 0) {
                    log("categoryList.size() > 0");
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabCategory, null);
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        log("select all categories, record count =" + cursor.getCount());
                        for (int i = 0; i < cursor.getCount(); i++) {


                            for (int z = 0; z < categoryList.size(); z++) {
                                log("Categori List item =" + categoryList.get(z));
                            }

                            for (int j = 0; j < categoryList.size(); j++) {
                                log("Check j=" + j);

                                if (categoryList.get(j).equals(cursor.getString(1))) {
                                    log("categoryList.get(j).equals(c.getString(1))");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("category", cursor.getString(1));
                                    log("category =" + cursor.getString(1));
                                    map.put("color", cursor.getString(4));
                                    log("color =" + cursor.getString(4));
                                    map.put("amount", amountList.get(j));
                                    log("amount =" + amountList.get(j));
                                    arrayMap.add(map);
                                    break;
                                }
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                log("strDate =" + c.getString(1) + ", " + c.getString(0) + " arrayList =" + arrayMap);
                mapArrayListFinal.put(c.getString(1) + ", " + c.getString(0), arrayMap);

                c.moveToNext();
            }
        }
        log("mapArrayListFinal =" + mapArrayListFinal);
        return mapArrayListFinal;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getCategoriesFromRecordsYear() {
        log("getCategoriesFromRecordsDay Year");
        String tag = "getCategoriesFromRecordsYear";
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayListFinal = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();


        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");
        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        log(tag + "called");
        Cursor c;
        if (filterString.equals("")) {

            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + filterString + sortOrder, null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();

            for (int k = 0; k < c.getCount(); k++) {
                ArrayList<String> categoryList = new ArrayList<String>();
                ArrayList<String> amountList = new ArrayList<String>();

                ArrayList<HashMap<String, String>> arrayMap = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {

                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'"+ " GROUP BY " + helper.rec_category+sortOrder, null);
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'" + " GROUP BY " + helper.rec_category + sortOrder, null);
                } else {

                    //cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(CASE WHEN "+helper.rec_type+"='expense' THEN -1*("+helper.rec_ammount+") ELSE "+helper.rec_ammount+" END)  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'"+filterString1+ " GROUP BY " + helper.rec_category+sortOrder, null);
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select DISTINCT " + helper.rec_category + ",SUM(" + helper.rec_ammount + ")  from " + helper.TabRecords + " where " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1 + " GROUP BY " + helper.rec_category + sortOrder, null);
                }
                if (cursor.getCount() > 0) {
                    log("c.getCount()>0 with DISTINCT");
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        log("DISTINCT category =" + cursor.getString(0));
                        categoryList.add(cursor.getString(0));
                        log("Category = " + cursor.getString(0) + " Total Amount =" + cursor.getString(1));
                        amountList.add(cursor.getString(1));
                        cursor.moveToNext();

                    }
                }
                if (categoryList.size() > 0) {
                    log("categoryList.size() > 0");
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabCategory, null);
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        log("select all categories, record count =" + cursor.getCount());
                        for (int i = 0; i < cursor.getCount(); i++) {


                            for (int z = 0; z < categoryList.size(); z++) {
                                log("Categori List item =" + categoryList.get(z));
                            }

                            for (int j = 0; j < categoryList.size(); j++) {
                                log("Check j=" + j);

                                if (categoryList.get(j).equals(cursor.getString(1))) {
                                    log("categoryList.get(j).equals(c.getString(1))");

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("category", cursor.getString(1));
                                    log("category =" + cursor.getString(1));
                                    map.put("color", cursor.getString(4));
                                    log("color =" + cursor.getString(4));
                                    map.put("amount", amountList.get(j));
                                    log("amount =" + amountList.get(j));
                                    arrayMap.add(map);
                                    break;
                                }
                            }
                            cursor.moveToNext();
                        }
                    }
                }
                log("strDate =" + c.getString(0) + " arrayList =" + arrayMap);
                mapArrayListFinal.put(c.getString(0), arrayMap);

                c.moveToNext();
            }
        }
        log("mapArrayListFinal =" + mapArrayListFinal);
        return mapArrayListFinal;
    }

    public ArrayList<HashMap<String, String>> getTopTenRecords(String acc_name) {

        String tag = "getTopTenRecords";
        ArrayList<HashMap<String, String>> arrayListMap = new ArrayList<HashMap<String, String>>();
        Cursor c;
        if (acc_name == null) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " order by " + helper.id + " desc LIMIT 10", null);

        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select *from " + helper.TabRecords + " where " + helper.rec_account + "='" + acc_name + "' order by " + helper.id + " desc LIMIT 10", null);
        }

        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                log(tag + "Cursor at " + (i + 1) + " position");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", c.getString(0));
                log("Id " + c.getString(0));
                map.put("account", c.getString(1));
                log("Account " + c.getString(1));
                map.put("amount", c.getString(2));
                log("Amount" + c.getString(2));
                map.put("currency", c.getString(3));
                log("Currency" + c.getString(3));


                map.put("category", c.getString(4));
                log("Category" + c.getString(4));

                map.put("type", c.getString(5));
                log("Type" + c.getString(5));
                map.put("desc", c.getString(6));
                log("Description" + c.getString(6));
                map.put("time", c.getString(7));
                log("Time" + c.getString(7));
                map.put("date", c.getString(8));
                log("Date" + c.getString(8));
                map.put("sdate", c.getString(9));
                log("Date For Sort" + c.getString(9));
                map.put("currencytype", c.getString(13));
                log("CurrencyType" + c.getString(13));
                map.put("childamount", c.getString(14));
                log("ChildAmount" + c.getString(14));

                Cursor cCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCategory + " where " + helper.cat_name + "='" + c.getString(4) + "'", null);
                if (cCategory.getCount() > 0) {
                    cCategory.moveToFirst();
                    map.put("icon", cCategory.getString(3));
                    log("icon =" + cCategory.getString(3));
                    map.put("color", cCategory.getString(4));
                    log("color =" + cCategory.getString(4));
                    cCategory.close();
                }

                String currency_code = c.getString(3).substring(0, 3);
                log("Currency Code " + currency_code);
                Cursor cCurrency = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select * from " + helper.TabCurrencies + " where " + helper.cur_code + "='" + currency_code + "'", null);
                if (cCurrency.getCount() > 0) {
                    cCurrency.moveToFirst();
                    map.put("symbol", cCurrency.getString(1));
                    log("Symbol =" + cCurrency.getString(1));
                    cCurrency.close();
                }

                c.moveToNext();
                arrayListMap.add(map);


            }

        }
        return arrayListMap;
    }

    public LinkedHashMap<String, HashMap<String, String>> getReportForDaily() {

        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        //LinkedHashMap<String,ArrayList<HashMap<String,String>>> mapArrayList=new LinkedHashMap<String,ArrayList<HashMap<String,String>>>();
        LinkedHashMap<String, HashMap<String, String>> mapArrayList = new LinkedHashMap<String, HashMap<String, String>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_date + " from " + helper.TabRecords + filterString + sortOrder, null);
        }
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                String strDate = c.getString(0);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("daycount", 1 + "");
                //arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_date + "='" + strDate + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_date + "='" + strDate + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    map.put("incomerecordcount", cursor.getCount() + "");
                } else {
                    map.put("incomerecordcount", "" + 0);
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_date + "='" + strDate + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_date + "='" + strDate + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    map.put("expenserecordcount", cursor.getCount() + "");
                } else {
                    map.put("expenserecordcount", 0 + "");
                }
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_date + "='" + strDate + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_date + "='" + strDate + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalincome =" + cursor.getString(0));
                    map.put("totalincome", cursor.getString(0));
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_date + "='" + strDate + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_date + "='" + strDate + "'" + filterString1, null);
                }

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalexpense " + cursor.getString(0));
                    map.put("totalexpense", cursor.getString(0));

                }

                //arrayList.add(map);
                mapArrayList.put(strDate, map);
                c.moveToNext();
            }
        }


        return mapArrayList;
    }

    public LinkedHashMap<String, HashMap<String, String>> getReportForWeekly() {

        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        //LinkedHashMap<String,ArrayList<HashMap<String,String>>> mapArrayList=new LinkedHashMap<String,ArrayList<HashMap<String,String>>>();
        LinkedHashMap<String, HashMap<String, String>> mapArrayList = new LinkedHashMap<String, HashMap<String, String>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + filterString + sortOrder, null);
        }
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                // String strDate = c.getString(0);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("daycount", 7 + "");
                //arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    map.put("incomerecordcount", cursor.getCount() + "");
                } else {
                    map.put("incomerecordcount", "" + 0);
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    map.put("expenserecordcount", cursor.getCount() + "");
                } else {
                    map.put("expenserecordcount", 0 + "");
                }
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalincome =" + cursor.getString(0));
                    map.put("totalincome", cursor.getString(0));
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "'" + filterString1, null);
                }

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalexpense " + cursor.getString(0));
                    map.put("totalexpense", cursor.getString(0));

                }

                //arrayList.add(map);
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(c.getString(1)));
                calendar.set(Calendar.YEAR, Integer.parseInt(c.getString(0)));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf.format(calendar.getTime());

                mapArrayList.put(date, map);
                c.moveToNext();
            }
        }


        return mapArrayList;
    }

    public LinkedHashMap<String, HashMap<String, String>> getReportForMonthly() {

        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        //LinkedHashMap<String,ArrayList<HashMap<String,String>>> mapArrayList=new LinkedHashMap<String,ArrayList<HashMap<String,String>>>();
        LinkedHashMap<String, HashMap<String, String>> mapArrayList = new LinkedHashMap<String, HashMap<String, String>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }

        Cursor c;
        if (filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + filterString + sortOrder, null);
        }
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                // String strDate = c.getString(0);
                HashMap<String, String> map = new HashMap<String, String>();
                //map.put("daycount", 1 + "");
                //arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    map.put("incomerecordcount", cursor.getCount() + "");
                } else {
                    map.put("incomerecordcount", "" + 0);
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    map.put("expenserecordcount", cursor.getCount() + "");
                } else {
                    map.put("expenserecordcount", 0 + "");
                }
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalincome =" + cursor.getString(0));
                    map.put("totalincome", cursor.getString(0));
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "'" + filterString1, null);
                }

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalexpense " + cursor.getString(0));
                    map.put("totalexpense", cursor.getString(0));

                }
                int year = Integer.parseInt(c.getString(0));
                String month = c.getString(1);

                Date date = null;
                try {
                    date = new SimpleDateFormat("MMMM").parse(month);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int monthNumber = cal.get(Calendar.MONTH);


                map.put("daycount", "" + getMaxDaysInMonth(monthNumber, year));
                log("daycount = " + getMaxDaysInMonth(monthNumber, year));
                //arrayList.adaycount = dd(map);
                mapArrayList.put(c.getString(1) + ", " + c.getString(0), map);
                c.moveToNext();
            }
        }


        return mapArrayList;
    }

    public LinkedHashMap<String, HashMap<String, String>> getReportForYearly() {

        String tag = "getAllRecordByDay Method ";
        log(tag + "called");

        //LinkedHashMap<String,ArrayList<HashMap<String,String>>> mapArrayList=new LinkedHashMap<String,ArrayList<HashMap<String,String>>>();
        LinkedHashMap<String, HashMap<String, String>> mapArrayList = new LinkedHashMap<String, HashMap<String, String>>();
        ArrayList<HashMap<String, String>> arrayList;
        String filterCategory, filterAccount, sortOrder, filterString = "", filterString1 = "";
        sortOrder = dataPrefrences.GetStoredPrefrence("filter_sort");
        filterAccount = dataPrefrences.GetStoredPrefrence("filter_account");

        if (!filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' ";
            filterString1 = " and " + helper.rec_account + "='" + filterAccount + "' ";

        }

        filterCategory = dataPrefrences.GetStoredPrefrence("filter_category");


        if (filterCategory.equals("income")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='income' ";
                filterString1 = " and " + helper.rec_type + "='income' ";

            } else {
                filterString = filterString + " and " + helper.rec_type + "='income' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='income'";
            }

        } else if (filterCategory.equals("expense")) {
            if (filterString.equals("")) {
                filterString = " where " + helper.rec_type + "='expense' ";
                filterString1 = " and " + helper.rec_type + "='expense'";
            } else {
                filterString = filterString + " and " + helper.rec_type + "='expense' ";
                filterString1 = filterString1 + " and " + helper.rec_type + "='expense'";
            }

        }

        if (sortOrder.equals("N/A")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " DESC ";
        } else if (sortOrder.equals("old")) {
            sortOrder = " ORDER BY " + helper.rec_date_for_sort + " ASC ";
        } else if (sortOrder.equals("high")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) DESC ";
        } else if (sortOrder.equals("low")) {
            sortOrder = " ORDER BY CAST(" + helper.rec_ammount + " as double) ASC ";
        }
        Cursor c;
        if (!filterString.equals("")) {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + filterString + sortOrder, null);
        } else {
            c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + sortOrder, null);
        }
        if (c.getCount() > 0) {
            c.moveToFirst();
            for (int k = 0; k < c.getCount(); k++) {

                // String strDate = c.getString(0);
                HashMap<String, String> map = new HashMap<String, String>();
                //map.put("daycount", 1 + "");
                //arrayList = new ArrayList<HashMap<String, String>>();
                Cursor cursor;
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    map.put("incomerecordcount", cursor.getCount() + "");
                } else {
                    map.put("incomerecordcount", "" + 0);
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.rec_type + " from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    map.put("expenserecordcount", cursor.getCount() + "");
                } else {
                    map.put("expenserecordcount", 0 + "");
                }
                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='income' and " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1, null);
                }
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalincome =" + cursor.getString(0));
                    map.put("totalincome", cursor.getString(0));
                }

                if (filterString1.equals("")) {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "'", null);
                } else {
                    cursor = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "'" + filterString1, null);
                }

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    log("totalexpense " + cursor.getString(0));
                    map.put("totalexpense", cursor.getString(0));

                }
                int year = Integer.parseInt(c.getString(0));
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year); //setting the calendar year
                int noOfDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

                map.put("daycount", "" + noOfDays);
                log("daycount = " + noOfDays);
                //arrayList.adaycount = dd(map);
                mapArrayList.put(c.getString(0), map);
                c.moveToNext();
            }
        }


        return mapArrayList;
    }

    public static int getMaxDaysInMonth(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, month + 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);

        return cal.get(Calendar.DATE);
    }

    int getDayCountFromMonth(String month, String yearOfMonth) {
        Date date = null;
        try {
            date = new SimpleDateFormat("MMMM").parse(month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = Integer.parseInt(yearOfMonth);
        int monthNumber = cal.get(Calendar.MONTH);
        int numberOfDays;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, monthNumber + 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        log("Number Of days For " + month + " =" + calendar.get(Calendar.DATE));
        return cal.get(Calendar.DATE);
/*
        switch (monthNumber) {
            case 2: // February
                numberOfDays = 28;
                if (year % 4 == 0) {
                    numberOfDays = 29;
                    if (year % 100 == 0 && year % 400 != 0)
                        numberOfDays = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numberOfDays = 30;
                break;
            default: numberOfDays = 31;
                break;
        }
*/

        //return numberOfDays;

    }


    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getWeeklyBudget() {
        String tag = "getWeeklyBudget Method ";
        log(tag + "called");
        String filterCategory, filterAccount, sortOrder, filterString = "", filterStringCategoryName = " and ";
        filterAccount = dataPrefrences.GetStoredPrefrence("budget_account_filter");
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        if (filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_type + "='expense' ";
        } else {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' and " + helper.rec_type + "='expense' ";
        }


        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_week + " from " + helper.TabRecords + filterString, null);


        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                arrayList = new ArrayList<HashMap<String, String>>();

                Cursor cursorBudgetName;
                if (!filterAccount.equals("N/A")) {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_account + "='" + filterAccount + "' and " + helper.bud_interval + "='Week'", null);
                } else {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_interval + "='Week'", null);
                }

                if (cursorBudgetName.getCount() > 0) {

                    for (int j = 0; j < cursorBudgetName.getCount(); j++) {
                        double total_amount = 0;
                        cursorBudgetName.moveToPosition(j);
                        HashMap<String, String> budgetMap = new HashMap<String, String>();
                        budgetMap.put("bud_name", cursorBudgetName.getString(0));
                        log(tag + "bud_name =" + cursorBudgetName.getString(0));
                        budgetMap.put("bud_amount", cursorBudgetName.getString(1));
                        log(tag + "bud_amount =" + cursorBudgetName.getString(1));
                        budgetMap.put("bud_account", cursorBudgetName.getString(2));
                        budgetMap.put("bud_id", cursorBudgetName.getString(3));
                        //Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + filterAccount + "'", null);
                        Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + cursorBudgetName.getString(2) + "'", null);
                        if (CategoryName.getCount() > 0) {
                            for (int k = 0; k < CategoryName.getCount(); k++) {
                                CategoryName.moveToPosition(k);
                                log("CategoryName cursor at position =" + k);
                                log("CategoryName = " + CategoryName.getString(0));
                                Cursor cursorTotal;

                                if (!filterAccount.equals("N/A")) {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "' and " + helper.rec_account + "='" + filterAccount + "'", null);
                                } else {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_week + "='" + c.getString(1) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "'", null);
                                }

                                if (cursorTotal.getCount() > 0) {

                                    cursorTotal.moveToFirst();
                                    log("CursorTotal Total=" + Double.parseDouble(cursorTotal.getString(0)));
                                    total_amount = total_amount + Double.parseDouble(cursorTotal.getString(0));
                                }

                            }
                        }
                        budgetMap.put("interval", "Week");
                        budgetMap.put("expense_amount", "" + total_amount);
                        budgetMap.put("remain_amount", "" + (Double.parseDouble(cursorBudgetName.getString(1)) - total_amount));
                        arrayList.add(budgetMap);
                    }
                }
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(c.getString(1)));
                calendar.set(Calendar.YEAR, Integer.parseInt(c.getString(0)));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf.format(calendar.getTime());
                //Date date = calendar.getTime();
                log("strDate =" + date + " arrayList =" + arrayList);
                if (arrayList != null) {
                    mapArrayList.put("" + date, arrayList);
                }

            }


        }


        return mapArrayList;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getMonthlyBudget() {
        String tag = "getMonthlyBudget Method ";
        log(tag + "called");
        String filterCategory, filterAccount, sortOrder, filterString = "", filterStringCategoryName = " and ";
        filterAccount = dataPrefrences.GetStoredPrefrence("budget_account_filter");
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        if (filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_type + "='expense' ";
        } else {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' and " + helper.rec_type + "='expense' ";
        }


        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + ", " + helper.rec_month + " from " + helper.TabRecords + filterString, null);


        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                arrayList = new ArrayList<HashMap<String, String>>();

                Cursor cursorBudgetName;
                if (!filterAccount.equals("N/A")) {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_account + "='" + filterAccount + "' and " + helper.bud_interval + "='Month'", null);
                } else {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_interval + "='Month'", null);
                }

                if (cursorBudgetName.getCount() > 0) {

                    for (int j = 0; j < cursorBudgetName.getCount(); j++) {
                        double total_amount = 0;
                        cursorBudgetName.moveToPosition(j);
                        HashMap<String, String> budgetMap = new HashMap<String, String>();
                        budgetMap.put("bud_name", cursorBudgetName.getString(0));
                        log(tag + "bud_name =" + cursorBudgetName.getString(0));
                        budgetMap.put("bud_amount", cursorBudgetName.getString(1));
                        log(tag + "bud_amount =" + cursorBudgetName.getString(1));
                        budgetMap.put("bud_account", cursorBudgetName.getString(2));
                        budgetMap.put("bud_id", cursorBudgetName.getString(3));
                        //Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + filterAccount + "'", null);
                        Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + cursorBudgetName.getString(2) + "'", null);
                        if (CategoryName.getCount() > 0) {
                            for (int k = 0; k < CategoryName.getCount(); k++) {
                                CategoryName.moveToPosition(k);
                                log("CategoryName cursor at position =" + k);
                                log("CategoryName = " + CategoryName.getString(0));
                                Cursor cursorTotal;

                                if (!filterAccount.equals("N/A")) {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "' and " + helper.rec_account + "='" + filterAccount + "'", null);
                                } else {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_month + "='" + c.getString(1) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "'", null);
                                }

                                if (cursorTotal.getCount() > 0) {

                                    cursorTotal.moveToFirst();
                                    log("CursorTotal Total=" + Double.parseDouble(cursorTotal.getString(0)));
                                    total_amount = total_amount + Double.parseDouble(cursorTotal.getString(0));
                                }

                            }
                        }
                        budgetMap.put("interval", "Month");
                        budgetMap.put("expense_amount", "" + total_amount);
                        budgetMap.put("remain_amount", "" + (Double.parseDouble(cursorBudgetName.getString(1)) - total_amount));
                        arrayList.add(budgetMap);
                    }
                }
                log("strDate =" + c.getString(1) + ", " + c.getString(0) + " arrayList =" + arrayList);
                if (arrayList != null) {
                    mapArrayList.put(c.getString(1) + ", " + c.getString(0), arrayList);
                }

            }


        }


        return mapArrayList;
    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> getYearlyBudget() {
        String tag = "getMonthlyBudget Method ";
        log(tag + "called");
        String filterCategory, filterAccount, sortOrder, filterString = "", filterStringCategoryName = " and ";
        filterAccount = dataPrefrences.GetStoredPrefrence("budget_account_filter");
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> mapArrayList = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        ArrayList<HashMap<String, String>> arrayList;
        if (filterAccount.equals("N/A")) {
            filterString = " where " + helper.rec_type + "='expense' ";
        } else {
            filterString = " where " + helper.rec_account + "='" + filterAccount + "' and " + helper.rec_type + "='expense' ";
        }


        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select DISTINCT " + helper.rec_year + " from " + helper.TabRecords + filterString, null);


        if (c.getCount() > 0) {
            for (int i = 0; i < c.getCount(); i++) {
                c.moveToPosition(i);
                arrayList = new ArrayList<HashMap<String, String>>();

                Cursor cursorBudgetName;
                if (!filterAccount.equals("N/A")) {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_account + "='" + filterAccount + "' and " + helper.bud_interval + "='Year'", null);
                } else {
                    cursorBudgetName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_amount + ", " + helper.bud_account + ", " + helper.id + " from " + helper.TabBudget + " where " + helper.bud_interval + "='Year'", null);
                }

                if (cursorBudgetName.getCount() > 0) {

                    for (int j = 0; j < cursorBudgetName.getCount(); j++) {
                        double total_amount = 0;
                        cursorBudgetName.moveToPosition(j);
                        HashMap<String, String> budgetMap = new HashMap<String, String>();
                        budgetMap.put("bud_name", cursorBudgetName.getString(0));
                        log(tag + "bud_name =" + cursorBudgetName.getString(0));
                        budgetMap.put("bud_amount", cursorBudgetName.getString(1));
                        log(tag + "bud_amount =" + cursorBudgetName.getString(1));
                        budgetMap.put("bud_account", cursorBudgetName.getString(2));
                        budgetMap.put("bud_id", cursorBudgetName.getString(3));
                        //Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + filterAccount + "'", null);
                        Cursor CategoryName = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + cursorBudgetName.getString(0) + "' and " + helper.bud_cat_account + "='" + cursorBudgetName.getString(2) + "'", null);
                        if (CategoryName.getCount() > 0) {
                            for (int k = 0; k < CategoryName.getCount(); k++) {
                                CategoryName.moveToPosition(k);

                                log("CategoryName cursor at position =" + k);
                                log("CategoryName = " + CategoryName.getString(0));
                                Cursor cursorTotal;

                                if (!filterAccount.equals("N/A")) {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "' and " + helper.rec_account + "='" + filterAccount + "'", null);
                                } else {
                                    cursorTotal = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select Total(" + helper.rec_ammount + ") from " + helper.TabRecords + " where " + helper.rec_type + "='expense' and " + helper.rec_year + "='" + c.getString(0) + "' and " + helper.rec_category + "='" + CategoryName.getString(0) + "'", null);
                                }

                                if (cursorTotal.getCount() > 0) {

                                    cursorTotal.moveToFirst();
                                    log("CursorTotal Total=" + Double.parseDouble(cursorTotal.getString(0)));
                                    total_amount = total_amount + Double.parseDouble(cursorTotal.getString(0));
                                }
                            }
                        }

                        budgetMap.put("interval", "Year");
                        budgetMap.put("expense_amount", "" + total_amount);
                        budgetMap.put("remain_amount", "" + (Double.parseDouble(cursorBudgetName.getString(1)) - total_amount));
                        arrayList.add(budgetMap);
                    }
                }
                log("strDate =" + c.getString(0) + " arrayList =" + arrayList);
                if (arrayList != null) {
                    mapArrayList.put(c.getString(0), arrayList);
                }

            }


        }


        return mapArrayList;
    }

    public String[] getBudgetCategories(String id) {
        String[] arrayCategories = null;
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_name + ", " + helper.bud_account + " from " + helper.TabBudget + " where " + helper.id + "='" + id + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();

            Cursor cursorCategory = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.bud_cat_category_name + " from " + helper.TabBudgetCategories + " where " + helper.bud_cat_name + "='" + c.getString(0) + "' and " + helper.bud_cat_account + "='" + c.getString(1) + "'", null);
            if (cursorCategory.getCount() > 0) {
                String[] categories = new String[cursorCategory.getCount()];
                for (int i = 0; i < cursorCategory.getCount(); i++) {
                    cursorCategory.moveToPosition(i);
                    categories[i] = cursorCategory.getString(0);
                }
                arrayCategories = categories;

            }
        }
        return arrayCategories;
    }

    public int getDublicateCategory(String catName) {
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select " + helper.cat_name + " from " + helper.TabCategory + " where " + helper.cat_name + "='" + catName + "'", null);
        if (c.getCount() > 0) {

            return 1;
        } else {
            return 0;
        }
    }

    public String getMasterAccount() {
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select " + helper.acc_name + " from " + helper.TabAccounts + " where " + helper.acc_type + "='master'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getString(0);

        } else {
            return "";
        }
    }

    void log(String msg) {
        Log.d(Tag, msg);
    }

}
