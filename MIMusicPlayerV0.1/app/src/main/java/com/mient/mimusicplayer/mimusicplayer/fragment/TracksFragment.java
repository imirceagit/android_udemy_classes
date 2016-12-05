package com.mient.mimusicplayer.mimusicplayer.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.adapters.TracksAdapter;
import com.mient.mimusicplayer.mimusicplayer.model.Track;

import java.util.ArrayList;

public class TracksFragment extends Fragment {

    private static final String LOG_TAG = "TRACK_FRAGMENT";

    private ArrayList<Track> trackList;
    private MainActivity activity;

    private TracksAdapter adapter;

    public TracksFragment() {
        // Required empty public constructor
    }

    public static TracksFragment newInstance() {
        TracksFragment fragment = new TracksFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tracks, container, false);

        Log.v(LOG_TAG, "INFLATE");

        activity = MainActivity.mainActivity;
        trackList = MainActivity.allTracksList;

        adapter = new TracksAdapter(trackList);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_tracks);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(1));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

}
class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private  final int spacer;

    public VerticalSpaceItemDecorator(int spacer) {
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = spacer;
    }
}