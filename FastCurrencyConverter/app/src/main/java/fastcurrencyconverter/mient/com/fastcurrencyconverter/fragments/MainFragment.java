package fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.MainActivity;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.R;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors.CurrenciesAdaptor;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.adaptors.FavCurrenciesAdaptor;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.CalcService;

public class MainFragment extends Fragment {

    private ImageButton mainSettingsButton;
    private ImageView currencyFlag;
    private TextView currencyNameText;
    private TextView currencyTagText;
    private EditText amountText;

    CalcService calc;
    MainActivity activity;
    MainFragment fragment;

    private ArrayList<Currency> favoriteCurrencies;

    private CurrenciesAdaptor adapter;

    private double amount;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        calc = CalcService.getInstance();
        activity = (MainActivity) getActivity();
        favoriteCurrencies = activity.getFavoriteCurrencies();
        fragment = this;

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mainSettingsButton = (ImageButton) view.findViewById(R.id.main_settings_button);
        currencyFlag = (ImageView) view.findViewById(R.id.currency_flag);
        currencyNameText = (TextView) view.findViewById(R.id.currency_name_text);
        currencyTagText = (TextView) view.findViewById(R.id.currency_tag_text);
        amountText = (EditText) view.findViewById(R.id.amount_text);

        mainSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.loadFavoriteFragment();
            }
        });

        amountText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        adapter = new CurrenciesAdaptor(favoriteCurrencies);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_currencies);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecorator(2));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    amount = 0;
                    amountText.setText("0");
                }else {
                    try {
                        amount = Double.parseDouble(s.toString());
                    }catch (NumberFormatException e){
                        Log.v("NUMERIC", e.getLocalizedMessage());
                        amount = 0;
                        amountText.setText("0");
                    }
                    calculate(currencyTagText.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    public void updateList(){
        adapter.notifyDataSetChanged();
    }

    private void calculate(String tag){
        calc.calculate(tag, amount, favoriteCurrencies);
        updateList();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void selectCurrency(Currency item){
        int resurce;
        if(item.getTag().equals("TRY")){
            resurce = currencyFlag.getResources()
                    .getIdentifier(item.getUri() + "t", null, currencyFlag.getContext().getPackageName());
        }else {
            resurce = currencyFlag.getResources()
                    .getIdentifier(item.getUri(), null, currencyFlag.getContext().getPackageName());
        }
        currencyFlag.setImageResource(resurce);
        currencyNameText.setText(item.getName());
        currencyTagText.setText(item.getTag());
        calculate(item.getTag());
    }

}


