package fastcurrencyconverter.mient.com.fastcurrencyconverter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mircea.ionita on 11/16/2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fast_currency_mient.db";
    private static final int DATABASE_VERSION = 1;

    private final String FAV_TABLE_NAME = "fav_currencies";
    private final String FAV_COL_1 = "_id";
    private final String FAV_COL_2 = "tag";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
        onCreate(db);
    }

    public long insertFavCurrency(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_COL_2, tag);
        long result = db.insert(FAV_TABLE_NAME, null, contentValues);
        return result;
    }

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
