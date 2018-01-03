package newwalletapp.currency;

import android.content.Context;
import android.util.Log;

import com.example.accounts.newwalletapp.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyData {


    String Tag = "CurrencyData";
    Context context;

    public CurrencyData(Context con) {
        Log.d(Tag, "Construnter");
        this.context = con;
    }

    public ArrayList<HashMap<String, String>> getCurrencyList() {
        ArrayList<HashMap<String, String>> arrayCountry = new ArrayList<HashMap<String, String>>();
/*

		ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
		Log.d(Tag, "getCurrencyList Method");
		Map<String,String> currency=getAvailableCurrencies();
		String[] strArray=new String[currency.size()];
		int i=0;
	       for (String country : currency.keySet()) {
	            String currencyCode = currency.get(country);
	            Currency cur=Currency.getInstance(currencyCode);
	            System.out.println(country + " => " + currencyCode+" => "+cur.getSymbol());
	            
	            HashMap<String,String> map=new HashMap<String, String>();
	            map.put("country",country);
	           
	            map.put("code", currencyCode);
	           
	            map.put("symbol",""+ cur.getSymbol());
	            
	            arrayList.add(map);
	            
	            strArray[i]= currencyCode;
	            i++;
	            
	        }
	       Arrays.sort(strArray);
	       
	       for(i=0;i<strArray.length;i++)
	       {
	    	 for(int j=0;j<arrayList.size();j++)
	    	 {
	    		 if(arrayList.get(j).get("code").equals(strArray[i]))
	    		 {
	    			 
	    			arrayCountry.add(arrayList.get(j));
	    			arrayList.remove(j);
	    			Log.d(Tag, "Sorted Array"+arrayCountry.get(i));
	    			break;
	    		 }
	    	 }
	       }
	       
	       for(i=0;i<arrayCountry.size();i++)
	       {
	    	   Log.d(Tag, "Sorted Array"+arrayCountry.get(i));
	       }
*/

        String[] arrayCurrencyCode = context.getResources().getStringArray(R.array.currency_code);
        String[] arrayCurrencyName = context.getResources().getStringArray(R.array.currency_names);
        Log.d(Tag, "arrayCurrencyCode length =" + arrayCurrencyCode.length);
        Log.d(Tag, "arrayCurrencyName length =" + arrayCurrencyName.length);

        for (int i = 0; i < arrayCurrencyCode.length; i++) {
            try {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("country", arrayCurrencyName[i]);
                Log.d(Tag, "Currency Name =" + arrayCurrencyName[i]);

                map.put("code", arrayCurrencyCode[i]);
                Log.d(Tag, "Currency Code =" + arrayCurrencyCode[i]);

                Currency cur = Currency.getInstance(arrayCurrencyCode[i]);

                map.put("symbol", "" + cur.getSymbol());
                Log.d(Tag, "Currency Symbol =" + cur.getSymbol());

                arrayCountry.add(map);

            }
            catch (Exception e)
            {
             Log.d(Tag,"Currency Exception "+e);
            }

        }

        return arrayCountry;

    }

    private Map<String, String> getAvailableCurrencies() {
        Locale[] locales = Locale.getAvailableLocales();

        //
        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        //
        Map<String, String> currencies = new TreeMap<String, String>();
        for (Locale locale : locales) {
            try {
                currencies.put(locale.getDisplayCountry(),Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
                // when the locale is not supported
            }
        }
        return currencies;
    }

}
