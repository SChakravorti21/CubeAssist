package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by development on 8/15/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    protected Camera camera;
    Size previewSize;
    //private boolean surfaceWasDestroyed;
    List<Size> supportedPreviewSizes;
    private SurfaceHolder surfaceHolder;
    private Paint strokePaint;

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

    public CameraPreview(Context context, Camera camera) {
        super(context);
        Log.d("Surface Constructed", "TRUE");
        this.camera = camera;
        supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(6);
        setWillNotDraw(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("Surface Measured", "TRUE");
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (supportedPreviewSizes != null) {
            previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, height);
        }
    }

    /* Callback that is called when the surface is created or orientation changes */
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.d("Surface Created", "TRUE");
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.d("Surface Destroyed", "TRUE");
        //Stop Preview if user exited application but parent fragment was not destroyed
//        if (camera != null) {
//            camera.stopPreview();
//        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            Log.d("Surface Changed", "FALSE");
            return;
        } else {
            Log.d("Surface Changed", "TRUE");
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Log.d(TAG, "width: " + previewSize.width);
        Log.d(TAG, "height: " + previewSize.height);

        camera.setParameters(parameters);

        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            invalidate();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}
