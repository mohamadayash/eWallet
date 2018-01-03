package newwalletapp.backup;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.example.accounts.newwalletapp.R;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import newwalletapp.database.DatabaseSqlite;
import newwalletapp.extras.Constants;

/**
 * Created by ahmedchoteri on 07-05-15.
 */
public class DBExportImport {
    static Context context;
    public DBExportImport(Context context)
    {
      this.context=context;
    }
    public static final String TAG = DBExportImport.class.getName();

    /** Directory that files are to be read from and written to **/
    protected static final File DATABASE_DIRECTORY =new File(Environment.getExternalStorageDirectory(),context.getResources().getString(R.string.app_name)+File.separator+context.getResources().getString(R.string.backup));
    /** File path of Db to be imported **/
    protected static final File IMPORT_FILE =new File(DATABASE_DIRECTORY,getLatestFilefromDir().getName());
    public static final String PACKAGE_NAME = Constants.DATABASE_PACKAGE;
    public static final String DATABASE_NAME = DatabaseSqlite.db_name ;
//    public static final String DATABASE_TABLE = "entryTable";
    /** Contains: /data/data/com.example.app/databases/example.db **/
    private static final File DATA_DIRECTORY_DATABASE =
            new File(Environment.getDataDirectory() +
                    "/data/" + PACKAGE_NAME +
                    "/databases/" + DATABASE_NAME );

    /** Saves the application database to the
     * export directory under MyDb.db **/
    private static File getLatestFilefromDir() {



        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name) + File.separator + context.getResources().getString(R.string.backup));
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            //log("file not found");
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
//        log("last modified file name =" + lastModifiedFile.getName());
        return lastModifiedFile;
    }

     public static  boolean exportDb(){
        if( ! SdIsPresent() ) return false;

        File dbFile = DATA_DIRECTORY_DATABASE;
        String filename = getCurrentTimeStamp()+".bak";

        File exportDir = DATABASE_DIRECTORY;
        File file = new File(exportDir, filename);

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        try {
            file.createNewFile();
            copyFile(dbFile, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String getCurrentTimeStamp()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }
    /** Replaces current database with the IMPORT_FILE if
     * import database is valid and of the correct type **/
    protected static boolean restoreDb(){
        if( ! SdIsPresent() ) return false;

        File exportFile = DATA_DIRECTORY_DATABASE;
        File importFile = IMPORT_FILE;

//        if( ! checkDbIsValid(importFile) ) return false;

        if (!importFile.exists()) {
            Log.d(TAG, "File does not exist");
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

/*
    */
/** Imports the file at IMPORT_FILE **//*

    protected static boolean importIntoDb(Context ctx){
        if( ! SdIsPresent() ) return false;

        File importFile = IMPORT_FILE;

        if( ! checkDbIsValid(importFile) ) return false;

        try{
            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase(Constants.DATABASE_PASSWORD,importFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = sqlDb.query(true, DATABASE_TABLE,null, null, null, null, null, null, null);

            DbAdapter dbAdapter = new DbAdapter(ctx);
            dbAdapter.open();

            final int titleColumn = cursor.getColumnIndexOrThrow("title");
            final int timestampColumn = cursor.getColumnIndexOrThrow("timestamp");

            // Adds all items in cursor to current database
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                dbAdapter.createQuote(cursor.getString(titleColumn),cursor.getString(timestampColumn));
            }

            sqlDb.close();
            cursor.close();
            dbAdapter.close();
        } catch( Exception e ){
            e.printStackTrace();
            return false;
        }

        return true;
    }
*/

    /** Given an SQLite database file, this checks if the file
     * is a valid SQLite database and that it contains all the
     * columns represented by DbAdapter.ALL_COLUMN_KEYS **/
/*
    protected static boolean checkDbIsValid( File db ){
        try{
            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase(Constants.DATABASE_PASSWORD,db.getPath(), null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor = sqlDb.query(true, DATABASE_TABLE,null, null, null, null, null, null, null);

            // ALL_COLUMN_KEYS should be an array of keys of essential columns.
            // Throws exception if any column is missing
            for( String s : DbAdapter.ALL_COLUMN_KEYS ){
                cursor.getColumnIndexOrThrow(s);
            }
            sqlDb.close();
            cursor.close();
        } catch( IllegalArgumentException e ) {
            Log.d(TAG, "Database valid but not the right type");
            e.printStackTrace();
            return false;
        } catch( SQLiteException e ) {
            Log.d(TAG, "Database file is invalid.");
            e.printStackTrace();
            return false;
        } catch( Exception e){
            Log.d(TAG, "checkDbIsValid encountered an exception");
            e.printStackTrace();
            return false;
        }
        return true;
    }
*/

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

    /** Returns whether an SD card is present and writable **/
    public static boolean SdIsPresent() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
