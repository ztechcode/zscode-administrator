package org.zafritech.zscode.administrator.core.api.tasks.models;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class Tag extends BaseResponse {

    private Long id;

    private String tag;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTag() {  return tag; }

    public void setTag(String tag) { this.tag = tag; }
}
