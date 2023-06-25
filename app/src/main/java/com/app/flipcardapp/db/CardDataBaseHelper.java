package com.app.flipcardapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.app.flipcardapp.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardDataBaseHelper extends SQLiteOpenHelper {
    public static final String FLASHCARD = "FLASHCARD";
    public static final String CARD_IMG = "CARD_IMG";
    public static final String CARD_TEXT = "CARD_TEXT";
    public static final String ID = "ID";

    public CardDataBaseHelper(@Nullable Context context) {
        super(context, "card.db", null, 1);

    }

    public CardDataBaseHelper(@Nullable Context context, String db) {
        super(context, db, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + FLASHCARD + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARD_IMG + " BLOB, " + CARD_TEXT + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Card c){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CARD_IMG,c.getFrontImg());
        cv.put(CARD_TEXT,c.getBackText());

        long insert = db.insert(FLASHCARD, null, cv);
        if(insert == -1){

            return false;
        }else{
            return true;
        }

    }


    public List<Card> getAllCards(){

        List<Card> cards = new ArrayList<>();

        String selectString = "SELECT "+ID+","+CARD_IMG+","+CARD_TEXT+" FROM "+FLASHCARD;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectString, null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                byte[] img = cursor.getBlob(1);
                String txt = cursor.getString(2);


                Card c = new Card();
                c.setId(id);
                c.setImg(img);
                c.setBackText(txt);


                cards.add(c);

            }while(cursor.moveToNext());

        }
        else{

        }

        //Closing both cursor and database
        cursor.close();
        db.close();
        return cards;
    }

    public void updateCard(Card c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CARD_IMG, c.getFrontImg());
        values.put(CARD_TEXT, c.getBackText());



        // updating row
        db.update(FLASHCARD, values, ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        db.close();
    }

    public Boolean deleteCard(Card c) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        long insert = db.delete(FLASHCARD, ID + " = ?",
                new String[]{String.valueOf(c.getId())});
        db.close();

        if(insert == -1){

            return false;
        }else{
            return true;
        }
    }



}
