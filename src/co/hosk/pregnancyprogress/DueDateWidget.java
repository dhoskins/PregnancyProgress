package co.hosk.pregnancyprogress;

import org.joda.time.DateTime;
import org.joda.time.Days;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class DueDateWidget extends AppWidgetProvider {
    private static final int totalDays = 40 * 7;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("DueDateWidget", "onReceive");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("DueDateWidget", "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("DueDateWidget", "onUpdate");
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
        Settings settings = new Settings(context);
        String val = settings.get("time");
        Log.d("widget", "Val is " + val);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_due_date);
        if (val != null) {
            views.setViewVisibility(R.id.main, View.VISIBLE);
            views.setViewVisibility(R.id.alert, View.GONE);
            
            long time = Long.parseLong(val, 10);
            DateTime dueDate = new DateTime(time);
            DateTime conceptionDate = dueDate.minusDays(totalDays);
            int actualDays = Days.daysBetween(conceptionDate, new DateTime()).getDays();
            int percent = (actualDays * 100) / (totalDays);
            int weeks = actualDays / 7;
            int days = actualDays % 7;
            
            views.setProgressBar(R.id.progress, totalDays, actualDays, false);
            Log.d("Progress", ""+(actualDays * 100) + "/" + (totalDays * 100));
            views.setTextViewText(R.id.tvPercent, percent + "%");
            views.setTextViewText(R.id.tvWeeks, ""+weeks);
            views.setTextViewText(R.id.tvDays, "+"+days);
            
            Intent intent = new Intent(context, DueDateConfig.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.main, pendingIntent);
        } else {
            Log.d("DueDateWidget", "val is null, showing alert");
            views.setViewVisibility(R.id.main, View.GONE);
            views.setViewVisibility(R.id.alert, View.VISIBLE);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
