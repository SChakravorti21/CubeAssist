package comschakravorti21.github.cubeassist;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import static android.R.attr.fragment;

public class TestActivity extends AppCompatActivity implements EditScrambleDialog.EditScrambleDialogListener{

    TextSolutionFragment textSolutionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textSolutionFragment = new TextSolutionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, textSolutionFragment, "Text Solution Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solution, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FrameLayout container = (FrameLayout)findViewById(R.id.container);
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
        if(frag instanceof TextSolutionFragment) {
            frag.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String scramble) {
        TextView scrambleView = (TextView)findViewById(R.id.scramble_view);
        scrambleView.setText(scramble);
        textSolutionFragment.cubeView.resetScramble(scramble);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
