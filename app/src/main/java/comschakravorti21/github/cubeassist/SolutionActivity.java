package comschakravorti21.github.cubeassist;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SolutionActivity extends AppCompatActivity implements View.OnClickListener,
        EditScrambleDialog.EditScrambleDialogListener{

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

    public void updateMoves(String movesToPerform, String movesPerformed, String phase) {
        TextView toPerformView = (TextView)findViewById(R.id.moves_to_perform);
        toPerformView.setText(movesToPerform);
        TextView performedView = (TextView)findViewById(R.id.moves_performed);
        performedView.setText(movesPerformed);
        TextView phaseView = (TextView)findViewById(R.id.phase_view);
        phaseView.setText(phase);

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
            case R.id.action_edit_scramble:
                Bundle args = new Bundle();
                args.putString(EditScrambleDialog.SCRAMBLE_TAG,
                        ((TextView)findViewById(R.id.scramble_view)).getText().toString());
                EditScrambleDialog dialog = new EditScrambleDialog();
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "edit scramble");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip_forward:
                cubeView.skipToPhase(cubeView.getPhase()+1);
                break;
            case R.id.rewind:
                cubeView.skipToPhase(cubeView.getPhase()-1);
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String scramble) {
        TextView scrambleView = (TextView)findViewById(R.id.scramble_view);
        scrambleView.setText(scramble);
        cubeView.resetScramble(scramble);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
