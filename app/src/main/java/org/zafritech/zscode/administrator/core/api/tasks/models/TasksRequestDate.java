package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class TasksRequestDate extends BaseResponse {

    private String filter;

    private String date;

    public TasksRequestDate(String date, String filter) {

        this.filter = filter;
        this.date = date;
    }

    public String getFilter() { return filter; }

    public String getDate() { return date; }

    public void setFilter(String filter) { this.filter = filter; }

    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return "TasksRequestDate{" +
                "filter='" + filter + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
