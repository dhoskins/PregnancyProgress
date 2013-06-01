package co.hosk.pregnancyprogress;

import java.util.Calendar;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

public class DueDateConfig extends Activity {

    private static final String TimeKey = "time";
    private Settings s;
    private int _AppWidgetId;
    private Intent _Result;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            _AppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            
        }
        setContentView(R.layout.activity_due_date_entry);
        s = new Settings(this);
        
        final DatePicker dp = (DatePicker)findViewById(R.id.datePicker);
        Calendar cal = Calendar.getInstance();
        
        // Set a max date of 9 months from today
        cal.add(Calendar.MONTH, 9);
        dp.setMaxDate(cal.getTimeInMillis());
        
        String time = s.get(TimeKey);
        if (time != null) {
            // Initialise date to what we set it to last time
            cal.setTimeInMillis(Long.parseLong(time, 10));
        } else {
            // Initialise DatePicker to 7 months from today
            cal.add(Calendar.MONTH, -2);
        }
        
        dp.init(cal.get(Calendar.YEAR), 
                cal.get(Calendar.MONTH), 
                cal.get(Calendar.DAY_OF_MONTH), 
                null);
        
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
        _Result = new Intent();
        _Result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _AppWidgetId);
        setResult(RESULT_CANCELED, _Result);
    }

    private void saveTime(long time) {
        s.put(TimeKey, String.valueOf(time));
        
        final AppWidgetManager awm = AppWidgetManager.getInstance(this);
        DueDateWidget.updateAppWidget(this, awm, _AppWidgetId);
        setResult(RESULT_OK, _Result);
        finish();
    }
}
