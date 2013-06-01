package co.hosk.pregnancyprogress;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

public class DueDateWidget extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Settings settings = new Settings(context);
        String val = settings.get("time");
        Log.d("widget", "Val is " + val);
        if (val != null) {
            long time = Long.parseLong(val, 10);
        }
    }
    
}
