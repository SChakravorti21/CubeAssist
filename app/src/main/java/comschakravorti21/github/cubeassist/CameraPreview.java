package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by development on 8/15/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,
        Camera.PreviewCallback {

    protected Camera camera;
    Size previewSize;
    //private boolean surfaceWasDestroyed;
    List<Size> supportedPreviewSizes;
    private SurfaceHolder surfaceHolder;
    private Paint strokePaint;

    private byte[] data;
    private int[][] previewPixels;
    private Bitmap[] previewBitmaps;
    int camImageWidth, camImageHeight;

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

        previewBitmaps = new Bitmap[6];
        previewPixels = new int[6][];
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

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        camImageWidth = params.getPreviewSize().width;
        camImageHeight = params.getPreviewSize().height;

        this.data = data;
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

            camera.setPreviewCallback(this);
            invalidate();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void saveCurrentBitmap(int side) {
        Log.d("Width", "" + camImageWidth);
        Log.d("Height", "" + camImageHeight);

        //The decodeYUV420 way
//        previewPixels[side] = decodeYUV420SP(data, camImageWidth, camImageHeight);
//        Bitmap bitmap = Bitmap.createBitmap(previewPixels[side], camImageWidth,
//                camImageHeight, Bitmap.Config.ARGB_8888);
//

        //The RenderScript way
        Bitmap bitmap2 = Bitmap.createBitmap(camImageWidth, camImageHeight, Bitmap.Config.ARGB_8888);
        Allocation bmData = renderScriptNV21ToRGBA888(
                getContext(),
                camImageWidth,
                camImageHeight,
                data);
        Log.d("Data null", "" + (data == null));
        Log.d("Bitmap null", "" + (bitmap2 == null));
        Log.d("Side", "" + side);
        bmData.copyTo(bitmap2);
        previewBitmaps[side] = bitmap2;

        Toast.makeText(getContext(), "Picture captured! Select another side.",
                Toast.LENGTH_SHORT).show();
    }

    public boolean resolveColors(int centerX, int centerY, int startX, int startY,
                         int cubeSideLength, int cubieSideLength) {
        for (int i = 0; i < previewBitmaps.length; i++) {
            if(previewBitmaps[i] == null) {
                Toast.makeText(getContext(), "Please capture all six sides first",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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

    //  Byte decoder : ---------------------------------------------------------------------
    private int[] decodeYUV420SP( byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;
        int rgb[] = new int[width*height];

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0) r = 0; else if (r > 262143) r = 262143;
                if (g < 0) g = 0; else if (g > 262143) g = 262143;
                if (b < 0) b = 0; else if (b > 262143) b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }

        return rgb;
    }

    private Allocation renderScriptNV21ToRGBA888(Context context, int width, int height, byte[] nv21) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));

        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

        in.copyFrom(nv21);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        return out;
    }
}
