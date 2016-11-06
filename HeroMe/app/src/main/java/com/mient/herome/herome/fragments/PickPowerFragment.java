package com.mient.herome.herome.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.mient.herome.herome.R;
import com.mient.herome.herome.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PickPowerFragment.PickPowerFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PickPowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickPowerFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button turtlePowerBtn;;
    private Button lightningBtn;
    private Button flightBtn;
    private Button webSlingingBtn;
    private Button laserVisionBtn;
    private Button superStrengthBtn;
    private Button showStoryBtn;

    private List<MainActivity.POWER> powers;
    private boolean[] btnPressed;

    private PickPowerFragmentInteractionListener mListener;

    public PickPowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PickPowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PickPowerFragment newInstance(String param1, String param2) {
        PickPowerFragment fragment = new PickPowerFragment();
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
        View view = inflater.inflate(R.layout.fragment_pick_power, container, false);

        turtlePowerBtn = (Button) view.findViewById(R.id.turtlePowerBtn);
        lightningBtn = (Button) view.findViewById(R.id.lightningBtn);
        flightBtn = (Button) view.findViewById(R.id.flightBtn);
        webSlingingBtn = (Button) view.findViewById(R.id.webSlingingBtn);
        laserVisionBtn = (Button) view.findViewById(R.id.laserVisionBtn);
        superStrengthBtn = (Button) view.findViewById(R.id.superStrengthBtn);
        showStoryBtn = (Button) view.findViewById(R.id.showStoryBtn);

        turtlePowerBtn.setOnClickListener(this);
        lightningBtn.setOnClickListener(this);
        flightBtn.setOnClickListener(this);
        webSlingingBtn.setOnClickListener(this);
        laserVisionBtn.setOnClickListener(this);
        superStrengthBtn.setOnClickListener(this);

        showStoryBtn.setEnabled(false);
        showStoryBtn.getBackground().setAlpha(128);

        powers = new ArrayList<MainActivity.POWER>();
        btnPressed = new boolean[6];

        for(int i = 0; i < btnPressed.length; i++){
            btnPressed[i] = false;
        }

        showStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnPressed[0]){
                    powers.add(MainActivity.POWER.TURTLE_POWER);
                }
                if(btnPressed[1]){
                    powers.add(MainActivity.POWER.LIGHTNING);
                }
                if(btnPressed[2]){
                    powers.add(MainActivity.POWER.FLIGHT);
                }
                if(btnPressed[3]){
                    powers.add(MainActivity.POWER.WEB_SLINGING);
                }
                if(btnPressed[4]){
                    powers.add(MainActivity.POWER.LASER_VISION);
                }
                if(btnPressed[5]){
                    powers.add(MainActivity.POWER.SUPER_STRENGHT);
                }

                if (mListener != null) {
                    mListener.onPickPowerFragmentInteraction(powers);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        Button btn = (Button) v;
        int leftBtnId = 0;

        if(btn == turtlePowerBtn){
            leftBtnId = R.drawable.turtlepower;
            btnPressed[0] = !btnPressed[0];
        }else if(btn == lightningBtn){
            leftBtnId = R.drawable.thorshammer;
            btnPressed[1] = !btnPressed[1];
        }else if(btn == flightBtn){
            leftBtnId = R.drawable.supermancrest;
            btnPressed[2] = !btnPressed[2];
        }else if(btn == webSlingingBtn){
            leftBtnId = R.drawable.spiderweb;
            btnPressed[3] = !btnPressed[3];
        }else if(btn == laserVisionBtn){
            leftBtnId = R.drawable.laservision;
            btnPressed[4] = !btnPressed[4];
        }else if(btn == superStrengthBtn){
            leftBtnId = R.drawable.superstrength;
            btnPressed[5] = !btnPressed[5];
        }

        int powerCount = 0;

        for(int i = 0; i < btnPressed.length; i++){
            if(btnPressed[i]){
                powerCount++;
                btn.setCompoundDrawablesWithIntrinsicBounds(leftBtnId, 0, R.drawable.itemselected, 0);
            }else {
                btn.setCompoundDrawablesWithIntrinsicBounds(leftBtnId, 0, 0, 0);
            }
        }

        if(powerCount == 2){
            showStoryBtn.setEnabled(true);
            showStoryBtn.getBackground().setAlpha(255);
        }else {
            showStoryBtn.setEnabled(false);
            showStoryBtn.getBackground().setAlpha(128);
        }
   }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PickPowerFragmentInteractionListener) {
            mListener = (PickPowerFragmentInteractionListener) context;
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
    public interface PickPowerFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPickPowerFragmentInteraction(List<MainActivity.POWER> powers);
    }
}
