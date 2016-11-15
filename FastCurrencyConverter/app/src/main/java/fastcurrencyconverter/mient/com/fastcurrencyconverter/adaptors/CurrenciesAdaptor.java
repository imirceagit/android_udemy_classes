package fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.MainActivity;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.holders.CurrenciesViewHolder;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/15/2016.
 */

public class CurrenciesAdaptor extends RecyclerView.Adapter<CurrenciesViewHolder> {

    private ArrayList<Currency> list;
    private MainActivity activity;

    public CurrenciesAdaptor(ArrayList<Currency> list) {
        this.list = list;
    }

    @Override
    public CurrenciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        activity = (MainActivity) parent.getContext();
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_currencies, parent, false);
        return new CurrenciesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CurrenciesViewHolder holder, int position) {
        final Currency item = list.get(position);
        holder.updateUI(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectCurrency(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
