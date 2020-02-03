package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class TasksRequestRange extends BaseResponse {

    private String startDate;

    private String endDate;

    private String filter;

    public TasksRequestRange(String startDate, String endDate, String filter) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.filter = filter;
    }

    public String getEndDate() { return endDate; }

    public void setFilter(String filter) { this.filter = filter; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getFilter() { return filter; }

    public String getStartDate() { return startDate; }
}
