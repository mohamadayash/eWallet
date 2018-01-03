package newwalletapp.database;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class DataPrefrences {

	String TAG = "DataPrefrences";
	public static final String DEFAULT = "N/A";
	Context context;

	public DataPrefrences(Context con) {
		this.context = con;
	}

	public void StorePrefrence(String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences("MyData",context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		Log.d(TAG, "StorePrefrence ="+key +" Value "+value);
		editor.commit();
	}

	public String GetStoredPrefrence(String key) {
		SharedPreferences preferences = context.getSharedPreferences("MyData",context.MODE_PRIVATE);
		String value = preferences.getString(key, DEFAULT);
		Log.d(TAG, "GetStoredPrefrence Key ="+key +" Value "+value);
		return value;
	}
	

}
