package comschakravorti21.github.cubeassist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import static android.R.attr.width;

/**
 * Created by development on 8/16/17.
 */

public class CaptureCubeActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnClickListener, AdapterView.OnItemSelectedListener{

    CameraPreview preview;
    private Camera camera;
    private boolean hasCamera;
    private SurfaceView transparentView;
    private SurfaceHolder focusHolder;

    private char side;

    //These views are saved for animation purposes
    TextView instructions;
    ImageView toggleInstructions;
    private boolean showingInstructions;
    private int animTime;

    public static Camera getCameraInstance(Context context) {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture_cube);
        hasCamera = checkCameraHardware(this);

        Spinner sideOptions = (Spinner) findViewById(R.id.side_options);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cube_capture_sides, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sideOptions.setAdapter(spinnerAdapter);
        sideOptions.setOnItemSelectedListener(this);

        transparentView = (SurfaceView)findViewById(R.id.grid_view);
        transparentView.setZOrderOnTop(true);
        focusHolder = transparentView.getHolder();
        focusHolder.setFormat(PixelFormat.TRANSPARENT);
        focusHolder.addCallback(this);
        focusHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        instructions = (TextView)findViewById(R.id.instructions);
        toggleInstructions = (ImageView) findViewById(R.id.toggle_instructions);
        showingInstructions = false;
        animTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        instructions.setOnClickListener(this);
        toggleInstructions.setOnClickListener(this);
        findViewById(R.id.instructions_layout).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasCamera && camera == null) {
            camera = getCameraInstance(this);
            preview = new CameraPreview(this, camera);
            FrameLayout container = (FrameLayout) findViewById(R.id.camera_preview_container);
            container.addView(preview, 0
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            //Will prevent the surfaceCreated() callback from taking place
            //and trying to use the released camera
            preview.getHolder().removeCallback(preview);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        Paint strokePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        strokePaint.setStrokeWidth(3);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int centerX = (metrics.widthPixels / 2);
        int centerY = (metrics.heightPixels / 2);

        int cubeSideLength = (metrics.widthPixels * 7 / 10);
        int cubieSideLength = cubeSideLength / 3;
        int startX = centerX - cubeSideLength / 2;
        int startY = centerY - cubeSideLength / 2;

        for (int x = startX; x < startX + cubeSideLength; x += cubieSideLength ) {
            for (int y = startY; y < startY + cubeSideLength; y += cubieSideLength ) {
                canvas.drawRect(x, y,
                        x + cubieSideLength,
                        y + cubieSideLength,
                        strokePaint);
            }
        }

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.instructions_layout
                || id == R.id.instructions
                || id == R.id.toggle_instructions) {
            if(showingInstructions) {
                instructions.animate()
                        .alpha(0f)
                        //.translationY(-instructions.getHeight())
                        .setDuration(animTime)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                instructions.setVisibility(View.INVISIBLE);
                                transparentView.setVisibility(View.VISIBLE);
                            }
                        });

                toggleInstructions.animate()
                        .rotation(0)
                        .setDuration(animTime)
                        .setListener(null);

                showingInstructions = false;
            } else {
                instructions.setAlpha(0f);
                instructions.setVisibility(View.VISIBLE);

                instructions.animate()
                        .alpha(1f)
                        //.translationY(0)
                        .setDuration(animTime)
                        .setListener(null);

                toggleInstructions.animate()
                        .rotation(180)
                        .setDuration(animTime)
                        .setListener(null);

                transparentView.setVisibility(View.INVISIBLE);
                showingInstructions = true;
            }

            return;
        }

        switch (id) {
            case R.id.take_picture: 
                break;
        }
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        char side = adapterView.getItemAtPosition(pos).toString().trim().charAt(0);
        this.side = side;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //  Byte decoder : ---------------------------------------------------------------------
    void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        // Pulled directly from:
        // http://ketai.googlecode.com/svn/trunk/ketai/src/edu/uic/ketai/inputService/KetaiCamera.java
        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }
}
