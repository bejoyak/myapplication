package ai.tech5.tech5.enroll.dialog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.hdbarcode.hdbarcodereader.DecdeHDBarcodeActivity;

import ai.tech5.tech5.R;

import static ai.tech5.tech5.enroll.utils.Logger.logException;


public class RestartDialog {
    public static void showRestartAppDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.sdk_initialization_fail_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok_message), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent mStartActivity = new Intent(context, DecdeHDBarcodeActivity.class);
                            int mPendingIntentId = 123456;
                            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                            ((Activity) context).finishAffinity();
                            System.exit(0);
                        } catch (Exception e) {
                            logException("RestartDialog", e);
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
