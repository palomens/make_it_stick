package com.harpooner.start.makeitstick;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddStickerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSave, btnSetAlarm, btnDelete, btnFlag;
    private ArrayList<Button> colorButtons;
    private ArrayList<Integer> colorArray;
    private Button btnTextSize18, btnTextSize24, btnTextSize30, btnTypeface;
    private Button btnOneSecond, btnOneQuarter, btnOneEighth;
    private boolean isImportant, isAlarmSet;
    private EditText etSticker;
    private int fontCounter = 0;
    private int editedStickerId;
    private static long rowId;
    private DBHelper dbHelper;
    private SQLiteDatabase dbWritable;
    private static final int STATUS_INVISIBLE = 0;
    private static final int STATUS_TRANSPARENT = 100;
    private static final int STATUS_NON_TRANSPARENT = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sticker);

        initData();

        if (getIntent().getExtras() == null) {
            createNewSticker();
        } else {
            amendExistingSticker();
        }
    }

    private void initData() {

        etSticker = (EditText) findViewById(R.id.etSticker);
        etSticker.setGravity(Gravity.CENTER);
        etSticker.setPadding(10, 5, 80, 5);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        btnFlag = (Button) findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(this);

        btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(this);

        colorButtons = new ArrayList<Button>();

        colorButtons.add((Button) findViewById(R.id.btnColor1));
        colorButtons.add((Button) findViewById(R.id.btnColor2));
        colorButtons.add((Button) findViewById(R.id.btnColor3));
        colorButtons.add((Button) findViewById(R.id.btnColor4));

        colorArray = new ArrayList<Integer>();
        colorArray.add(getResources().getColor(R.color.colorBackgroundYellow));
        colorArray.add(getResources().getColor(R.color.colorBackgroundOrange));
        colorArray.add(getResources().getColor(R.color.colorBackgroundBlue));
        colorArray.add(getResources().getColor(R.color.colorBackgroundGreen));
        colorArray.add(getResources().getColor(R.color.colorBackgroundRose));

        btnTextSize18 = (Button) findViewById(R.id.btnTextSize18);
        btnTextSize18.setOnClickListener(this);

        btnTextSize24 = (Button) findViewById(R.id.btnTextSize24);
        btnTextSize24.setOnClickListener(this);

        btnTextSize30 = (Button) findViewById(R.id.btnTextSize30);
        btnTextSize30.setOnClickListener(this);

        btnTypeface = (Button) findViewById(R.id.btnTypeface);
        btnTypeface.setOnClickListener(this);

        btnOneSecond = (Button) findViewById(R.id.btnOneSecond);
        btnOneSecond.setOnClickListener(this);

        btnOneQuarter = (Button) findViewById(R.id.btnOneQuarter);
        btnOneQuarter.setOnClickListener(this);

        btnOneEighth = (Button) findViewById(R.id.btnOneEighth);
        btnOneEighth.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbWritable = dbHelper.getWritableDatabase();
    }

    private void createNewSticker() {
        editedStickerId = 0;
        int randomIndex = (int) (Math.random() * colorArray.size());
        etSticker.setBackgroundColor(colorArray.get(randomIndex));
        paintColorButtons(randomIndex);
        isImportant = false;
        isAlarmSet = false;
        btnFlag.getBackground().setAlpha(STATUS_TRANSPARENT);
        btnSetAlarm.getBackground().setAlpha(STATUS_TRANSPARENT);
        btnTextSize18.setEnabled(false);
        btnOneSecond.setEnabled(false);
    }

    private void amendExistingSticker() {

        Intent intent = getIntent();
        editedStickerId = intent.getIntExtra(DBHelper.COLUMN_ID, 0);
        etSticker.setText(intent.getStringExtra(DBHelper.COLUMN_TEXT));

        int currentTextSize = intent.getIntExtra(DBHelper.COLUMN_FONT_SIZE, 0);
        etSticker.setTextSize(currentTextSize);
        if (currentTextSize == 18) {
            btnTextSize18.setEnabled(false);
        } else if (currentTextSize == 24) {
            btnTextSize24.setEnabled(false);
        } else if (currentTextSize == 30) {
            btnTextSize30.setEnabled(false);
        }

        int currentHeight = intent.getIntExtra(DBHelper.COLUMN_HEIGHT, 0);
        int currentWidth = intent.getIntExtra(DBHelper.COLUMN_WIDTH, 0);
        RelativeLayout.LayoutParams lParams = new RelativeLayout.LayoutParams(currentWidth, currentHeight);
        etSticker.setLayoutParams(lParams);
        if (currentHeight == getResources().getDisplayMetrics().heightPixels / 2) {
            btnOneSecond.setEnabled(false);
        } else if (currentHeight == getResources().getDisplayMetrics().heightPixels / 4) {
            btnOneQuarter.setEnabled(false);
        } else if (currentHeight == getResources().getDisplayMetrics().heightPixels / 8) {
            btnOneEighth.setEnabled(false);
        }

        int currentFontIndex = intent.getIntExtra(DBHelper.COLUMN_FONT, 0);
        etSticker.setTypeface(MainActivity.fonts[currentFontIndex]);
        fontCounter = currentFontIndex;

        int currentBackgroundColor = intent.getIntExtra(DBHelper.COLUMN_COLOR, 0);
        etSticker.setBackgroundColor(currentBackgroundColor);
        int indexOfCurrentColor = colorArray.indexOf(currentBackgroundColor);
        paintColorButtons(indexOfCurrentColor);

        if (intent.getIntExtra(DBHelper.COLUMN_FLAG, 0) == STATUS_NON_TRANSPARENT) {
            btnFlag.getBackground().setAlpha(STATUS_NON_TRANSPARENT);
            isImportant = true;
        } else {
            btnFlag.getBackground().setAlpha(STATUS_TRANSPARENT);
            isImportant = false;
        }

        if (intent.getIntExtra(DBHelper.COLUMN_ALARM, 0) == STATUS_NON_TRANSPARENT) {
            btnSetAlarm.getBackground().setAlpha(STATUS_NON_TRANSPARENT);
            isAlarmSet = true;
        } else {
            btnSetAlarm.getBackground().setAlpha(STATUS_TRANSPARENT);
            isAlarmSet = false;
        }
    }

    private void paintColorButtons(int indexToRemove) {
        colorArray.remove(indexToRemove);
        for (int i = 0; i < colorArray.size(); i++) {
            colorButtons.get(i).setBackgroundColor(colorArray.get(i));
            colorButtons.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFlag:
                setImportance();
                break;
            case R.id.btnSetAlarm:
                setAlarm();
                break;
            case R.id.btnColor1:
                changeColor(colorButtons.get(0));
                break;
            case R.id.btnColor2:
                changeColor(colorButtons.get(1));
                break;
            case R.id.btnColor3:
                changeColor(colorButtons.get(2));
                break;
            case R.id.btnColor4:
                changeColor(colorButtons.get(3));
                break;
            case R.id.btnTextSize18:
                changeFontSize(btnTextSize18, btnTextSize24, btnTextSize30);
                break;
            case R.id.btnTextSize24:
                changeFontSize(btnTextSize24, btnTextSize18, btnTextSize30);
                break;
            case R.id.btnTextSize30:
                changeFontSize(btnTextSize30, btnTextSize18, btnTextSize24);
                break;
            case R.id.btnTypeface:
                etSticker.setTypeface(MainActivity.fonts[++fontCounter % 4]);
                break;
            case R.id.btnOneSecond:
                setStickerSize(2, btnOneSecond, btnOneQuarter, btnOneEighth);
                break;
            case R.id.btnOneQuarter:
                setStickerSize(4, btnOneQuarter, btnOneSecond, btnOneEighth);
                break;
            case R.id.btnOneEighth:
                setStickerSize(8, btnOneEighth, btnOneSecond, btnOneQuarter);
                break;
            case R.id.btnSave:
                saveSticker();
                break;
            case R.id.btnDelete:
                deleteFromDatabase();
                break;
        }
    }

    private void setImportance() {
        if (isImportant) {
            btnFlag.getBackground().setAlpha(STATUS_TRANSPARENT);
            isImportant = false;
        } else {
            btnFlag.getBackground().setAlpha(STATUS_NON_TRANSPARENT);
            isImportant = true;
        }
    }

    private void setAlarm() {
        if (isAlarmSet) {
            btnSetAlarm.getBackground().setAlpha(STATUS_TRANSPARENT);
            isAlarmSet = false;
        } else {
            btnSetAlarm.getBackground().setAlpha(STATUS_NON_TRANSPARENT);
            isAlarmSet = true;
        }
    }

    private void changeColor(Button btn) {
        Drawable tempColor = etSticker.getBackground().getCurrent();
        etSticker.setBackground(btn.getBackground().getCurrent());
        btn.setBackground(tempColor);
    }

    private void changeFontSize(Button btn, Button other1, Button other2) {
        etSticker.setTextSize(Integer.valueOf(btn.getText().toString()));
        btn.setEnabled(false);
        other1.setEnabled(true);
        other2.setEnabled(true);
    }

    private void setStickerSize(int divider, Button btn, Button other1, Button other2) {
        RelativeLayout.LayoutParams layoutParams = null;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getResources().getDisplayMetrics().heightPixels / divider);
        } else if (getIntent().getExtras() != null) {
            layoutParams = new RelativeLayout.LayoutParams(getIntent().getIntExtra(DBHelper.COLUMN_WIDTH, 0), getResources().getDisplayMetrics().widthPixels / divider);
        } else {
            layoutParams = new RelativeLayout.LayoutParams(etSticker.getWidth(), getResources().getDisplayMetrics().widthPixels / divider);
        }
        etSticker.setLayoutParams(layoutParams);
        btn.setEnabled(false);
        other1.setEnabled(true);
        other2.setEnabled(true);
    }

    private void saveSticker() {

        ColorDrawable background = (ColorDrawable) etSticker.getBackground().getCurrent();
        Calendar calendar = Calendar.getInstance();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_HEIGHT, etSticker.getHeight());
        cv.put(DBHelper.COLUMN_WIDTH, etSticker.getWidth());
        cv.put(DBHelper.COLUMN_COLOR, background.getColor());
        cv.put(DBHelper.COLUMN_FONT_SIZE, etSticker.getTextSize());
        cv.put(DBHelper.COLUMN_FONT, fontCounter % 4);
        cv.put(DBHelper.COLUMN_TEXT, etSticker.getText().toString());
        cv.put(DBHelper.COLUMN_DATE_EDITED, calendar.getTimeInMillis());

        if (isImportant) {
            cv.put(DBHelper.COLUMN_FLAG, STATUS_NON_TRANSPARENT);
        } else {
            cv.put(DBHelper.COLUMN_FLAG, STATUS_INVISIBLE);
        }

        if (isAlarmSet) {
            cv.put(DBHelper.COLUMN_ALARM, STATUS_NON_TRANSPARENT);
        } else {
            cv.put(DBHelper.COLUMN_ALARM, STATUS_INVISIBLE);
        }

        Intent intent = prepareIntent(background, calendar);

        if (editedStickerId == 0) {
            cv.put(DBHelper.COLUMN_DATE_CREATED, calendar.getTimeInMillis());
            rowId = dbWritable.insert(DBHelper.STICKERS_TABLE, null, cv);
            intent.putExtra(DBHelper.COLUMN_ID, (int) rowId);
            MainActivity.setSmthNewIsAdded(true);
            Toast.makeText(this, "new row id = " + rowId, Toast.LENGTH_SHORT).show();
        } else {
            cv.put(DBHelper.COLUMN_DATE_CREATED, getIntent().getLongExtra(DBHelper.COLUMN_DATE_CREATED, 0));
            String[] whereArgs = {String.valueOf(editedStickerId)};
            rowId = dbWritable.update(DBHelper.STICKERS_TABLE, cv, DBHelper.COLUMN_ID + " = ?", whereArgs);
            intent.putExtra(DBHelper.COLUMN_ID, getIntent().getIntExtra(DBHelper.COLUMN_ID, 0));
            MainActivity.setSmthIsAmended(true);
            Toast.makeText(this, "edited row id = " + editedStickerId, Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void deleteFromDatabase() {
        int delCount = dbWritable.delete(DBHelper.STICKERS_TABLE, DBHelper.COLUMN_ID + " = " + editedStickerId, null);
        Toast.makeText(this, "DELETED " + editedStickerId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(DBHelper.COLUMN_ID, getIntent().getIntExtra(DBHelper.COLUMN_ID, 0));
        MainActivity.setSmthIsDeleted(true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private Intent prepareIntent(ColorDrawable background, Calendar calendar) {
        Intent intent = new Intent();
        intent.putExtra(DBHelper.COLUMN_HEIGHT, etSticker.getHeight());
        intent.putExtra(DBHelper.COLUMN_WIDTH, etSticker.getWidth());
        intent.putExtra(DBHelper.COLUMN_COLOR, background.getColor());
        intent.putExtra(DBHelper.COLUMN_FONT_SIZE, (int) etSticker.getTextSize());
        intent.putExtra(DBHelper.COLUMN_FONT, fontCounter % 4);
        intent.putExtra(DBHelper.COLUMN_TEXT, etSticker.getText().toString());
        intent.putExtra(DBHelper.COLUMN_DATE_EDITED, calendar.getTimeInMillis());
        intent.putExtra(DBHelper.COLUMN_DATE_CREATED, getIntent().getLongExtra(DBHelper.COLUMN_DATE_CREATED, 0));
        if (isImportant) {
            intent.putExtra(DBHelper.COLUMN_FLAG, STATUS_NON_TRANSPARENT);
        } else {
            intent.putExtra(DBHelper.COLUMN_FLAG, STATUS_INVISIBLE);
        }
        if (isAlarmSet) {
            intent.putExtra(DBHelper.COLUMN_ALARM, STATUS_NON_TRANSPARENT);
        } else {
            intent.putExtra(DBHelper.COLUMN_ALARM, STATUS_INVISIBLE);
        }
        return intent;
    }

    protected static int getStatusNonTransparent() {
        return STATUS_NON_TRANSPARENT;
    }

    protected static int getStatusInvisible() {
        return STATUS_INVISIBLE;
    }
}
