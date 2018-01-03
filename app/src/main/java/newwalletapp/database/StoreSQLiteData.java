package newwalletapp.database;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

import newwalletapp.extras.Constants;

public class StoreSQLiteData {

	String Tag = "StoreSQLiteData";
	SQLiteDatabase db;
	DatabaseSqlite helper;
	Context context;

	public StoreSQLiteData(Context con) {
		this.context = con;
        SQLiteDatabase.loadLibs(con);
		helper = new DatabaseSqlite(context);
	}

	public void storeCurrencyData(HashMap<String, String> map, String type) {

		String tag="storeCurrencyData";
		
		db = helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
		ContentValues cv = new ContentValues();
		
		cv.put(helper.cur_symbol, map.get("symbol"));

		Log.d(Tag+" "+tag,"Symbol "+ map.get("symbol"));
		
		cv.put(helper.cur_code, map.get("code"));
		Log.d(Tag+" "+tag,"Code "+ map.get("code"));
		
		cv.put(helper.cur_name, map.get("country"));
		Log.d(Tag+" "+tag,"Name "+ map.get("country"));
		
		if (type.equals("child")) {
			cv.put(helper.cur_rate, map.get("rate"));
			Log.d(Tag+" "+tag,"Rate "+ map.get("rate"));
		}
		cv.put(helper.cur_type, type);
		Log.d(Tag+" "+tag,"Type "+ type);
		
		db.insert(helper.TabCurrencies, null, cv);
		Log.d(Tag+" "+tag,"Data Inserted To  TabCurrencies"+ type);

        db.close();
		Log.d(Tag+" "+tag,"DataBase Closed");
	}
	
	public int AddNewAccount(String accountName,String type)
	{

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("select "+helper.acc_name+" from "+helper.TabAccounts+" where "+helper.acc_name+"='"+accountName+"'",null);

        if (c.getCount()>0)
        {
            return 1;
        }

		String tag="AddNewAccount";

        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
		ContentValues cv=new ContentValues();

        cv.put(helper.acc_name,accountName);
		Log.d(Tag+" "+tag,"Name "+ accountName);

        cv.put(helper.acc_type,type);
        Log.d(Tag+" "+tag,"Type "+ type);
		
		db.insert(helper.TabAccounts, null, cv);
		Log.d(Tag+" "+tag,"Data Inserted To  TabAccounts");

        db.close();
		Log.d(Tag+" "+tag,"DataBase Closed");

        return 0;
		
	}
	
	public void AddNewCategoury(HashMap<String,String> map)
	{
		String tag="AddNewCategoury";
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


		
		db.insert(helper.TabCategory, null, cv);
		Log.d(Tag+" "+tag,"Data Inserted To  TabCategory");
		db.close();
		Log.d(Tag+" "+tag,"DataBase Closed");

		
	}

    public void AddNewCurrency(HashMap<String,String> map)
    {
        String tag="AddNewCurrency";
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        ContentValues cv=new ContentValues();

        cv.put(helper.cur_symbol, map.get("symbol"));
        Log.d(Tag+" "+tag,"Symbol "+ map.get("symbol"));

        cv.put(helper.cur_code, map.get("code"));
        Log.d(Tag+" "+tag,"Code "+ map.get("code"));


        cv.put(helper.cur_name, map.get("country"));
        Log.d(Tag,"Country "+ map.get("country"));

        cv.put(helper.cur_rate, map.get("rate"));
        Log.d(Tag+" "+tag,"Rate "+ map.get("rate"));

        cv.put(helper.cur_type,map.get("type"));
        Log.d(Tag+" "+tag,"Type "+ map.get("type"));



        db.insert(helper.TabCurrencies, null, cv);
        Log.d(Tag+" "+tag,"Data Inserted To  TabCurrencies");
        db.close();
        Log.d(Tag+" "+tag,"DataBase Closed");


    }

    public void AddNewRecord(HashMap<String,String> map)
    {
        String tag="AddNewRecord";
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


        db.insert(helper.TabRecords, null, cv);
        Log.d(Tag+" "+tag,"Data Inserted To  TabRecords");
        db.close();
        Log.d(Tag+" "+tag,"DataBase Closed");

    }

    public int AddBudget(HashMap<String,String> map,ArrayList<String> cat)
    {
        String tag="AddBudget";

        Cursor c = helper.getReadableDatabase(Constants.DATABASE_PASSWORD).rawQuery("Select *from "+helper.TabBudget+" where "+helper.bud_name+"='"+map.get("name")+"' and "+helper.bud_account+"='"+map.get("account")+"'",null);
        if (c.getCount()>0)
        {
            return 1;
        }
        else
        {

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

            db.insert(helper.TabBudget, null, cv);
            Log.d(Tag+" "+tag,"Data Inserted To  TabBudget");

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