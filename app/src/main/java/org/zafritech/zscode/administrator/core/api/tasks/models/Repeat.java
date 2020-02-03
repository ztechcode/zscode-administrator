package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Repeat extends BaseResponse {

    private Long id;

    private String type;

    private Long count;

    private String start;

    private String lastRepeat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getLastRepeat() {
        return lastRepeat;
    }

    public void setLastRepeat(String lastRepeat) {
        this.lastRepeat = lastRepeat;
    }
}
