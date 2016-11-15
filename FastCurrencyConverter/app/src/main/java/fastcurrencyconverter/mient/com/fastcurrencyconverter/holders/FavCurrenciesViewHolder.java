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

    private ImageView favCurrencyIcon;
    private TextView favCurrencyName;
    private TextView favCurrencyTag;
    private CheckBox favCurrencyCheck;

    public FavCurrenciesViewHolder(View itemView) {
        super(itemView);

        favCurrencyIcon = (ImageView) itemView.findViewById(R.id.fav_currency_icon);
        favCurrencyName = (TextView) itemView.findViewById(R.id.fav_currency_name);
        favCurrencyTag = (TextView) itemView.findViewById(R.id.fav_currency_tag);
        favCurrencyCheck = (CheckBox) itemView.findViewById(R.id.fav_currency_check);
    }

    public void updateUI(Currency item){
        int resurce;
        if(item.getTag().equals("TRY")){
            resurce = favCurrencyIcon.getResources()
                    .getIdentifier(item.getUri() + "t", null, favCurrencyIcon.getContext().getPackageName());
        }else {
            resurce = favCurrencyIcon.getResources()
                    .getIdentifier(item.getUri(), null, favCurrencyIcon.getContext().getPackageName());
        }
        favCurrencyIcon.setImageResource(resurce);
        favCurrencyName.setText(item.getName());
        favCurrencyTag.setText(item.getTag());
        favCurrencyCheck.setChecked(item.isFavorite());
    }

    public CheckBox getCurrencyCheck(){
        return favCurrencyCheck;
    }
}
