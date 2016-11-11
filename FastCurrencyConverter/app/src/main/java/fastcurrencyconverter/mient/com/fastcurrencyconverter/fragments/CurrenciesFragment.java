package fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors.FavoriteCurrencyAdaptor;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrenciesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrenciesFragment extends Fragment {

    private static final String TODAY_CURRENCY = "today_curr";

    private ArrayList<Currency> todayCurrencies = new ArrayList<>();

    public CurrenciesFragment() {

    }

    public static CurrenciesFragment newInstance(ArrayList<Currency> todayCurrencies) {
        CurrenciesFragment fragment = new CurrenciesFragment();
        Bundle args = new Bundle();
        args.putSerializable(TODAY_CURRENCY, todayCurrencies);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            todayCurrencies = (ArrayList<Currency>) getArguments().getSerializable(TODAY_CURRENCY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currencies, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_fav_currency);
        recyclerView.setHasFixedSize(true);

        FavoriteCurrencyAdaptor adaptor =  new FavoriteCurrencyAdaptor(DataService.getInstance().getTodayCurrencies());
        recyclerView.setAdapter(adaptor);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(50));


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

