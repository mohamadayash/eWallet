package newwalletapp.extras;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by ahmedchoteri on 06-05-15.
 */
public class Constants {

    public static final String DATABASE_PACKAGE = "com.example.accounts.newwalletapp";
    public static final String DATABASE_PASSWORD = "password123";

    private static Typeface kufiFont;
    public static Typeface GetKufiFont(Context ctx){
        if (kufiFont == null)
            kufiFont = Typeface.createFromAsset( ctx.getAssets(), "DroidKufi-Bold.ttf");
        return kufiFont;
    }

    public static void SetLanguage(Context ctx, String currentlanguage) {
        Resources res = ctx.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(currentlanguage);
        res.updateConfiguration(conf, dm);
    }

    public static String computeHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}