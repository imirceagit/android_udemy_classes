package com.mient.herome.herome.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.herome.herome.R;
import com.mient.herome.herome.activities.MainActivity;
import com.mient.herome.herome.entities.Hero;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoryFragment.StoryFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView heroName;
    private ImageView heroLogo;
    private TextView story;
    private Button primaryPower;
    private Button secondaryPower;
    private Button startOverBtn;

    private Hero hero;

    private StoryFragmentInteractionListener mListener;

    public StoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryFragment newInstance(String param1, String param2) {
        StoryFragment fragment = new StoryFragment();
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
        View view  = inflater.inflate(R.layout.fragment_story, container, false);

        heroName = (TextView) view.findViewById(R.id.heroName);
        heroLogo = (ImageView) view.findViewById(R.id.heroLogo);
        story = (TextView) view.findViewById(R.id.story);
        primaryPower = (Button) view.findViewById(R.id.primaryPower);
        secondaryPower = (Button) view.findViewById(R.id.secondaryPower);
        startOverBtn = (Button) view.findViewById(R.id.startOverBtn);

        primaryPower.setEnabled(false);
        secondaryPower.setEnabled(false);

        startOverBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onStoryFragmentInteraction("start");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            hero = (Hero) args.getSerializable(MainActivity.HERO_DATA);
            heroName.setText(hero.getName());
            heroLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), hero.getLogo()));
            primaryPower.setCompoundDrawablesWithIntrinsicBounds(hero.getPrimaryPowerDrawableId(), 0, 0, 0);
            primaryPower.setText(hero.getPrimaryPowerStr());
            secondaryPower.setCompoundDrawablesWithIntrinsicBounds(hero.getSecondaryPowerDrawableId(), 0, 0, 0);
            secondaryPower.setText(hero.getSecondaryPowerStr());
            story.setText(hero.getStory());

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StoryFragmentInteractionListener) {
            mListener = (StoryFragmentInteractionListener) context;
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
    public interface StoryFragmentInteractionListener {
        // TODO: Update argument type and name
        void onStoryFragmentInteraction(String data);
    }
}
