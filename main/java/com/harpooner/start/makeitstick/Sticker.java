package com.harpooner.start.makeitstick;

public class Sticker {

    private int id;
    private int height;
    private int width;
    private int color;
    private int fontSize;
    private int font;
    private String text;
    private long dateCreated;
    private long dateEdited;
    private int flagImportance;
    private int alarmSet;

    public Sticker(int id, int height, int width, int color, int fontSize, int font, String text,
                   long dateCreated, long dateEdited, int flagImportance, int alarmSet) {
        this.id = id;
        this.height = height;
        this.width = width;
        this.color = color;
        this.fontSize = fontSize;
        this.font = font;
        this.text = text;
        this.dateCreated = dateCreated;
        this.dateEdited = dateEdited;
        this.flagImportance = flagImportance;
        this.alarmSet = alarmSet;
    }

    protected int getId() {
        return id;
    }

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getColor() {
        return color;
    }

    protected int getFontSize() {
        return fontSize;
    }

    protected int getFont() {
        return font;
    }

    protected String getText() {
        return text;
    }

    protected long getDateCreated() {
        return dateCreated;
    }

    protected long getDateEdited() {
        return dateEdited;
    }

    protected int getFlagImportance() {
        return flagImportance;
    }

    protected int getAlarmSet() {
        return alarmSet;
    }
}
