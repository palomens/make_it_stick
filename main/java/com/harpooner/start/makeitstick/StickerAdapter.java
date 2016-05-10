package com.harpooner.start.makeitstick;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StickerAdapter extends ArrayAdapter<Sticker> {

    private LayoutInflater infl;
    private MainActivity context;
    private Sticker s;
    private static final int REQUEST_CODE_AMEND_STICKER = 10;

    public StickerAdapter(Context context, ArrayList<Sticker> objects) {
        super(context, 0, objects);
        this.context = (MainActivity) context;
        infl = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        s = getItem(position);

        if (convertView == null) {
            convertView = infl.inflate(R.layout.sticker_list_item, parent, false);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setHeight(s.getHeight());
        tv.setWidth(s.getWidth());
        tv.setBackgroundColor(s.getColor());
        tv.setTextSize(s.getFontSize() / 2);
        tv.setTypeface(context.fonts[s.getFont()]);
        tv.setText(s.getText());
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 80, 5);
        tv.setTextColor(Color.BLACK);

        ImageView ivFlag = (ImageView) convertView.findViewById(R.id.ivFlag);
        ivFlag.setImageResource(R.drawable.ic_flag_black);
        if (s.getFlagImportance() == AddStickerActivity.getStatusNonTransparent()) {
            ivFlag.getDrawable().setAlpha(AddStickerActivity.getStatusNonTransparent());
        } else {
            ivFlag.getDrawable().setAlpha(AddStickerActivity.getStatusInvisible());
        }

        ImageView ivAlarm = (ImageView) convertView.findViewById(R.id.ivAlarm);
        ivAlarm.setImageResource(R.drawable.ic_alarm_black_48dp);
        if (s.getAlarmSet() == AddStickerActivity.getStatusNonTransparent()) {
            ivAlarm.getDrawable().setAlpha(AddStickerActivity.getStatusNonTransparent());
        } else {
            ivAlarm.getDrawable().setAlpha(AddStickerActivity.getStatusInvisible());
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(getItem(position).getId()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AddStickerActivity.class);
                intent.putExtra(DBHelper.COLUMN_ID, getItem(position).getId());
                intent.putExtra(DBHelper.COLUMN_HEIGHT, getItem(position).getHeight());
                intent.putExtra(DBHelper.COLUMN_WIDTH, getItem(position).getWidth());
                intent.putExtra(DBHelper.COLUMN_COLOR, getItem(position).getColor());
                intent.putExtra(DBHelper.COLUMN_FONT_SIZE, getItem(position).getFontSize() / 2);
                intent.putExtra(DBHelper.COLUMN_FONT, getItem(position).getFont());
                intent.putExtra(DBHelper.COLUMN_TEXT, getItem(position).getText());
                intent.putExtra(DBHelper.COLUMN_DATE_CREATED, getItem(position).getDateCreated());
                intent.putExtra(DBHelper.COLUMN_DATE_EDITED, getItem(position).getDateEdited());
                intent.putExtra(DBHelper.COLUMN_FLAG, getItem(position).getFlagImportance());
                intent.putExtra(DBHelper.COLUMN_ALARM, getItem(position).getAlarmSet());
                context.setEditedIndex(position);
                context.startActivityForResult(intent, REQUEST_CODE_AMEND_STICKER);
            }
        });
        return convertView;
    }

    protected static int getRequestCodeAmendSticker() {
        return REQUEST_CODE_AMEND_STICKER;
    }
}