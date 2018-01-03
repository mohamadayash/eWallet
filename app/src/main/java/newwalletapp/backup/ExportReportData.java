package newwalletapp.backup;

import android.content.Context;
import android.os.Environment;

import com.example.accounts.newwalletapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ahmedchoteri on 28-04-15.
 */
public class ExportReportData {

    Context context;
    FileWriter writer;
    public ExportReportData(Context context)
    {
        this.context=context;
    }

    public String exportReport(String filename,HashMap<String,String> map)
    {

        String ff="report_"+getCurrentTimeStamp()+filename+".csv";
        //File root = Environment.getExternalStorageDirectory();
        //File root = new File(Environment.getExternalStorageDirectory() + "/"+context.getResources().getString(R.string.app_name)+"/");
        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+context.getResources().getString(R.string.app_name)+File.separator+context.getResources().getString(R.string.reports));
        //Toast.makeText(context,"Root = "+dir,Toast.LENGTH_LONG).show();
        boolean success=true;
       if (!dir.exists())
        {
            success=dir.mkdirs();
        }
        if (success)
        {
           File gpxfile = new File(dir, ff);
            try {
                writer = new FileWriter(gpxfile);
                writeReportCsvHeader(context.getResources().getString(R.string.report_quickoverview),context.getResources().getString(R.string.report_income),context.getResources().getString(R.string.report_expence));
                writeReportCsvData(context.getResources().getString(R.string.report_count), map.get("income_count"), map.get("expense_count"));
                writeReportCsvData(context.getResources().getString(R.string.report_averayday), map.get("income_average_day"), map.get("expense_average_day"));
                writeReportCsvData(context.getResources().getString(R.string.report_averagerecord),map.get("income_average_record"), map.get("expense_average_record"));
                writeReportCsvData(context.getResources().getString(R.string.report_total),map.get("income_total"),map.get("expense_total"));
                writeReportCsvData(context.getResources().getString(R.string.report_startingbalance), map.get("starting_balance"));
                writeReportCsvData(context.getResources().getString(R.string.report_netending_balance), map.get("netending_balance"));
                writeReportCsvData(context.getResources().getString(R.string.report_cashflow), map.get("cashflow"));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dir+"/"+ff;
        }
        else
        {
           ff=" Something goes wrong";
            return ff;
        }



    }

    private void writeReportCsvHeader(String h1, String h2, String h3) throws IOException {
        String line = String.format("%s,%s,%s\n", h1,h2,h3);
        writer.write(line);
    }
    private void writeReportCsvData(String d, String e,String f) throws IOException {
        String line = String.format("%s,%s,%s\n", d, e, f);
        writer.write(line);
    }
    private void writeReportCsvData(String d, String e) throws IOException {
        String line = String.format("%s,%s\n", d, e);
        writer.write(line);
    }
    public String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

}
