package comschakravorti21.github.cubeassist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;


public class ColorInputSolutionFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private GridLayout palette;
    private GridLayout userInputField;

    private char colorSelected;
    private char sideChosen;
    private char[][][] colorsInputted;
    private String[] instructionColors;

    public ColorInputSolutionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_side:
                previousSide();
                break;
            case R.id.next_side:
                nextSide();
                break;
            case R.id.generate_solution:
                TextSolutionFragment fragment = new TextSolutionFragment();
                Bundle args = new Bundle();
                args.putString(TextSolutionFragment.INITIAL_INPUT_TYPE,
                        TextSolutionFragment.MANUAL_COLOR_INPUT);
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_LEFT,
                        packageSide(getIndexOfSide('L')));
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_RIGHT,
                        packageSide(getIndexOfSide('R')));
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_UP,
                        packageSide(getIndexOfSide('U')));
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_DOWN,
                        packageSide(getIndexOfSide('D')));
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_FRONT,
                        packageSide(getIndexOfSide('F')));
                args.putCharArray(TextSolutionFragment.COLORS_INPUTTED_BACK,
                        packageSide(getIndexOfSide('B')));
                fragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, "Colors Inputted")
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        colorSelected = 'B'; //Set blue as the initial default color
        sideChosen = 'U';
        colorsInputted = new char[6][3][3];
        resetCubeInputs();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((Activity)getContext()).getMenuInflater().inflate(R.menu.menu_color_solution, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_color_input, container, false);
        rootView.findViewById(R.id.previous_side).setOnClickListener(this);
        rootView.findViewById(R.id.next_side).setOnClickListener(this);
        rootView.findViewById(R.id.generate_solution).setOnClickListener(this);

        palette = rootView.findViewById(R.id.palette);
        userInputField = rootView.findViewById(R.id.user_input_field);

        Button selectBlue = rootView.findViewById(R.id.select_blue);
        selectBlue.setActivated(true);

        repaintSide();

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetCubeInputs();
                repaintSide();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PaletteButtonClickListener paletteButtonClickListener = new PaletteButtonClickListener();
        for (int i = palette.getChildCount() - 1; i >= 0; i--) {
            palette.getChildAt(i).setOnTouchListener(paletteButtonClickListener);
        }

        for (int i = userInputField.getChildCount() - 1; i >= 0; i--) {
            Button selectionButton = (Button) userInputField.getChildAt(i);
            //Set a position tag to that we can access it later for row and column clicked
            selectionButton.setTag("" + i);

            selectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = Integer.parseInt((String) view.getTag());
                    int row = position / 3;
                    int col = position % 3;

                    if (!(row == 1 && col == 1)) { //Do not allow for changing of center color
                        switch (colorSelected) {
                            case 'B':
                                view.setBackgroundResource(R.drawable.cube_button_blue);
                                break;
                            case 'G':
                                view.setBackgroundResource(R.drawable.cube_button_green);
                                break;
                            case 'R':
                                view.setBackgroundResource(R.drawable.cube_button_red);
                                break;
                            case 'O':
                                view.setBackgroundResource(R.drawable.cube_button_orange);
                                break;
                            case 'W':
                                view.setBackgroundResource(R.drawable.cube_button_white);
                                break;
                            case 'Y':
                                view.setBackgroundResource(R.drawable.cube_button_yellow);
                                break;
                        }

                        colorsInputted[getIndexOfSide(sideChosen)][row][col] = colorSelected;
                    }
                }
            });
        }
    }


    /**
     * Gets the index for colorsInputted[(index here)] that corresponds to the side currently being painted when in color
     * selection mode. Helper method for paintComponent().
     * @param side
     * @return index
     */
    private int getIndexOfSide(char side) {
        switch(side) {
            case('L'): return 0;
            case('U'): return 1;
            case('F'): return 2;
            case('B'): return 3;
            case('R'): return 4;
            default: return 5; //case 'D'
        }
    }

    private char getSideOfIndex(int index) {
        switch(index) {
            case 0: return 'L';
            case 1: return 'U';
            case 2: return 'F';
            case 3: return 'B';
            case 4: return 'R';
            default: return 'D'; //case '5'
        }
    }

    private void repaintSide() {
        int index = getIndexOfSide(sideChosen);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button selectionButton = (Button)userInputField.getChildAt(i*3+j);
                switch (colorsInputted[index][i][j]) {
                    case 'B':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_blue);
                        break;
                    case 'G':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_green);
                        break;
                    case 'R':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_red);
                        break;
                    case 'O':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_orange);
                        break;
                    case 'W':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_white);
                        break;
                    case 'Y':
                        selectionButton.setBackgroundResource(R.drawable.cube_button_yellow);
                        break;
                }
            }
        }

        updateInstructions();
    }

    /**
     * Resets the colors inputted in color selection mode to the colors of a cube in its solved state.
     */
    public void resetCubeInputs() {
        for(int i = 0; i<3; i++) {
            Arrays.fill(colorsInputted[0][i], 'R');
            Arrays.fill(colorsInputted[1][i], 'Y');
            Arrays.fill(colorsInputted[2][i], 'G');
            Arrays.fill(colorsInputted[3][i], 'B');
            Arrays.fill(colorsInputted[4][i], 'O');
            Arrays.fill(colorsInputted[5][i], 'W');
        }
    }

    private void nextSide() {
        int currentIndex = getIndexOfSide(sideChosen);
        if(currentIndex < 5) {
            sideChosen = getSideOfIndex(currentIndex+1);
            repaintSide();
        }
    }

    private char[] packageSide(int side) {
        char[] packageArray = new char[9];
        int index = side;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                packageArray[i*3 + j] = colorsInputted[index][i][j];
            }
        }
        return packageArray;
    }

    private void previousSide() {
        int currentIndex = getIndexOfSide(sideChosen);
        if(currentIndex > 0) {
            sideChosen = getSideOfIndex(currentIndex-1);
            repaintSide();
        }
    }

    private void updateInstructions() {
        String[] colors = new String[3];
        colors[0] = "TOP: ";
        colors[1] = "BACK: ";
        colors[2] = "FRONT: ";

        switch(sideChosen) {
            case('L'):
                colors[0] += "\tRed";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case('U'):
                colors[0] += "\tYellow";
                colors[1] += "\tBlue";
                colors[2] += "\tGreen";
                break;
            case('F'):
                colors[0] += "\tGreen";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case('B'):
                colors[0] += "\tBlue";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case('R'):
                colors[0] += "\tOrange";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case('D'):
                colors[0] += "White";
                colors[1] += "Green";
                colors[2] += "Blue";
                break;
        }

        TextView topColor = rootView.findViewById(R.id.top_color);
        topColor.setText(colors[0]);

        TextView backColor = rootView.findViewById(R.id.back_color);
        backColor.setText(colors[1]);

        TextView frontColor = rootView.findViewById(R.id.front_color);
        frontColor.setText(colors[2]);

    }


    private class PaletteButtonClickListener implements View.OnTouchListener {
        Button previousSelection = rootView.findViewById(R.id.select_blue);

        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (previousSelection != null) {
                previousSelection.setActivated(false);
            }

            switch (view.getId()) {
                case R.id.select_blue:
                    colorSelected = 'B';
                    break;
                case R.id.select_green:
                    colorSelected = 'G';
                    break;
                case R.id.select_orange:
                    colorSelected = 'O';
                    break;
                case R.id.select_red:
                    colorSelected = 'R';
                    break;
                case R.id.select_white:
                    colorSelected = 'W';
                    break;
                case R.id.select_yellow:
                    colorSelected = 'Y';
                    break;
            }

            if(view instanceof Button) {
                previousSelection = (Button) view;
            }
            previousSelection.setActivated(true);

            return true;
        }
    }
}
