package newwalletapp.database;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;

import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 16-02-15.
 */
public class DeleteSQLiteData {

    String Tag = "DeleteSQLiteData";
    Context context;
    SQLiteDatabase db;
    DatabaseSqlite helper;

    public DeleteSQLiteData(Context con) {
        this.context = con;
        Log.d(Tag,"Constructor");
        SQLiteDatabase.loadLibs(con);
        helper = new DatabaseSqlite(con);
    }

    public void deleteAccout(String id,String accountName) {
        db = helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        Log.d(Tag,"deleteAccout method id="+id);
        db.delete(helper.TabAccounts, helper.id + "='" + id + "'", null);
        db.delete(helper.TabRecords, helper.rec_account + "='" + accountName + "'", null);
        db.delete(helper.TabBudget, helper.bud_account + "='" + accountName + "'", null);
        db.delete(helper.TabBudgetCategories, helper.bud_cat_account + "='" + accountName + "'", null);
        Log.d(Tag,"TabAccounts Record Deleted id="+id);
        db.close();

    }

    public void deleteCurrencyData(String idCurrency,String code,String cName)
    {
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        Log.d(Tag,"deleteCurrency method id="+idCurrency);
        db.delete(helper.TabCurrencies, helper.id + "='" + idCurrency + "'", null);
        db.delete(helper.TabRecords, helper.rec_currency + "='" + code+"  "+cName + "'", null);
        Log.d(Tag,"TabCurrency Record Deleted id="+idCurrency);
        db.close();
    }
    public  void deleteRecord(String recId)
    {
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        Log.d(Tag,"deleteRecord method id="+recId);
        db.delete(helper.TabRecords, helper.id + "='" + recId + "'", null);
        Log.d(Tag,"TabCurrency Record Deleted id="+recId);
        db.close();

    }
    public void deleteCategory(String id,String name)
    {
        db=helper.getWritableDatabase(Constants.DATABASE_PASSWORD);
        Log.d(Tag,"deleteRecord method id="+id);
        db.delete(helper.TabCategory, helper.id + "='" + id + "'", null);
        Log.d(Tag,"TabCurrency Record Deleted id="+id);
        db.delete(helper.TabRecords ,helper.rec_category + "= ?", new String[] {name});
        db.delete(helper.TabBudgetCategories ,helper.bud_cat_category_name + "= ?", new String[] {name});
        db.close();

    }
}
