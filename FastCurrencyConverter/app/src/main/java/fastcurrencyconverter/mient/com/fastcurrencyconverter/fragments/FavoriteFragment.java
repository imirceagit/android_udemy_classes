package fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.MainActivity;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors.FavCurrenciesAdaptor;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

public class FavoriteFragment extends Fragment {

    private ImageButton saveFavButton;

    MainActivity activity;

    private ArrayList<Currency> todayCurrencies;
    private ArrayList<Currency> favoriteCurrencies;

    FavCurrenciesAdaptor adapter;

    public FavoriteFragment() {

    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        saveFavButton = (ImageButton) view.findViewById(R.id.save_fav_button);

        saveFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.createFavoriteList();
            }
        });

        activity = (MainActivity) getActivity();

        todayCurrencies = activity.getTodayCurrencies();
        favoriteCurrencies = activity.getFavoriteCurrencies();

        adapter = new FavCurrenciesAdaptor(todayCurrencies);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fav_currencies);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(2));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void updateList(){
        adapter.notifyDataSetChanged();
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
