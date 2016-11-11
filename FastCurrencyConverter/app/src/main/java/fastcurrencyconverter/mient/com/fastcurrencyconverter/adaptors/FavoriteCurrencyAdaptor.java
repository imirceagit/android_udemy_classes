package fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.holders.FavoriteCurrencyViewHolder;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/11/2016.
 */

public class FavoriteCurrencyAdaptor extends RecyclerView.Adapter<FavoriteCurrencyViewHolder> {

    private ArrayList<Currency> todayCurrencies = new ArrayList<>();

    public FavoriteCurrencyAdaptor(ArrayList<Currency> todayCurrencies) {
        this.todayCurrencies = todayCurrencies;
    }

    @Override
    public FavoriteCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View favoriteCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fav_currency, parent, false);
        return new FavoriteCurrencyViewHolder(favoriteCard);
    }

    @Override
    public void onBindViewHolder(FavoriteCurrencyViewHolder holder, int position) {
        final Currency item = todayCurrencies.get(position);
        holder.updateUI(item);
    }

    @Override
    public int getItemCount() {
        return todayCurrencies.size();
    }
}
