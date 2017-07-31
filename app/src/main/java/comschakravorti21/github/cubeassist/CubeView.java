package comschakravorti21.github.cubeassist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by development on 7/28/17.
 */

public class CubeView extends View {

    private Timer frameTimer;
    public final static int DELAY = 1500;

    private Cube cube = new Cube();
    //Default scramble
    private final String DEFAULT_SCRAMBLE = "F2 D' B U' D L2 B2 R B L' B2 L2 B2 D' R2 F2 D' R2 U' ";
    private String scramble = new String(DEFAULT_SCRAMBLE),
            sunflower = new String(), whiteCross = new String(),
            whiteCorners = new String(), secondLayer = new String(),
            yellowCross = new String(), OLL = new String(), PLL = new String();
    private String movesToPerform = new String(), movesPerformed = new String();

    /*
     * Respective stages of the solution w.r.t the phase variable
     * 0 = sunflower
     * 1 = whiteCross
     * 2 = whiteCorners
     * 3 = secondLayer
     * 4 = yellowCross
     * 5 = OLL
     * 6 = PLL
     * The phase is updated in updatePhase() to reflect the stage at which the solution is
     */
    private int phase = 0;
    private String phaseString;
    //Helps keep track of moves to perform, and allows for painting of moves
    private int movesIndex = 0;

    private Paint bluePaint;
    private Paint greenPaint;
    private Paint redPaint;
    private Paint orangePaint;
    private Paint whitePaint;
    private Paint yellowPaint;

    private Paint strokePaint;

    private int cubieSize;
    private int gap;


    public CubeView(Context context) {
        super(context);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWillNotDraw(false);
        init();
    }

    public CubeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        cube = new Cube();
        resetScramble("R2 F2 R2 D2 B' R2 F L2 D2 F D2 U' B2 R2 B' D2 U' L' U' ");

        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(Color.parseColor("#03A9F4"));

        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.parseColor("#FF11CF31"));

        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(Color.parseColor("#FFE70000"));

        orangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orangePaint.setStyle(Paint.Style.FILL);
        orangePaint.setColor(Color.parseColor("#FFFE7D15"));

        whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.parseColor("#FFFEFEFE"));

        yellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellowPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(Color.parseColor("#FFFCD51E"));

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(6);

        cubieSize = (int)dpToPx(22);
        gap = (int)dpToPx(4);

        frameTimer = new Timer();
        frameTimer.scheduleAtFixedRate(new TimerTask() {

            synchronized public void run() {
                performNextMove();
                postInvalidate();
            }

        }, TimeUnit.MILLISECONDS.toMillis(100), TimeUnit.MILLISECONDS.toMillis(100));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Paint Reds
        int xVal = gap*5;
        int yVal = (cubieSize + gap)*3 + gap*20;
        for(int y = 2; y>=0; y--) {
            for(int z = 2; z>=0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(0, y, z, 'L')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(z-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

        //Paint Yellows
        xVal += (cubieSize + gap)*3 + gap;
        for(int x = 0; x<=2; x++) {
            for(int y = 2; y>=0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(x, y, 0, 'U')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

        //Paint Blues
        yVal -= (cubieSize + gap)*3 + gap;
        for(int x = 0; x<=2; x++) {
            for(int z = 2; z>=0; z--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(x, 2, z, 'B')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * Math.abs(z-2),
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(z-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

        //Paint Greens
        yVal += ((cubieSize + gap)*3 + gap)*2;
        for(int z = 0; z<=2; z++) {
            for(int x = 0; x<=2; x++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(x, 0, z, 'F')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * x,
                        yVal + (cubieSize + gap) * z,
                        xVal + (cubieSize + gap) * x + cubieSize,
                        yVal + (cubieSize + gap) * z + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

        //Paint Oranges
        xVal += (cubieSize + gap)*3 + gap;
        yVal -= (cubieSize + gap)*3 + gap;
        for(int y = 2; y>=0; y--) {
            for(int z = 0; z<=2; z++) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(2, y, z, 'R')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * z,
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * z + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

        //Paint Whites
        xVal += (cubieSize + gap)*3 + gap;
        for(int x = 2; x>=0; x--) {
            for(int y = 2; y>=0; y--) {
                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(x-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        colorToPaint(cube.getColor(x, y, 2, 'D')));

                canvas.drawRoundRect(xVal + (cubieSize + gap) * Math.abs(x-2),
                        yVal + (cubieSize + gap) * Math.abs(y-2),
                        xVal + (cubieSize + gap) * Math.abs(x-2) + cubieSize,
                        yVal + (cubieSize + gap) * Math.abs(y-2) + cubieSize,
                        cubieSize/5,
                        cubieSize/5,
                        strokePaint);
            }
        }

    }

    private float dpToPx(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private Paint colorToPaint(char color) {
        switch(color) {
            case 'W':
                return whitePaint;
            case 'Y':
                return yellowPaint;
            case 'R':
                return redPaint;
            case 'O':
                return orangePaint;
            case 'B':
                return bluePaint;
            case 'G':
                return greenPaint;
        }
        return strokePaint;
    }

    /**
     * After updating the phase (if necessary), performs the next move in the String movesToPerform
     * and updates movesPerformed.
     */
    public void performNextMove() {
        updatePhase();

        //Get to a character that is not a space
        while(movesIndex<movesToPerform.length()-1 && movesToPerform.substring(movesIndex, movesIndex+1).compareTo(" ") == 0) {
            movesIndex++;
        }
        //Same logic as in Cube class's performMoves() method
        if(movesToPerform.length()>0 && movesToPerform.substring(movesIndex, movesIndex+1) != " ") {
            if(movesIndex!=movesToPerform.length()-1) {
                if(movesToPerform.substring(movesIndex+1, movesIndex+2).compareTo("2") == 0) {
                    //Turning twice ex. U2
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex+1));
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex+1));
                    movesIndex++;
                }
                else if(movesToPerform.substring(movesIndex+1,movesIndex+2).compareTo("'") == 0) {
                    //Making a counterclockwise turn ex. U'
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex+2));
                    movesIndex++;
                }
                else {
                    //Clockwise turn
                    cube.turn(movesToPerform.substring(movesIndex, movesIndex+1));
                }
            }
            else {
                //Clockwise turn
                cube.turn(movesToPerform.substring(movesIndex, movesIndex+1));
            }
        }
        movesIndex++;
        //Append the moves performed onto the end of movesPerformed
        if(movesToPerform.length()>0) {
            movesPerformed = movesToPerform.substring(0, movesIndex);
        }
        //Ensure that movesPerformed does not overflow out of the graphical interface
        if(movesPerformed.length() >= 35) {
            movesPerformed = movesPerformed.substring(movesPerformed.length()-33);
        }
    }

    /**
     * Updates the current phase of the solution as necessary
     * Respective stages of the solution w.r.t the phase variable
     * 0 = sunflower		 	1 = whiteCross		2 = whiteCorners		3 = secondLayer
     * 4 = yellowCross		5 = OLL				6 = PLL
     */
    public void updatePhase() {
        if(movesIndex >= movesToPerform.length()) {
            switch(phase) {
                case 0:
                    movesToPerform = whiteCross;
                    phaseString = "White Cross"; break;
                case 1:
                    movesToPerform = whiteCorners;
                    phaseString = "White Corners"; break;
                case 2:
                    movesToPerform = secondLayer;
                    phaseString = "Second Layer"; break;
                case 3:
                    movesToPerform = yellowCross;
                    phaseString = "Yellow Cross";break;
                case 4:
                    movesToPerform = OLL;
                    phaseString = "OLL";break;
                case 5:
                    movesToPerform = PLL;
                    phaseString = "PLL";break;
                case 6:
                    movesToPerform = " ";
                    phaseString = "Solved"; phase--;
                    frameTimer.cancel();
            }
            phase++; movesIndex = 0;
        }
    }

    public void resetScramble(String s) {
        scramble = s;
        cube = new Cube();
        cube.scramble(scramble);
        sunflower = cube.makeSunflower();
        whiteCross = cube.makeWhiteCross();
        whiteCorners = cube.finishWhiteLayer();
        secondLayer = cube.insertAllEdges();
        yellowCross = cube.makeYellowCross();
        OLL = cube.orientLastLayer();
        PLL = cube.permuteLastLayer();

        movesToPerform = sunflower;
        movesPerformed = new String();

        cube = new Cube();
        cube.scramble(scramble);
        //If the cube is being scrambled newly after initializing is complete and animation has begun,
        //be sure to reset all reference indexes
        movesIndex = 0; phase = 0;
        phaseString = "Sunflower";
        invalidate();
    }
}
