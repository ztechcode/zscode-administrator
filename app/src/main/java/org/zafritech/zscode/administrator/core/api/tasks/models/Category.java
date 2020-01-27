package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Category extends BaseResponse {

    private int id;

    private String name;

    public Category(String name) {

        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
