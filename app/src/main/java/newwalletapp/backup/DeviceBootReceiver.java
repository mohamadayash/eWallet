package newwalletapp.backup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.accounts.newwalletapp.R;

import newwalletapp.database.DataPrefrences;

public class DeviceBootReceiver extends BroadcastReceiver {
    DataPrefrences dataPrefrences;
    public DeviceBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        dataPrefrences=new DataPrefrences(context);
        if (dataPrefrences.GetStoredPrefrence("backup_interval").equals("N/A") || dataPrefrences.GetStoredPrefrence("backup_interval").equals(context.getResources().getString(R.string.backup_weekly)))
        {
            dataPrefrences.StorePrefrence("backup_interval",context.getResources().getString(R.string.backup_weekly));
            ScheduleBackup scheduleBackup=new ScheduleBackup(context);
            scheduleBackup.changeAlarmInterval(context.getResources().getString(R.string.backup_weekly));
        }else if (dataPrefrences.GetStoredPrefrence("backup_interval").equals(context.getResources().getString(R.string.backup_daily)))
        {
            dataPrefrences.StorePrefrence("backup_interval",context.getResources().getString(R.string.backup_daily));
            ScheduleBackup scheduleBackup=new ScheduleBackup(context);
            scheduleBackup.changeAlarmInterval(context.getResources().getString(R.string.backup_daily));
        }
        else if (dataPrefrences.GetStoredPrefrence("backup_interval").equals(context.getResources().getString(R.string.backup_monthly)))
        {
            dataPrefrences.StorePrefrence("backup_interval",context.getResources().getString(R.string.backup_monthly));
            ScheduleBackup scheduleBackup=new ScheduleBackup(context);
            scheduleBackup.changeAlarmInterval(context.getResources().getString(R.string.backup_monthly));
        }
    }
}
