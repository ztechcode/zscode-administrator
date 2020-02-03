package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Schedule extends BaseResponse {

    private Long id;

    private String owner;

    private String date;

    private String time;

    private Task task;

    private boolean done;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }
}
