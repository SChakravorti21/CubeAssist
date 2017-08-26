package comschakravorti21.github.cubeassist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import static comschakravorti21.github.cubeassist.MainActivity.CAMERA_INPUT;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_BACK;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_DOWN;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_FRONT;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_LEFT;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_RIGHT;
import static comschakravorti21.github.cubeassist.MainActivity.COLORS_INPUTTED_UP;
import static comschakravorti21.github.cubeassist.MainActivity.INITIAL_INPUT_TYPE;
import static comschakravorti21.github.cubeassist.MainActivity.MANUAL_COLOR_INPUT;
import static comschakravorti21.github.cubeassist.R.id.container;


public class ColorInputFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private GridLayout palette;
    private GridLayout userInputField;

    private char colorSelected;
    private char sideChosen;
    private char[][][] colorsInputted;
    private int animTime;
    //private String[] instructionColors;

    public ColorInputFragment() {
        // Required empty public constructor
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
            case R.id.rotate_left:
                rotateMatrix(colorsInputted[getIndexOfSide(sideChosen)],
                        -90,
                        getIndexOfSide(sideChosen));

                userInputField.animate()
                        .rotation(-90)
                        .setDuration(animTime)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                userInputField.setRotation(0);
                                repaintSide();
                            }
                        });
                break;
            case R.id.rotate_right:
                rotateMatrix(colorsInputted[getIndexOfSide(sideChosen)],
                        90,
                        getIndexOfSide(sideChosen));

                userInputField.animate()
                        .rotation(90)
                        .setDuration(animTime)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                userInputField.setRotation(0);
                                repaintSide();
                            }
                        });
                break;
            case R.id.generate_solution:
                TextSolutionFragment fragment = new TextSolutionFragment();
                Bundle args = new Bundle();
                args.putString(INITIAL_INPUT_TYPE,
                        MANUAL_COLOR_INPUT);

                args.putCharArray(COLORS_INPUTTED_LEFT,
                        packageSide(getIndexOfSide('L')));

                args.putCharArray(COLORS_INPUTTED_RIGHT,
                        packageSide(getIndexOfSide('R')));

                args.putCharArray(COLORS_INPUTTED_UP,
                        packageSide(getIndexOfSide('U')));

                args.putCharArray(COLORS_INPUTTED_DOWN,
                        packageSide(getIndexOfSide('D')));

                args.putCharArray(COLORS_INPUTTED_FRONT,
                        packageSide(getIndexOfSide('F')));

                args.putCharArray(COLORS_INPUTTED_BACK,
                        packageSide(getIndexOfSide('B')));
                fragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(container, fragment, "Colors Inputted")
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
        if(colorsInputted == null) {
            resetCubeInputs();
        }
        animTime = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_color_input, container, false);
        rootView.findViewById(R.id.previous_side).setOnClickListener(this);
        rootView.findViewById(R.id.next_side).setOnClickListener(this);
        rootView.findViewById(R.id.generate_solution).setOnClickListener(this);
        rootView.findViewById(R.id.rotate_left).setOnClickListener(this);
        rootView.findViewById(R.id.rotate_right).setOnClickListener(this);

        palette = rootView.findViewById(R.id.palette);
        userInputField = rootView.findViewById(R.id.user_input_field);

        Button selectBlue = rootView.findViewById(R.id.select_blue);
        selectBlue.setActivated(true);

        repaintSide();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View.OnTouchListener paletteButtonClickListener = new View.OnTouchListener() {
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

                if (view instanceof Button) {
                    previousSelection = (Button) view;
                }
                previousSelection.setActivated(true);

                return true;
            }
        };

        for (int i = palette.getChildCount() - 1; i >= 0; i--) {
            palette.getChildAt(i).setOnTouchListener(paletteButtonClickListener);
        }

        View.OnClickListener userFieldClickListener = new View.OnClickListener() {
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
        };

        for (int i = userInputField.getChildCount() - 1; i >= 0; i--) {
            Button selectionButton = (Button) userInputField.getChildAt(i);
            //Set a position tag to that we can access it later for row and column clicked
            selectionButton.setTag("" + i);

            selectionButton.setOnClickListener(userFieldClickListener);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((Activity) getContext()).getMenuInflater().inflate(R.menu.menu_color_input, menu);
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


    /**
     * Gets the index for colorsInputted[(index here)] that corresponds to the side currently being painted when in color
     * selection mode. Helper method for paintComponent().
     *
     * @param side
     * @return index
     */
    private int getIndexOfSide(char side) {
        switch (side) {
            case ('L'):
                return 0;
            case ('U'):
                return 1;
            case ('F'):
                return 2;
            case ('B'):
                return 3;
            case ('R'):
                return 4;
            default:
                return 5; //case 'D'
        }
    }

    private char getSideOfIndex(int index) {
        switch (index) {
            case 0:
                return 'L';
            case 1:
                return 'U';
            case 2:
                return 'F';
            case 3:
                return 'B';
            case 4:
                return 'R';
            default:
                return 'D'; //case '5'
        }
    }

    private void repaintSide() {
        int index = getIndexOfSide(sideChosen);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button selectionButton = (Button) userInputField.getChildAt(i * 3 + j);
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
    private void resetCubeInputs() {
        colorsInputted = new char[6][3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(colorsInputted[0][i], 'R');
            Arrays.fill(colorsInputted[1][i], 'Y');
            Arrays.fill(colorsInputted[2][i], 'G');
            Arrays.fill(colorsInputted[3][i], 'B');
            Arrays.fill(colorsInputted[4][i], 'O');
            Arrays.fill(colorsInputted[5][i], 'W');
        }
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null) {
            Log.d("Bundle null", "FLASE");
            String initInputType = args.getString(INITIAL_INPUT_TYPE);
            if (initInputType != null && initInputType.equals(CAMERA_INPUT)) {
                Log.d("CAMERA INPUT", "TRUEE");
                colorsInputted = new char[6][][];
                colorsInputted[0] = unpackArrays(args.getCharArray(COLORS_INPUTTED_LEFT));
                colorsInputted[1] = unpackArrays(args.getCharArray(COLORS_INPUTTED_UP));
                colorsInputted[2] = unpackArrays(args.getCharArray(COLORS_INPUTTED_FRONT));
                colorsInputted[3] = unpackArrays(args.getCharArray(COLORS_INPUTTED_BACK));
                colorsInputted[4] = unpackArrays(args.getCharArray(COLORS_INPUTTED_RIGHT));
                colorsInputted[5] = unpackArrays(args.getCharArray(COLORS_INPUTTED_DOWN));
            }
        } else {
            Log.d("Bundle null", "TRUEE");
        }
    }

    /**
     * Rotates a given 2D matrix as specified by {@code degrees}, where {@code degrees}
     * can either be 90, indicating a clockwise rotation, or -90, indicating a counterclockwise
     * rotation. {@code postChange} dictates how the direction of a cubie's colors should
     * change after the rotation, the original directions being denoted by {@code preChange}.
     *
     * @param orig       the original matrix
     * @param degrees    degrees by which to rotate, can be 90 or -90
     * @return the rotated matrix
     */
    private void rotateMatrix(char[][] orig, int degrees, int side) {
        char[][] rotated = new char[3][3];
        if (degrees == 90) {
            //Transpose the matrix
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotated[i][j] = orig[j][i];
                }
            }
            //Reverse all the rows
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < rotated[0].length / 2; j++) {
                    char temp = rotated[i][3 - j - 1];
                    rotated[i][3 - j - 1] = rotated[i][j];
                    rotated[i][j] = temp;
                }
            }
        } else if (degrees == -90) {
            //Transpose the matrix
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rotated[i][j] = orig[j][i];
                }
            }

            //Reverse all the columns
            for (int i = 0; i < rotated[0].length / 2; i++) {
                for (int j = 0; j < 3; j++) {
                    char temp= rotated[3 - i - 1][j];
                    rotated[3 - i - 1][j] = rotated[i][j];
                    rotated[i][j] = temp;
                }
            }
        }

        colorsInputted[side] = rotated;
    }

    private void nextSide() {
        int currentIndex = getIndexOfSide(sideChosen);
        if (currentIndex < 5) {
            sideChosen = getSideOfIndex(currentIndex + 1);
            repaintSide();
        }
    }

    private char[] packageSide(int side) {
        char[] packageArray = new char[9];
        int index = side;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                packageArray[i * 3 + j] = colorsInputted[index][i][j];
            }
        }
        return packageArray;
    }

    private char[][] unpackArrays(char[] colorsArray) {
        char[][] unpackedArray = new char[3][3];
        for (int i = 0; i < colorsArray.length; i++) {
            unpackedArray[i / 3][i % 3] = colorsArray[i];
        }
        return unpackedArray;
    }

    private void previousSide() {
        int currentIndex = getIndexOfSide(sideChosen);
        if (currentIndex > 0) {
            sideChosen = getSideOfIndex(currentIndex - 1);
            repaintSide();
        }
    }

    private void updateInstructions() {
        String[] colors = new String[3];
        colors[0] = "TOP: ";
        colors[1] = "BACK: ";
        colors[2] = "FRONT: ";

        switch (sideChosen) {
            case ('L'):
                colors[0] += "\tRed";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case ('U'):
                colors[0] += "\tYellow";
                colors[1] += "\tBlue";
                colors[2] += "\tGreen";
                break;
            case ('F'):
                colors[0] += "\tGreen";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case ('B'):
                colors[0] += "\tBlue";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case ('R'):
                colors[0] += "\tOrange";
                colors[1] += "\tYellow";
                colors[2] += "\tWhite";
                break;
            case ('D'):
                colors[0] += "\tWhite";
                colors[1] += "\tGreen";
                colors[2] += "\tBlue";
                break;
        }

        TextView topColor = rootView.findViewById(R.id.top_color);
        topColor.setText(colors[0]);

        TextView backColor = rootView.findViewById(R.id.back_color);
        backColor.setText(colors[1]);

        TextView frontColor = rootView.findViewById(R.id.front_color);
        frontColor.setText(colors[2]);

    }

}
