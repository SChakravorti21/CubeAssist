package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by development on 7/28/17.
 */

public class CubeView extends View {

    Cube cube;
    Paint bluePaint;
    Paint greenPaint;
    Paint redPaint;
    Paint orangePaint;
    Paint whitePaint;
    Paint yellowPaint;

    Paint strokePaint;

    int cubieSize;
    int gap;

    public CubeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        invalidate();
    }

    private void init() {
        cube = new Cube();

        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(Color.parseColor("#03A9F4"));

        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.parseColor("#FF11CF31"));

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(Color.parseColor("#FFE70000"));

        orangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setColor(Color.parseColor("#FFFE7D15"));

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.parseColor("#FFFEFEFE"));

        yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(Color.parseColor("#FFFCD51E"));

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(6);

        cubieSize = (int)dpToPx(22);
        gap = (int)dpToPx(4);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        destroyDrawingCache();
        super.onDraw(canvas);

        //Paint Reds
        int xVal = gap*5;
        int yVal = (cubieSize + gap)*3 + gap*20;
        for(int y = 2; y>=0; y--) {
            for(int z = 2; z>=0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(0, y, z, 'L')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        //Paint Yellows
        xVal += (cubieSize + gap)*3 + gap;
        for(int x = 0; x<=2; x++) {
            for(int y = 2; y>=0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, y, 0, 'U')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        //Paint Blues
        yVal -= (cubieSize + gap)*3 + gap;
        for(int x = 0; x<=2; x++) {
            for(int z = 2; z>=0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, 2, z, 'B')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        //Paint Greens
        yVal += ((cubieSize + gap)*3 + gap)*2;
        for(int z = 0; z<=2; z++) {
            for(int x = 0; x<=2; x++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, 0, z, 'F')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        //Paint Oranges
        xVal += (cubieSize + gap)*3 + gap;
        yVal -= (cubieSize + gap)*3 + gap;
        for(int y = 2; y>=0; y--) {
            for(int z = 0; z<=2; z++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(2, y, z, 'R')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

        //Paint Whites
        xVal += (cubieSize + gap)*3 + gap;
        for(int x = 2; x>=0; x--) {
            for(int y = 2; y>=0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(x-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        colorToPaint(cube.getColor(x, y, 2, 'D')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(x-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize / 5,
                        cubieSize / 5,
                        strokePaint);
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private Paint colorToPaint(char color) {
        switch(color) {
            case 'W':
                return whitePaint;
            case 'Y':
                return yellowPaint;
            case 'R':
                return redPaint;
            case 'O':
                return orangePaint;
            case 'B':
                return bluePaint;
            case 'G':
                return greenPaint;
        }
        return strokePaint;
    }
}
