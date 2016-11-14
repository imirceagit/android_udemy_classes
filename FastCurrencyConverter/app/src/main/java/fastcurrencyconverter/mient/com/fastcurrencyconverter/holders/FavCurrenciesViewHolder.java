package fastcurrencyconverter.mient.com.fastcurrencyconverter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/14/2016.
 */

public class FavCurrenciesViewHolder extends RecyclerView.ViewHolder {

    private ImageView currencyIcon;
    private TextView currencyName;
    private TextView currencyTag;
    private CheckBox currencyCheck;

    public FavCurrenciesViewHolder(View itemView) {
        super(itemView);

        currencyIcon = (ImageView) itemView.findViewById(R.id.currency_icon);
        currencyName = (TextView) itemView.findViewById(R.id.currency_name);
        currencyTag = (TextView) itemView.findViewById(R.id.currency_tag);
        currencyCheck = (CheckBox) itemView.findViewById(R.id.currency_check);
    }

    public void updateUI(Currency item){
        int resurce;
        if(item.getTag().equals("TRY")){
            resurce = currencyIcon.getResources()
                    .getIdentifier(item.getUri() + "t", null, currencyIcon.getContext().getPackageName());
        }else {
            resurce = currencyIcon.getResources()
                    .getIdentifier(item.getUri(), null, currencyIcon.getContext().getPackageName());
        }
        currencyIcon.setImageResource(resurce);
        currencyName.setText(item.getName());
        currencyTag.setText(item.getTag());
        currencyCheck.setChecked(item.isFavorite());
    }

    public CheckBox getCurrencyCheck(){
        return currencyCheck;
    }
}
