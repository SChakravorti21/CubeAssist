package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by development on 8/17/17.
 */

public class CaptureGrid extends View {

    private  Paint strokePaint;

    public CaptureGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);
        init();
    }

    public CaptureGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);
        init();
    }

    public CaptureGrid(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setWillNotDraw(false);
        init();
    }

    public CaptureGrid(Context context) {
        super(context);

        setWillNotDraw(false);
        init();
    }

    private void init() {
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(6);
    }

    //TODO: implement onDraw to show gridlines for cube alignment

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int centerX = (metrics.widthPixels / 2);
        int centerY = (metrics.heightPixels / 2);

        int cubeSideLength = (metrics.widthPixels * 3 / 5);
        int cubieSideLength = cubeSideLength / 3;
        int startX = centerX - cubeSideLength / 2;
        int startY = centerY - cubeSideLength / 2;

        for (int x = startX; x < startX + cubeSideLength; x += cubieSideLength ) {
            for (int y = startY; x < startY + cubeSideLength; y += cubieSideLength ) {
                canvas.drawRoundRect(x, y,
                        x + cubieSideLength,
                        y + cubeSideLength,
                        cubieSideLength / 5,
                        cubieSideLength / 5,
                        strokePaint);
            }
        }

    }


}
