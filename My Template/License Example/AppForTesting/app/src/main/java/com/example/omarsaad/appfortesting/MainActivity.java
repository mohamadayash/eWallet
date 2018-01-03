package com.example.omarsaad.appfortesting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends Activity {

    public String device_id ,ct;
    public Long time;
    DataPreferences DataPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataPreferences = new DataPreferences(getApplicationContext());

        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        time = System.currentTimeMillis();
        ct = String.valueOf(time);

        final TextView tx1 = (TextView)findViewById(R.id.textview1);


        Button serverbtn = (Button)findViewById(R.id.btnserver);

        serverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx1.setText(ct);
                if (DataPreferences.GetStoredPrefrence("liscence").equals("N/A")) {
                    AsyncTaskGetLicense action = new AsyncTaskGetLicense();
                    action.execute(Constant.url + "/License?key=" + device_id);
                } else {
                    String lisc = DataPreferences.GetStoredPrefrence("liscence");
                    if(lisc.equals("")){
                        AsyncTaskGetLicense action = new AsyncTaskGetLicense();
                        action.execute(Constant.url + "/License?key=" + device_id);
                    }else
                        GetLicense(lisc);
                }
            }
        });


    }

    private class AsyncTaskGetLicense extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            GetLicense(result);
        }
    }


    private void GetLicense(String result) {
        try{
            DataPreferences.StorePrefrence("liscence", result);
            String text = Constant.Decrypt(result, "My Key");
            String[] lic = text.split("-");
            String key_id = lic[0];
            String end_date = lic[1];
            if(key_id.equals(device_id) && end_date.compareTo(ct) > 0){
                Intent intent = new Intent(this,WelcomePage.class);
                startActivity(intent);
                Date date1,date2;
                date1 = getDate(Long.valueOf(ct),"dd/MM/yyyy hh:mm:ss");
                date2 = getDate(Long.valueOf(end_date),"dd/MM/yyyy hh:mm:ss") ;
                Toast.makeText(this,"you have " + DifferenceTwoDate(date1,date2) + "for ending this license app",Toast.LENGTH_LONG).show();
            }
            else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // set title
                alertDialogBuilder.setTitle("To Buy The App");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to buy the application")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=net.ehorizon.tel"));
                                startActivity(intent);
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                                MainActivity.this.finish();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        }catch (Exception e){
            e.getMessage();
        }
    }

    public static Date getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        Date date = null;
        try {
            date = format.parse(formatter.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String DifferenceTwoDate(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return(elapsedDays-1 + "days, " + elapsedHours + "hours, " + elapsedMinutes + "minutes, " + elapsedSeconds + "seconds");

    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = Constant.convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

}
