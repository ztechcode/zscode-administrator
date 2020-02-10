package org.zafritech.zscode.administrator.core.api.tasks.models;

public class BasicTask {

    private Long id;

    private String task;

    public BasicTask(String task) {
        this.task = task;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
