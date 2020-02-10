package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Schedule extends BaseResponse {

    private Long id;

    private String owner;

    private String deadline;

    private Task task;

    private boolean done;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }

    public String getDeadline() { return deadline; }

    public void setDeadline(String deadline) { this.deadline = deadline; }

    public Task getTask() { return task; }

    public void setTask(Task task) { this.task = task; }

    public boolean isDone() { return done; }

    public void setDone(boolean done) { this.done = done; }
}
