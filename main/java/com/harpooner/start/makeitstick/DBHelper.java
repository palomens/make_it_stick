package com.harpooner.start.makeitstick;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell I5 on 17.04.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public final static String STICKERS_DATABASE = "StickersDatabase";
    public final static String STICKERS_TABLE = "StickersTable";
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_HEIGHT = "height";
    public final static String COLUMN_WIDTH = "width";
    public final static String COLUMN_COLOR = "color";
    public final static String COLUMN_FONT_SIZE = "fontSize";
    public final static String COLUMN_FONT = "font";
    public final static String COLUMN_TEXT = "text";
    public final static String COLUMN_DATE_CREATED = "dateCreated";
    public final static String COLUMN_DATE_EDITED = "dateEdited";
    public final static String COLUMN_FLAG = "flag";
    public final static String COLUMN_ALARM = "alarm";

    public DBHelper(Context context) {
        super(context, STICKERS_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + STICKERS_TABLE + " ("
                + COLUMN_ID + " integer primary key autoincrement,"
                + COLUMN_HEIGHT + " integer,"
                + COLUMN_WIDTH + " integer,"
                + COLUMN_COLOR + " integer,"
                + COLUMN_FONT_SIZE + " integer,"
                + COLUMN_FONT + " integer,"
                + COLUMN_TEXT + " text,"
                + COLUMN_DATE_CREATED + " integer,"
                + COLUMN_DATE_EDITED + " integer,"
                + COLUMN_FLAG + " integer,"
                + COLUMN_ALARM + " integer"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
