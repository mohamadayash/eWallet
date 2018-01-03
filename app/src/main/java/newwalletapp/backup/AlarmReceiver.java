package newwalletapp.backup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DataBackup dataBackup = new DataBackup(context);
        dataBackup.exportDatabase();
    }
}
