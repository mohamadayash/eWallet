package newwalletapp.backup;

import android.content.Context;
import android.os.Environment;

import com.example.accounts.newwalletapp.MainActivity;
import com.example.accounts.newwalletapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import newwalletapp.database.DataPrefrences;
import newwalletapp.database.DatabaseSqlite;
import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 29-04-15.
 */
public class DataBackup {

    Context context;

    public DataBackup(Context context)
    {
       this.context=context;

    }

    public String exportDatabase()
    {
        //File sd = Environment.getExternalStorageDirectory();
        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+context.getResources().getString(R.string.app_name)+File.separator+context.getResources().getString(R.string.backup));
        boolean success = true;
        if (!dir.exists())
        {
            success = dir.mkdirs();
        }

        if (success)
        {
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            //String currentDBPath = "/data/"+ "com.your.package" +"/databases/"+db_name;
            String currentDBPath = "/data/"+ Constants.DATABASE_PACKAGE+"/databases/"+ DatabaseSqlite.db_name;
            String backupDBPath = getCurrentTimeStamp()+".bak";
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(dir, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                //Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
            } catch(IOException e) {
                e.printStackTrace();
            }
            return dir+"/"+backupDBPath;
        }
        else
        {
            return context.getResources().getString(R.string.somthing_wrong);
        }

    }
    public String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }
}
