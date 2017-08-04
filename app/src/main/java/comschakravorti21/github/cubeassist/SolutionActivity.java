package comschakravorti21.github.cubeassist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SolutionActivity extends AppCompatActivity {

    CubeView cubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayOptions(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        cubeView = (CubeView)findViewById(R.id.cube_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solution, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void updateMoves(String movesToPerform, String movesPerformed) {
        TextView toPerformView = (TextView)findViewById(R.id.moves_to_perform);
        toPerformView.setText(movesToPerform);
        TextView performedView = (TextView)findViewById(R.id.moves_performed);
        performedView.setText(movesPerformed);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_random:
                TextView scrambleView = (TextView)findViewById(R.id.scramble_view);
                scrambleView.setText(cubeView.randScramble());
                break;
            case R.id.action_reset:
                cubeView.resetCurrentScramble();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
