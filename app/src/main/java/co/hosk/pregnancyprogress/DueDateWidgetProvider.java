package co.hosk.pregnancyprogress;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class DueDateWidgetProvider extends AppWidgetProvider {

    // log tag
    private static final String TAG = "DueDateWidgetProvider";


    private static final int totalDays = 40 * 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate");
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {
        Settings settings = new Settings(context);
        String val = settings.get("time");
        Log.d(TAG, "Saved DueDate is: " + val);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_due_date);
        if (val != null) {
            views.setViewVisibility(R.id.main, View.VISIBLE);
            views.setViewVisibility(R.id.alert, View.GONE);

            long dueDateTime = Long.parseLong(val, 10);
            long now = System.currentTimeMillis();
            long remainingDurration = dueDateTime - now;
            int remainingDays = (int) (remainingDurration/(24*60*60*1000));
            int actualDays = totalDays - remainingDays;
            int percent = (actualDays * 100) / (totalDays);
            int weeks = actualDays / 7;
            int days = actualDays % 7;

            views.setProgressBar(R.id.progress, totalDays, actualDays, false);
            Log.d(TAG, String.format(" Progress %d / %d", (actualDays * 100), (totalDays * 100)));
            views.setTextViewText(R.id.tvPercent, percent + "%");
            views.setTextViewText(R.id.tvWeeks, "" + weeks);
            views.setTextViewText(R.id.tvDays, "+" + days);

            Intent intent = new Intent(context, DueDateWidgetConfigure.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.main, pendingIntent);
        } else {
            Log.d(TAG, "val is null, showing alert");
            views.setViewVisibility(R.id.main, View.GONE);
            views.setViewVisibility(R.id.alert, View.VISIBLE);
        }
        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
