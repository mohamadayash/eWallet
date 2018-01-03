package newwalletapp.backup;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.accounts.newwalletapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import newwalletapp.database.DatabaseSqlite;
import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 07-05-15.
 */
public class ImportBackup {
    Context context;

    public ImportBackup(Context context) {
        this.context = context;

    }

    public boolean isBackupAvailable() {
        log("isBackupAvailable");
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + File.separator + context.getResources().getString(R.string.backup));
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            log("any files not found");
            return false;
        } else {
            log("files found");
            return true;
        }

    }

    public boolean restoreDb() {
        if (!SdIsPresent()) return false;

        File databaseFile = new File("/data/data/" + Constants.DATABASE_PACKAGE + "/databases");


        // check if databases folder exists, if not create one and its subfolders
        if (!databaseFile.exists()) {
            databaseFile.mkdir();
        }
        File DATA_DIRECTORY_DATABASE = new File(Environment.getDataDirectory() + "/data/" + Constants.DATABASE_PACKAGE + "/databases/" + DatabaseSqlite.db_name);
        File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), context.getResources().getString(R.string.app_name) + File.separator + context.getResources().getString(R.string.backup));
        File IMPORT_FILE = new File(DATABASE_DIRECTORY, getLatestFilefromDir(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + File.separator + context.getResources().getString(R.string.backup)).getName());
        File exportFile = DATA_DIRECTORY_DATABASE;
        File importFile = IMPORT_FILE;

//        if( ! checkDbIsValid(importFile) ) return false;

        if (!importFile.exists()) {
            //          Log.d(TAG, "File does not exist");
            return false;
        }

        try {
            exportFile.createNewFile();
            copyFile(importFile, exportFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
/*
    public void importDataBase() {
        final String inFileName = "/data/data/" + Constants.DATABASE_PACKAGE + "/databases/";
        FileChannel source = null;
        FileChannel destination = null;
        File backupFile = getLatestFilefromDir(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + File.separator + context.getResources().getString(R.string.backup));

        File currentData = new File(Environment.getExternalStorageDirectory()+File.separator+context.getResources().getString(R.string.app_name)+File.separator+context.getResources().getString(R.string.backup));
        File restoreData = new File(Environment.getDataDirectory()+"/data/" + Constants.DATABASE_PACKAGE + "/databases/");
        String restoreDBPath = "/data/"+ Constants.DATABASE_PACKAGE+"/databases/"+ DatabaseSqlite.db_name;

        File currentDB = new File(currentData, backupFile.getName());
        File restoreDB = new File(restoreData, DatabaseSqlite.db_name);
        try {
            restoreDB.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log("Create new file error "+e);
        }


        try {
            source = new FileInputStream(backupFile).getChannel();
            destination = new FileOutputStream(restoreDB).getChannel();
            destination.transferFrom(source, 0, source.size());

            source.close();
            destination.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log("Error " + e);
        } catch (IOException e) {
            e.printStackTrace();
            log("Error " + e);
        }
    }
*/

    private File getLatestFilefromDir(String dirPath) {
        log("getLatestFilefromDir");
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            log("file not found");
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        log("last modified file name =" + lastModifiedFile.getName());
        return lastModifiedFile;
    }

    void log(String message) {
        Log.d("ImportBackup", message);
    }
}
