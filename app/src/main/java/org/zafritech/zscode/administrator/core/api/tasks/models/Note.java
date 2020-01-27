package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Note extends BaseResponse {

    private int id;

    private String text;

    private String timestamp;

    public Note(String text) {

        this.text = text;
    }

    public Note(int id, String text) {

        this.id = id;
        this.text = text;
    }

    public int getId() {

        return id;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(String timestamp) {

        this.timestamp = timestamp;
    }
}
