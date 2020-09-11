package com.example.jenga;

import java.io.Serializable;

public class ListItem implements Serializable {
    private int Id;
    private String content;
    private boolean diceChecked;
    private long seconds;

    public ListItem(int id, String content, boolean diceChecked, long seconds) {
        Id = id;
        this.content = content;
        this.diceChecked = diceChecked;
        this.seconds = seconds;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDiceChecked() {
        return diceChecked;
    }

    public void setDiceChecked(boolean diceChecked) {
        this.diceChecked = diceChecked;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }
}
