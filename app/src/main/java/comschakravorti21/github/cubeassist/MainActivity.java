package comschakravorti21.github.cubeassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout container = (FrameLayout)findViewById(R.id.container);
        CubeView view = new CubeView(getApplicationContext(), null);
        container.addView(view);
        view.destroyDrawingCache();
        view = null;

    }
}
