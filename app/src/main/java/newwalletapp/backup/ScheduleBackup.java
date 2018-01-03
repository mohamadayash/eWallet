package newwalletapp.backup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.accounts.newwalletapp.R;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ahmedchoteri on 30-04-15.
 */
public class ScheduleBackup  {
    Context context;
    public ScheduleBackup(Context context){
        this.context = context;

    }

    public void changeAlarmInterval(String interval) {

        Log.d("ScheduleBackup","changeAlarmInterval method call");
        long time = new GregorianCalendar().getTimeInMillis()+(1000*20);
        Intent intentAlarm = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 1000 * 20, pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if (context.getResources().getString(R.string.backup_daily).equals(interval)){
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }
        else if (context.getResources().getString(R.string.backup_weekly).equals(interval))
        {
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
        }
        else if (context.getResources().getString(R.string.backup_monthly).equals(interval))
        {
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pendingIntent);
        }


/*
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        am.cancel(pi); // cancel any existing alarms
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.MINUTE,30);
        long time = new GregorianCalendar().getTimeInMillis()+(1000*2);
        Log.d("AlarmSchedual","Method Call");
        if (context.getResources().getString(R.string.backup_daily).equals(interval))
        {
            //am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
            am.setRepeating(AlarmManager.RTC_WAKEUP,time, 1000*20, pi);
            Log.d("AlarmSchedual","Set Interval to Daily");
        }
        else if (context.getResources().getString(R.string.backup_weekly).equals(interval))
        {
            Log.d("AlarmSchedual","Set Interval To Weekly");
        }
        else if (context.getResources().getString(R.string.backup_monthly).equals(interval))
        {
            Calendar calendar1=Calendar.getInstance();
            calendar1.setTimeInMillis(System.currentTimeMillis());
            calendar1.set(Calendar.MONTH,1);
            am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),calendar1.getTimeInMillis(),pi);
            //am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,calendar.getTimeInMillis(),calendar1.getTimeInMillis(), pi);
            Log.d("AlarmSchedual","Set Interval To monthly");

        }
*/

    }

}
