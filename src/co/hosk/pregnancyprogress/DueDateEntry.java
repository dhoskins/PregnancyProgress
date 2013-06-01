package co.hosk.pregnancyprogress;

import java.util.Calendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

public class DueDateEntry extends Activity {

    private Settings s;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_date_entry);
        s = new Settings(this);
        final DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
        dp.setCalendarViewShown(false);
        View v = findViewById(R.id.saveButton);
        v.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                int day = dp.getDayOfMonth();
                int month = dp.getMonth();
                int year = dp.getYear();
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                long time = c.getTimeInMillis();
                saveTime(time);
            }
        });
    }

    private void saveTime(long time) {
        s.put("time", String.valueOf(time));
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            
            //AppWidgetManager awm = AppWidgetManager.getInstance(this);
            //RemoteViews views = new RemoteViews(getPackageName(),
            //                                R.layout.widget_due_date);
           // awm.updateAppWidget(appWidgetId, views);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }
}
