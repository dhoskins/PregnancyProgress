package co.hosk.pregnancyprogress;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class DueDateConfig extends Activity {

    private static final long PRAGNACY_DURATION_IN_MILLIS = 40L * 7 * 24 * 60 * 60 * 1000;
    private static final String TimeKey = "time";
    private Settings s;
    private int _AppWidgetId;
    private Intent _Result;
    private long lastDueDate = 0;

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

        final DatePicker conceptionDatePicker = (DatePicker) findViewById(R.id.conceptionDate_datePicker);
        final DatePicker dueDatePicker = (DatePicker) findViewById(R.id.duedate_datePicker);
        initConceptionDatePicker(conceptionDatePicker, dueDatePicker);
        initDueDatePicker(conceptionDatePicker, dueDatePicker);
        // just in case to put dates in sync
        updateDueDatePicker(conceptionDatePicker, dueDatePicker);

        View v = findViewById(R.id.saveButton);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTime(getTimeInMillsByDatePicker(dueDatePicker));
            }
        });


        _Result = new Intent();
        _Result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, _AppWidgetId);
        setResult(RESULT_CANCELED, _Result);
    }

    private void initDueDatePicker(DatePicker conceptionDatePicker, DatePicker dueDatePicker) {
        Calendar cal = Calendar.getInstance();

        // Set a max date of 9 months from today
        cal.add(Calendar.MONTH, 9);
        dueDatePicker.setMaxDate(cal.getTimeInMillis());
        //dueDatePicker.setMinDate(cal.getTimeInMillis() - PRAGNACY_DURATION_IN_MILLIS);

        String time = s.get(TimeKey);
        if (time != null) {
            // Initialise date to what we set it to last time
            cal.setTimeInMillis(Long.parseLong(time, 10));
        } else {
            // Initialise DatePicker to 7 months from today
            cal.add(Calendar.MONTH, -2);
        }

        dueDatePicker.init(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        conceptionDatePicker.setBackgroundColor((int) (Math.random() * 256 * 256 * 256));
                        updateConceptionDatePicker(conceptionDatePicker, dueDatePicker);
                        //  saveTime(getTimeInMillsByDatePicker(dueDatePicker));
                    }
                }
        );

    }

    private void initConceptionDatePicker(DatePicker conceptionDatePicker, DatePicker dueDatePicker) {
        Calendar conceptionDateCalendar = Calendar.getInstance();

        // Set a max date to today
        conceptionDatePicker.setMaxDate(conceptionDateCalendar.getTimeInMillis());
        //conceptionDatePicker.setMinDate(conceptionDateCalendar.getTimeInMillis() - PRAGNACY_DURATION_IN_MILLIS);


        // Initialise date to what we set it to last time
        String time = s.get(TimeKey);
        if (time != null) {
            // Initialise date to what we set it to last time
            long conceptionDateMillis = Long.parseLong(time, 10) - PRAGNACY_DURATION_IN_MILLIS;
            conceptionDateCalendar.setTimeInMillis(conceptionDateMillis);
        } else {
            // Initialise DatePicker to 2 months to today
            conceptionDateCalendar.add(Calendar.MONTH, -2);
        }

        conceptionDatePicker.init(conceptionDateCalendar.get(Calendar.YEAR),
                conceptionDateCalendar.get(Calendar.MONTH),
                conceptionDateCalendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        dueDatePicker.setBackgroundColor((int) (Math.random() * 256 * 256 * 256));
                        updateDueDatePicker(conceptionDatePicker, dueDatePicker);
                        //saveTime(getTimeInMillsByDatePicker(dueDatePicker));
                    }
                }
        );
    }

    private void updateConceptionDatePicker(DatePicker conceptionDatePicker, DatePicker dueDatePicker) {
        long dueDateInMillis = getTimeInMillsByDatePicker(dueDatePicker);
        long conceptionDateInMillis = dueDateInMillis - PRAGNACY_DURATION_IN_MILLIS;
        if (lastDueDate != dueDateInMillis) {
            lastDueDate = dueDateInMillis;
            conceptionDatePicker.getCalendarView().setDate(conceptionDateInMillis);
        }
    }

    private void updateDueDatePicker(DatePicker conceptionDatePicker, DatePicker dueDatePicker) {
        long conceptionDateInMillis = getTimeInMillsByDatePicker(conceptionDatePicker);
        long dueDateInMillis = conceptionDateInMillis + PRAGNACY_DURATION_IN_MILLIS;
        if (lastDueDate != dueDateInMillis){
            lastDueDate = dueDateInMillis;
            dueDatePicker.getCalendarView().setDate(dueDateInMillis);
        }
    }

    private long getTimeInMillsByDatePicker(DatePicker dp) {
        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year = dp.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        long time = c.getTimeInMillis();
        return time;
    }

    private void saveTime(long time) {
        s.put(TimeKey, String.valueOf(time));

        final AppWidgetManager awm = AppWidgetManager.getInstance(this);
        DueDateWidget.updateAppWidget(this, awm, _AppWidgetId);
        setResult(RESULT_OK, _Result);
        finish();
    }
}
