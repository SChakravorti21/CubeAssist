package comschakravorti21.github.cubeassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SolutionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        CubeView view = (CubeView)findViewById(R.id.cube_view);
        view.onViewCreated();
    }

    public void updateMoves(String movesToPerform, String movesPerformed) {
        TextView toPerformView = (TextView)findViewById(R.id.moves_to_perform);
        toPerformView.setText(movesToPerform);
        TextView performedView = (TextView)findViewById(R.id.moves_performed);
        performedView.setText(movesPerformed);
    }
}
