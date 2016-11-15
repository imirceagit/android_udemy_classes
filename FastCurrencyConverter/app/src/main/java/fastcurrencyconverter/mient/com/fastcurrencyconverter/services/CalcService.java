package fastcurrencyconverter.mient.com.fastcurrencyconverter.services;

import android.util.Log;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.MainFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/15/2016.
 */
public class CalcService {
    private static CalcService instance = new CalcService();

    public static CalcService getInstance() {
        return instance;
    }

    private CalcService() {
    }

    public void calculate(String tag, double amount, ArrayList<Currency> list){

        Currency xCurrency = list.get(0);

        for (int i = 0; i < list.size(); i++){
            if(list.get(i).getTag().toLowerCase().equals(tag.toLowerCase())){
                xCurrency = list.get(i);
            }
        }

        for (int i = 0; i < list.size(); i++){
            list.get(i).setResult(amount * (list.get(i).getValue() / xCurrency.getValue()));
        }
    }
}
