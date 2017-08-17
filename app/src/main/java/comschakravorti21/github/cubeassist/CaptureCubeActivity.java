package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import static android.widget.ArrayAdapter.createFromResource;

/**
 * Created by development on 8/16/17.
 */

public class CaptureCubeActivity extends AppCompatActivity {

    private Camera camera;
    CameraPreview preview;

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(Context context) {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture_cube);

        Spinner sideOptions = (Spinner)findViewById(R.id.side_options);
        ArrayAdapter<CharSequence> spiinerAdapter = ArrayAdapter.createFromResource(this,
                R.array.cube_capture_sides, android.R.layout.simple_spinner_dropdown_item);
        spiinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideOptions.setAdapter(spiinerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(preview == null && checkCameraHardware(this)) {
            camera = getCameraInstance(this);
            preview = new CameraPreview(this, camera);
            FrameLayout container = (FrameLayout)findViewById(R.id.camera_preview_container);
            container.addView(preview, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            preview.camera = null;
            Log.d("Fragment destroyed", "YES");
        }
    }
}
