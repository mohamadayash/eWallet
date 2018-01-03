package newwalletapp.database;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
/*import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;*/
import android.util.Log;

public class DatabaseSqlite extends SQLiteOpenHelper{

	public static String db_name = "Data.db";
	String Tag="DatabaseSqlite";

	public String TabAccounts="Accounts";
	public String id="id";
	public String acc_name="AccountName";
    public String acc_type="AccountType";//master,child
	
	public String TabCurrencies="Currencies";
	public String cur_symbol="Symbol";
	public String cur_code="Code";
	public String cur_name="Name";
	public String cur_rate="Rate";
	public String cur_type="Type";//(Master or Child)
	
	public String TabCategory="Category";
	public String cat_name="Name";//1
	public String cat_description="Description";//2
	public String cat_icon="Icon";//3
    public String cat_color="Color";//4
	public String cat_type="Type";//(Expence or Income)//5
    public String cat_parent="Parent";//6
    
    public String TabRecords="Records";
    public String rec_account="Account";//1
    public String rec_ammount="Ammount";//2
    public String rec_currency="Currency";//3
    public String rec_category="Category";//4
    public String rec_type="Type"; //(Expence or Income) //5
    public String rec_description="Description";//6
    public String rec_time="Time";//7
    public String rec_date="Date";//8
    public String rec_date_for_sort="SortDate";//9
    public String rec_week="Week";//10
    public String rec_month="Month";//11
    public String rec_year="Year";//12
    public String rec_currency_type="CurrencyType";//13 master/child
    public String rec_child_amount="ChildAmount";//14

    public  String TabBudget="Budget";
    public  String bud_name="Name";
    public  String bud_account="Account";
    public  String bud_interval="Interval";
    public  String bud_amount="Amount";

    public  String TabBudgetCategories="BudgetCategories";
    public  String bud_cat_name="Name";
    public  String bud_cat_account="Account";
    public  String bud_cat_category_name="CategoryName";



    public DatabaseSqlite(Context context) {
		super(context, db_name, null,1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String SqlAccounts = "create table " + TabAccounts + " (" + id+ " integer primary key AUTOINCREMENT,	" + acc_name + " text ,"+acc_type+" text)";
		db.execSQL(SqlAccounts);
		Log.d(Tag, "SqlAccounts Executed");

        String SqlCurrencies = "create table " + TabCurrencies + " (" + id+ " integer primary key AUTOINCREMENT,	" + cur_symbol+ " text, " + cur_code + " text, " + cur_name + " text, "+ cur_rate + " text, " + cur_type+ " text)";
		db.execSQL(SqlCurrencies);
		Log.d(Tag, "SqlCurrencies Executed");

        String SqlCategory="create table " + TabCategory + " (" + id+ " integer primary key AUTOINCREMENT,	" + cat_name+ " text, " + cat_description + " text, " + cat_icon + " text, "+ cat_color + " text, "+ cat_type + " text, " + cat_parent+ " text)";
		db.execSQL(SqlCategory);
		Log.d(Tag, "SqlCategory Executed");

        String SqlRecords = "create table " + TabRecords + " (" + id+ " integer primary key AUTOINCREMENT,	" + rec_account + " text, "+ rec_ammount + " text, " + rec_currency + " text, "+ rec_category + " text, " + rec_type + " text, "+ rec_description + " text, " + rec_time + " text, " + rec_date + " text, "+ rec_date_for_sort+ " text, " + rec_week + " text, " + rec_month + " text, "+ rec_year+ " text, " + rec_currency_type + " text, "+ rec_child_amount+ " text)";
        db.execSQL(SqlRecords);
        Log.d(Tag, "SqlRecords Executed");

        String SqlBudget="create table " + TabBudget + " (" + id+ " integer primary key AUTOINCREMENT,	" + bud_name+ " text, " + bud_account+ " text, " + bud_interval + " text, " + bud_amount + " text)";
        db.execSQL(SqlBudget);

        String SqlBudgetCategories="create table " + TabBudgetCategories + " (" + id+ " integer primary key AUTOINCREMENT,	" + bud_cat_name+ " text, "+ bud_cat_account+ " text, " + bud_cat_category_name + " text)";
        db.execSQL(SqlBudgetCategories);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
