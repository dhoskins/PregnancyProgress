package co.hosk.pregnancyprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WidgetView extends View {

    public WidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public WidgetView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public WidgetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("widget", "painting, " + canvas.getWidth() + ", " + canvas.getHeight());
        Rect r = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint p = new Paint();
        p.setColor(0xffffff);
        canvas.drawRect(r, p);
    }

}
