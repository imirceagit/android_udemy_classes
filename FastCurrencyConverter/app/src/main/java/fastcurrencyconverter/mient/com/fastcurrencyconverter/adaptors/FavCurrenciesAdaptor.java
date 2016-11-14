package fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.holders.FavCurrenciesViewHolder;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/14/2016.
 */

public class FavCurrenciesAdaptor extends RecyclerView.Adapter<FavCurrenciesViewHolder> {

    private ArrayList<Currency> list;

    public FavCurrenciesAdaptor(ArrayList<Currency> list) {
        this.list = list;
    }

    @Override
    public FavCurrenciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View favCard = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_fav_currencies, parent, false);
        return new FavCurrenciesViewHolder(favCard);
    }

    @Override
    public void onBindViewHolder(FavCurrenciesViewHolder holder, final int position) {
        final Currency currency = list.get(position);
        holder.updateUI(currency);

        holder.getCurrencyCheck().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean favorite = list.get(position).isFavorite();
                list.get(position).setFavorite(!favorite);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
