package comschakravorti21.github.cubeassist;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import static android.R.attr.inputType;
import static java.util.ResourceBundle.getBundle;

public class MainActivity extends AppCompatActivity implements EditScrambleDialog.EditScrambleDialogListener {

    public static final String INITIAL_INPUT_TYPE = "initial input type";
    public static final String MANUAL_COLOR_INPUT = "manual color input";
    public static final String CAMERA_INPUT = "camera input";
    public static final String ALL_COLORS_INPUTTED = "all colors inputted";
    public static final String COLORS_INPUTTED_LEFT = "colors inputted left";
    public static final String COLORS_INPUTTED_UP = "colors inputted up";
    public static final String COLORS_INPUTTED_FRONT = "colors inputted front";
    public static final String COLORS_INPUTTED_BACK = "colors inputted back";
    public static final String COLORS_INPUTTED_RIGHT = "colors inputted right";
    public static final String COLORS_INPUTTED_DOWN = "colors inputted down";

    private final String TEXT_SCRAMBLE = "text scramble";
    private final String COLOR_INPUT = "color input";
    private String currentMode = TEXT_SCRAMBLE;
    private TextSolutionFragment solutionFragment;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView drawerList;
    private String[] navDrawerTitles;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (NavigationView) findViewById(R.id.left_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerList.setNavigationItemSelectedListener(new DrawerClickListener());


        Intent intent = getIntent();
        if(intent != null) {
            Bundle extras = intent.getExtras();
            Bundle args = (extras != null) ? extras.getBundle(ALL_COLORS_INPUTTED) : null;
            String inputType = (args != null) ? args.getString(INITIAL_INPUT_TYPE) : " ";

            if (inputType != null && inputType.equals(CAMERA_INPUT)) {
                Log.d("Starting Color Input", "true");
                ColorInputFragment colorInputFragment = new ColorInputFragment();

                if(args != null) {
                    colorInputFragment.setArguments(args);
                } else {
                    Log.d("Colors are null", "TRUE");
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, colorInputFragment, "Color Input Fragment")
                        .addToBackStack(null)
                        .commit();
                drawerList.setCheckedItem(R.id.color_input);
            } else {
                solutionFragment = new TextSolutionFragment();

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, solutionFragment, "Text Solution Fragment")
                        .addToBackStack(null)
                        .commit();
                drawerList.setCheckedItem(R.id.text_scramble);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String scramble) {
        solutionFragment.onDialogPositiveClick(dialog, scramble);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.text_scramble:
                    if (!currentMode.equals(TEXT_SCRAMBLE)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new TextSolutionFragment(), "Text Solution Fragment")
                                .addToBackStack(null)
                                .commit();
                        currentMode = TEXT_SCRAMBLE;
                        drawerLayout.closeDrawers();
                    }
                    break;
                case R.id.color_input:
                    if (!currentMode.equals(COLOR_INPUT)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new ColorInputFragment(), "Color Input Fragment")
                                .addToBackStack(null)
                                .commit();
                        currentMode = COLOR_INPUT;
                        drawerLayout.closeDrawers();
                    }
                    break;
                case R.id.camera_input:
                    if (!currentMode.equals("Camera Input")) {
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, new CaptureCubeFragment(), "Camera Input Fragment")
//                                .addToBackStack(null)
//                                .commit();
                        Intent intent = new Intent(getApplicationContext(), CaptureCubeActivity.class);
                        startActivity(intent);
                        currentMode = "Camera Input";
                        drawerList.setCheckedItem(R.id.color_input);
                        drawerLayout.closeDrawers();
                    }
                    break;
            }
            //solutionFragment = (TextSolutionFragment)getSupportFragmentManager().findFragmentById(R.id.container);
            return true;
        }
    }

}
