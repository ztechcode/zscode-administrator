package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;
import org.zafritech.zscode.administrator.core.api.tasks.enums.Priority;

import java.util.List;

public class Task extends BaseResponse {

    private int id;

    private String details;

    private Long parentId;

    private Category category;

    private boolean complete;

    private Repeat repeat;

    private Priority priority;

    private List<Note> notes;

    private List<Tag> tags;

    private String created;

    private String due;

    public Task(String details) {

        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public boolean isComplete() {
        return complete;
    }

    public Repeat getRepeat() {return repeat;}

    public Priority getPriority()  { return priority; }

    public void setPriority(Priority priority) { this.priority = priority; }

    public void setRepeat(Repeat repeat) { this.repeat = repeat; }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public List<Note> getNotes() { return notes; }

    public void setNotes(List<Note> notes) { this.notes = notes; }

    public List<Tag> getTags() { return tags; }

    public void setTags(List<Tag> tags) { this.tags = tags; }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }
}
