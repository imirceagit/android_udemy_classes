package com.mient.herome.herome.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mient.herome.herome.R;
import com.mient.herome.herome.activities.MainActivity;
import com.mient.herome.herome.entities.Hero;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.MainFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MainActivity mainActivity;

    private Button accidentBtn;
    private Button geneticBtn;
    private Button bornWithBtn;
    private Button chooseBtn;

    private MainActivity.POWER_CAUSE powerCause;

    private MainFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mainActivity = (MainActivity) getActivity();

        accidentBtn = (Button) view.findViewById(R.id.accidentBtn);
        geneticBtn = (Button) view.findViewById(R.id.geneticBtn);
        bornWithBtn = (Button) view.findViewById(R.id.bornWithBtn);
        chooseBtn = (Button) view.findViewById(R.id.chooseBtn);

        chooseBtn.setEnabled(false);
        chooseBtn.getBackground().setAlpha(128);

        accidentBtn.setOnClickListener(this);
        geneticBtn.setOnClickListener(this);
        bornWithBtn.setOnClickListener(this);

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMainFragmentInteraction(powerCause);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        chooseBtn.setEnabled(true);
        chooseBtn.getBackground().setAlpha(255);

        accidentBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lightning, 0, 0, 0);
        geneticBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.atomic, 0, 0, 0);
        bornWithBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rocket, 0, 0, 0);

        Button btn = (Button) v;

        int leftBtnId = 0;

        if(btn == accidentBtn){
            leftBtnId = R.drawable.lightning;
            powerCause = MainActivity.POWER_CAUSE.ACCIDENT;
        }else if(btn == geneticBtn){
            leftBtnId = R.drawable.atomic;
            powerCause = MainActivity.POWER_CAUSE.GENETIC;
        }else if(btn == bornWithBtn){
            leftBtnId = R.drawable.rocket;
            powerCause = MainActivity.POWER_CAUSE.BORN_WITH;
        }

        btn.setCompoundDrawablesWithIntrinsicBounds(leftBtnId, 0, R.drawable.itemselected, 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainFragmentInteractionListener) {
            mListener = (MainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface MainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainFragmentInteraction(MainActivity.POWER_CAUSE data);
    }
}
