package fastcurrencyconverter.mient.com.fastcurrencyconverter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by mircea.ionita on 11/16/2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fast_currency_mient.db";
    private static final int DATABASE_VERSION = 1;

    private String[] tags = {"AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD",
            "HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
            "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};

    private final String FAV_TABLE_NAME = "fav_currencies";
    private final String FAV_COL_1 = "_id";
    private final String FAV_COL_2 = "tag";

//    private final String CURR_TABLE_NAME = "daily_currencies";
//    private final String CURR_COL_1 = "_id";
//    private final String CURR_COL_2 = "date";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + FAV_TABLE_NAME + "("
                + FAV_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAV_COL_2 + " CHAR(5) NOT NULL);");

        db.execSQL("INSERT INTO " + FAV_TABLE_NAME + "(" + FAV_COL_2 + ") VALUES ('EUR');");
        db.execSQL("INSERT INTO " + FAV_TABLE_NAME + "(" + FAV_COL_2 + ") VALUES ('USD');");
        db.execSQL("INSERT INTO " + FAV_TABLE_NAME + "(" + FAV_COL_2 + ") VALUES ('GBP');");
        Log.v("CREATE TAB", "OnCreate");
//        createCurrencyTable(db);
    }

//    private void createCurrencyTable(SQLiteDatabase db){
//
//        String COLLUMS = "";
//
//        for (int i = 0; i < tags.length; i++){
//            if(i == tags.length - 1){
//                COLLUMS += " " + tags[i] + " INTEGER NOT NULL";
//            }else {
//                COLLUMS += " " + tags[i] + " INTEGER NOT NULL,";
//            }
//        }
//
//        db.execSQL("CREATE TABLE " + CURR_TABLE_NAME + "("
//                + CURR_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + CURR_COL_2 + " CHAR(10) NOT NULL,"
//                + COLLUMS +");");
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + CURR_TABLE_NAME);
        onCreate(db);
    }

    public long insertFavCurrency(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_COL_2, tag);
        long result = db.insert(FAV_TABLE_NAME, null, contentValues);
        return result;
    }

//    public long insertTodayCurrency(String date, List<Currency> list){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(CURR_COL_2, date);
//        for (int i = 0; i < list.size(); i++){
//            contentValues.put(tags[i], list.get(i).getValue());
//        }
//        long result = db.insert(CURR_TABLE_NAME, null, contentValues);
//        return result;
//    }

//    public Cursor getLastCurencies(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from " + CURR_TABLE_NAME, null);
//        return cursor;
//    }

    public Cursor getFavoriteCurencies(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + FAV_TABLE_NAME, null);
        return cursor;
    }

    public void clearFavoriteTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + FAV_TABLE_NAME);
    }
}
