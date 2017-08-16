package comschakravorti21.github.cubeassist;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements EditScrambleDialog.EditScrambleDialogListener{

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
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        solutionFragment = new TextSolutionFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, solutionFragment, "Text Solution Fragment")
                .addToBackStack(null)
                .commit();

        navDrawerTitles = getResources().getStringArray(R.array.nav_drawer_items);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerList = (NavigationView) findViewById(R.id.left_drawer);
        drawerList.setCheckedItem(R.id.text_scramble);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.open_drawer, R.string.close_drawer) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerList.setNavigationItemSelectedListener(new DrawerClickListener());

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
                    if(!currentMode.equals(TEXT_SCRAMBLE)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new TextSolutionFragment(), "Text Solution Fragment")
                                .addToBackStack(null)
                                .commit();
                        currentMode = TEXT_SCRAMBLE;
                        drawerLayout.closeDrawers();
                    }
                    break;
                case R.id.color_input:
                    if(!currentMode.equals(COLOR_INPUT)) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new ColorInputSolutionFragment(), "Color Input Fragment")
                                .addToBackStack(null)
                                .commit();
                        currentMode = COLOR_INPUT;
                        drawerLayout.closeDrawers();
                    }
                    break;
                case R.id.camera_input:
                    if(!currentMode.equals("Camera Input")) {
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, new CaptureCubeFragment(), "Camera Input Fragment")
//                                .addToBackStack(null)
//                                .commit();
                        Intent intent = new Intent(getApplicationContext(), CaptureCubeActivity.class);
                        startActivity(intent);
                        currentMode = "Camera Input";
                        drawerLayout.closeDrawers();
                    }
                    break;
            }
            //solutionFragment = (TextSolutionFragment)getSupportFragmentManager().findFragmentById(R.id.container);
            return true;
        }
    }

}
