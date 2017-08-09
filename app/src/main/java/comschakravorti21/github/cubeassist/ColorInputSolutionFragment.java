package comschakravorti21.github.cubeassist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.GridLayout;


public class ColorInputSolutionFragment extends Fragment implements View.OnClickListener{

    private View rootView;
    private GridLayout palette;

    public ColorInputSolutionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_color_input_fragment, container, false);
        rootView.setOnClickListener(this);
        palette = rootView.findViewById(R.id.palette);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PaletteButtonClickListener paletteButtonClickListener = new PaletteButtonClickListener();
        for(int i = palette.getChildCount()-1; i > 0; i--) {
            palette.getChildAt(i).setOnClickListener(paletteButtonClickListener);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((Activity)getContext()).getMenuInflater().inflate(R.menu.menu_color_solution, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    private class PaletteButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.palette) {
                Log.d("Clicked in whitespace:", "YES");
            } else  {
                Log.d("Clicked in whitespace:", "NO");
            }
        }
    }
}
