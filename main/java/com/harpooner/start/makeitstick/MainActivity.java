package com.harpooner.start.makeitstick;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lvContainer;
    protected static Typeface[] fonts;
    private ArrayList<Sticker> stickersArray;
    private ArrayList<Sticker> searchArray;
    private SQLiteDatabase dbReadable;
    private StickerAdapter stickerArrayAdapter;
    private StickerAdapter searchArrayAdapter;
    private int editedIndex;
    private static boolean isSmthDeleted = false;
    private static boolean isSmthAmended = false;
    private static boolean isSmthAdded = false;
    private static boolean isSearchPerformed = false;
    private final int SEARCH_ACTIVITY_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContainer = (ListView) findViewById(R.id.lvContainer);
        findViewById(R.id.btnAddSticker).setOnClickListener(this);

        fonts = new Typeface[4];
        fonts[0] = Typeface.defaultFromStyle(R.style.AppTheme);
        fonts[1] = Typeface.createFromAsset(getAssets(), "font8343.otf");
        fonts[2] = Typeface.createFromAsset(getAssets(), "font8983.ttf");
        fonts[3] = Typeface.createFromAsset(getAssets(), "font9756.ttf");

        stickersArray = new ArrayList<Sticker>();
        searchArray = new ArrayList<Sticker>();
        
        dbReadable = new DBHelper(this).getReadableDatabase();
        checkDatabase();

        stickerArrayAdapter = new StickerAdapter(this, stickersArray);
        lvContainer.setAdapter(stickerArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miClearFilter:
                lvContainer.setAdapter(stickerArrayAdapter);
                break;
            case R.id.miSearch:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.miSort:
                Toast.makeText(this, "SORT FUNCTIONALITY WILL BE HERE", Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, AddStickerActivity.class), StickerAdapter.getRequestCodeAmendSticker());
    }

    private void checkDatabase() {
        Cursor cursor = dbReadable.query(DBHelper.STICKERS_TABLE, null, null, null, null, null, null);
        Sticker sticker = null;
        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(DBHelper.COLUMN_ID);
            int heightIndex = cursor.getColumnIndex(DBHelper.COLUMN_HEIGHT);
            int widthIndex = cursor.getColumnIndex(DBHelper.COLUMN_WIDTH);
            int colorIndex = cursor.getColumnIndex(DBHelper.COLUMN_COLOR);
            int fontSizeIndex = cursor.getColumnIndex(DBHelper.COLUMN_FONT_SIZE);
            int fontIndex = cursor.getColumnIndex(DBHelper.COLUMN_FONT);
            int textIndex = cursor.getColumnIndex(DBHelper.COLUMN_TEXT);
            int dateCreatedIndex = cursor.getColumnIndex(DBHelper.COLUMN_DATE_CREATED);
            int dateEditedIndex = cursor.getColumnIndex(DBHelper.COLUMN_DATE_EDITED);
            int flagIndex = cursor.getColumnIndex(DBHelper.COLUMN_FLAG);
            int alarmIndex = cursor.getColumnIndex(DBHelper.COLUMN_ALARM);
            do {
                sticker = new Sticker(cursor.getInt(indexId), cursor.getInt(heightIndex), cursor.getInt(widthIndex),
                        cursor.getInt(colorIndex), cursor.getInt(fontSizeIndex), cursor.getInt(fontIndex),
                        cursor.getString(textIndex), cursor.getLong(dateCreatedIndex), cursor.getLong(dateEditedIndex),
                        cursor.getInt(flagIndex), cursor.getInt(alarmIndex));
                stickersArray.add(sticker);
            } while (cursor.moveToNext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
                if (searchArray.size() != 0) {
                    searchArray.clear();
                }
                for (Sticker s : stickersArray) {
                    if (s.getText().toLowerCase().contains(data.getStringExtra(SearchActivity.getKeySearchData()))) {
                        searchArray.add(s);
                    }
                }
                if (searchArray.size() == 0) {
                    Toast.makeText(this, "SEARCH RETURNED NO RESULT", Toast.LENGTH_SHORT).show();
                } else {
                    searchArrayAdapter = new StickerAdapter(this, searchArray);
                    lvContainer.setAdapter(searchArrayAdapter);
                }
                if (isSearchPerformed) {
                    lvContainer.setAdapter(searchArrayAdapter);
                }
                isSearchPerformed = false;
            }
            if (requestCode == stickerArrayAdapter.getRequestCodeAmendSticker()) {
                if (isSmthDeleted) {
                    stickersArray.remove(editedIndex);
                    isSmthDeleted = false;
                } else {
                    Sticker sticker = new Sticker(data.getIntExtra(DBHelper.COLUMN_ID, 0), data.getIntExtra(DBHelper.COLUMN_HEIGHT, 0),
                            data.getIntExtra(DBHelper.COLUMN_WIDTH, 0), data.getIntExtra(DBHelper.COLUMN_COLOR, 0),
                            data.getIntExtra(DBHelper.COLUMN_FONT_SIZE, 0), data.getIntExtra(DBHelper.COLUMN_FONT, 0),
                            data.getStringExtra(DBHelper.COLUMN_TEXT), data.getLongExtra(DBHelper.COLUMN_DATE_CREATED, 0),
                            data.getLongExtra(DBHelper.COLUMN_DATE_EDITED, 0), data.getIntExtra(DBHelper.COLUMN_FLAG, 0),
                            data.getIntExtra(DBHelper.COLUMN_ALARM, 0));
                    if (isSmthAdded) {
                        stickersArray.add(sticker);
                        isSmthAdded = false;
                    } else if (isSmthAmended) {
                        stickersArray.remove(editedIndex);
                        stickersArray.add(editedIndex, sticker);
                        isSmthAmended= false;
                    }
                }
            }
            stickerArrayAdapter.notifyDataSetChanged();
        }
    }

    protected void setEditedIndex(int index) {
        editedIndex = index;
    }

    protected static void setSmthIsDeleted(boolean isDeleted) {
        isSmthDeleted = isDeleted;
    }

    protected static void setSmthNewIsAdded(boolean isAdded) {
        isSmthAdded = isAdded;
    }

    protected static void setSmthIsAmended(boolean isAmended) {
        isSmthAmended = isAmended;
    }

    protected static void setIsSearchPerformed(boolean isPerformed) {
        isSearchPerformed = isPerformed;
    }
}
