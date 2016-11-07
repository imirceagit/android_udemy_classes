package com.mient.radiomi.radiomi.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mient.radiomi.radiomi.R;
import com.mient.radiomi.radiomi.model.Station;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STATION_SELECTED = "station_selected";

    private RelativeLayout detailsHeader;
    private TextView detailsHeaderTitle;
    private ImageView detailsHeaderImage;

    private Station station;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param station Parameter 1.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(Station station) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(STATION_SELECTED, station);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            station = (Station) getArguments().getSerializable(STATION_SELECTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        detailsHeader = (RelativeLayout) v.findViewById(R.id.details_header);
        detailsHeaderTitle = (TextView) v.findViewById(R.id.details_header_title);
        detailsHeaderImage = (ImageView) v.findViewById(R.id.details_header_image);

        detailsHeader.setBackgroundColor(Color.parseColor(station.getBackgroundColor()));
        detailsHeaderTitle.setText(station.getStationTitle());
        int uri = detailsHeaderImage.getResources().getIdentifier(station.getImgUri(), null, detailsHeaderImage.getContext().getPackageName());
        detailsHeaderImage.setImageResource(uri);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        PlaylistFragment playlistFragment = PlaylistFragment.newInstance("", "");
        fragmentManager.beginTransaction().add(R.id.container_playlist, playlistFragment).commit();

        return v;
    }

}
