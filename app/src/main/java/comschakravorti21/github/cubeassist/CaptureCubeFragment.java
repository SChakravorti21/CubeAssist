package comschakravorti21.github.cubeassist;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class CaptureCubeFragment extends Fragment {

    private View rootView;
    private Camera camera;
    CameraPreview preview;

    public CaptureCubeFragment() {
        // Required empty public constructor
    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_camera_capture_cube, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Context context = getContext();
        if(preview == null && checkCameraHardware(context)) {
            camera = getCameraInstance(context);
            preview = new CameraPreview(context, camera);
            FrameLayout container = rootView.findViewById(R.id.camera_preview_container);
            container.addView(preview, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
            preview.camera = null;
            Log.d("Fragment destroyed", "YES");
        }
    }
}
