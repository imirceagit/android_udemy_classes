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
 * Use the {@link CurrenciesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrenciesFragment extends Fragment {

    private DataService dataService;

    private ArrayList<Currency> currencies = new ArrayList<>();

    public CurrenciesFragment() {
        dataService = DataService.getInstance();
        dataService.init(getContext());
        currencies = dataService.downloadCurrentValues("latest");
    }

    public static CurrenciesFragment newInstance() {
        CurrenciesFragment fragment = new CurrenciesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currencies, container, false);

        return view;
    }

}
