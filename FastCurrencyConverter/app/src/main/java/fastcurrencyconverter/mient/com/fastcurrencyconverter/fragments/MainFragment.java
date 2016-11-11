package fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String FAVORITE_CURRENCY = "favorite_curr";

    private ArrayList<Currency> favoriteCurrencies = new ArrayList<>();

    public MainFragment() {

    }

    public static MainFragment newInstance(ArrayList<Currency> favoriteCurrencies) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(FAVORITE_CURRENCY, favoriteCurrencies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            favoriteCurrencies = (ArrayList<Currency>) getArguments().getSerializable(FAVORITE_CURRENCY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        return view;
    }

    public void setFavoriteCurrencies(ArrayList<Currency> currencies){
        favoriteCurrencies = currencies;
    }

}
