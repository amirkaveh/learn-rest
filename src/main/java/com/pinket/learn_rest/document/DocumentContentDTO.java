package com.pinket.learn_rest.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentContentDTO {
    @JsonProperty
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
