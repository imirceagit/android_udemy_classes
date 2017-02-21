package com.mient.mimusicplayer.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.mimusicplayer.R;
import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.adapters.TracksAdapter;

public class TracksFragment extends Fragment {

    private static final String LOG_TAG = "TRACK_FRAGMENT";

    private TracksAdapter adapter;

    public TracksFragment() {

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

        adapter = new TracksAdapter(MainActivity.defaultPlaylist);
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