package newwalletapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 17-02-15.
 */
public class UpdateSQLiteData {

    String Tag="UpdateSQLiteData";
    Context context;
    SQLiteDatabase db;
    DatabaseSqlite helper;
    public UpdateSQLiteData(Context con)
    {
        Log.d(Tag,"Constructor");
        this.context=con;
        SQLiteDatabase.loadLibs(con);
        helper=new DatabaseSqlite(con);

    }

    public int updateAccount(String id,String oldAccount,String accountName)
    {

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select "+helper.acc_name+" from "+helper.TabAccounts+" where "+helper.acc_name+"='"+accountName+"'",null);
        if (c.getCount()>0)
        {
            return 1;
        }
        Log.d(Tag,"updateAccount Method");
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        ContentValues cv=new ContentValues();
        cv.put(helper.acc_name,accountName);
        Log.d(Tag,"Id ="+id+"Account Name= "+accountName);
        db.update(helper.TabAccounts,cv,helper.id+"='"+id+"'",null);
        Log.d(Tag, "Updated");
        cv=new ContentValues();
        cv.put(helper.rec_account, accountName);
        db.update(helper.TabRecords, cv, helper.rec_account + "= ?", new String[]{oldAccount});
        cv=new ContentValues();
        cv.put(helper.bud_account, accountName);
        db.update(helper.TabBudget, cv, helper.bud_account + "= ?", new String[]{oldAccount});
        cv=new ContentValues();
        cv.put(helper.bud_cat_account,accountName);
        db.update(helper.TabBudgetCategories, cv, helper.bud_cat_account+ "= ?", new String[] {oldAccount});

        db.close();
        Log.d(Tag,"DataBase Closed");
        return 0;

    }

    public void updateCurrency(String id,HashMap<String,String> map)
    {
        Log.d(Tag,"updateCurrency Method");

        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        ContentValues cv=new ContentValues();

        cv.put(helper.cur_symbol, map.get("symbol"));
        Log.d(Tag,"Symbol "+ map.get("symbol"));

        cv.put(helper.cur_code, map.get("code"));
        Log.d(Tag,"Code "+ map.get("code"));


        cv.put(helper.cur_name, map.get("country"));
        Log.d(Tag,"Country "+ map.get("country"));

        cv.put(helper.cur_rate, map.get("rate"));
        Log.d(Tag,"Rate "+ map.get("rate"));

        cv.put(helper.cur_type, "type");
        Log.d(Tag,"Type "+ map.get("type"));

        db.update(helper.TabCurrencies,cv,helper.id+"='"+id+"'",null);
        db.close();

    }

    public void updateRecord(String id,HashMap<String,String> map)
    {
        String tag="updateRecord";
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        ContentValues cv=new ContentValues();

        cv.put(helper.rec_account, map.get("account"));
        Log.d(Tag+" "+tag,"Account "+ map.get("account"));

        cv.put(helper.rec_ammount, map.get("amount"));
        Log.d(Tag+" "+tag,"Amount "+ map.get("amount"));


        cv.put(helper.rec_currency, map.get("currency"));
        Log.d(Tag,"Country "+ map.get("currency"));

        cv.put(helper.rec_category, map.get("category"));
        Log.d(Tag+" "+tag,"Rate "+ map.get("category"));

        cv.put(helper.rec_type, map.get("type"));
        Log.d(Tag+" "+tag,"Type "+ map.get("type"));

        cv.put(helper.rec_description, map.get("des"));
        Log.d(Tag+" "+tag,"Description "+ map.get("des"));


        cv.put(helper.rec_time, map.get("time"));
        Log.d(Tag,"Time "+ map.get("time"));

        cv.put(helper.rec_date, map.get("date"));
        Log.d(Tag+" "+tag,"Date "+ map.get("date"));

        cv.put(helper.rec_date_for_sort,map.get("sdate"));
        Log.d(Tag+" "+tag,"Date For Sort ="+map.get("sdate"));

        cv.put(helper.rec_week,map.get("week"));
        Log.d(Tag+" "+tag,"Week ="+map.get("week"));

        cv.put(helper.rec_month,map.get("month"));
        Log.d(Tag+" "+tag,"Month ="+map.get("month"));

        cv.put(helper.rec_year,map.get("year"));
        Log.d(Tag+" "+tag,"Year ="+map.get("year"));


        cv.put(helper.rec_currency_type,map.get("cur_type"));
        Log.d(Tag+" "+tag,"Currency type  ="+map.get("cur_type"));

        cv.put(helper.rec_child_amount,map.get("childamount"));
        Log.d(Tag+" "+tag,"Child Amount ="+map.get("childamount"));

        db.update(helper.TabRecords,cv,helper.id+"='"+id+"'",null);
        Log.d(Tag+" "+tag,"Record Updated with id ="+id);
        db.close();
    }

    public void updateCategory(String id,String catName,HashMap<String,String> map)
    {
        String tag="updateCategory";
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        ContentValues cv=new ContentValues();

        cv.put(helper.cat_name, map.get("name"));
        Log.d(Tag+" "+tag,"Name "+ map.get("name"));

        cv.put(helper.cat_description, map.get("des"));
        Log.d(Tag+" "+tag,"Description "+ map.get("des"));


        cv.put(helper.cat_icon, map.get("icon"));
        Log.d(Tag+" "+tag,"Icon "+ map.get("icon"));

        cv.put(helper.cat_color,map.get("color"));
        Log.d(Tag+" "+tag,"Color "+ map.get("color"));

        cv.put(helper.cat_type, map.get("type"));
        Log.d(Tag+" "+tag,"Type "+ map.get("type"));

        cv.put(helper.cat_parent, map.get("parent"));
        Log.d(Tag+" "+tag,"Parent "+ map.get("parent"));

        db.update(helper.TabCategory,cv,helper.id+"='"+id+"'",null);
        cv=new ContentValues();
        cv.put(helper.rec_category, map.get("name"));
        db.update(helper.TabRecords, cv, helper.rec_category + "= ?", new String[] {catName});
        cv=new ContentValues();
        cv.put(helper.bud_cat_category_name, map.get("name"));
        db.update(helper.TabBudgetCategories, cv, helper.bud_cat_category_name + "= ?", new String[] {catName});
        Log.d(Tag+" "+tag,"Record Updated with id ="+id+" and Name ="+catName);
        db.close();
    }

    public int updateBudget(String recId,String oldName,HashMap<String,String> map,ArrayList<String> cat) {
        String tag = "UpdateBudget";
        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from " + helper.TabBudget + " where " + helper.bud_name + "='" + map.get("name") + "' and " + helper.bud_account + "='" + map.get("account") + "'", null);
        if (c.getCount() > 0 && !oldName.equals(map.get("name"))) {

            return 1;
        } else {
            db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
            ContentValues cv=new ContentValues();

            cv.put(helper.bud_name, ""+map.get("name"));
            Log.d(Tag+" "+tag,"Budget Name "+ map.get("name"));

            cv.put(helper.bud_account,""+ map.get("account"));
            Log.d(Tag+" "+tag,"Account Name "+ map.get("account"));

            cv.put(helper.bud_interval,""+ map.get("interval"));
            Log.d(Tag+" "+tag,"Interval "+ map.get("interval"));


            cv.put(helper.bud_amount, ""+map.get("amount"));
            Log.d(Tag,"Amount "+ map.get("amount"));

            db.update(helper.TabBudget,cv,helper.id+"='"+recId+"'",null);
            Log.d(Tag+" "+tag,"Data Updated To  TabBudget");

            db.delete(helper.TabBudgetCategories,helper.bud_cat_name+"='"+map.get("name")+"' and "+helper.bud_cat_account+"='"+map.get("account")+"'",null);
            for (int i=0;i<cat.size();i++) {
                cv = new ContentValues();
                cv.put(helper.bud_cat_name, map.get("name"));
                Log.d(Tag + " " + tag, "Budget Name  " + map.get("name"));

                cv.put(helper.bud_account,""+ map.get("account"));
                Log.d(Tag+" "+tag,"Account Name "+ map.get("account"));

                cv.put(helper.bud_cat_category_name, cat.get(i));
                Log.d(Tag + " " + tag, "Category Name " + cat.get(i));

                db.insert(helper.TabBudgetCategories, null, cv);
                Log.d(Tag+" "+tag,"Data Inserted To  TabBudgetCategories");

            }
            db.close();

            return 0;
        }
    }
}
