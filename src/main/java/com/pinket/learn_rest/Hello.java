package com.pinket.learn_rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello {
    @JsonProperty
    private String text;

    public Hello() {
        this.text = "Hello";
    }
    public Hello(String name) {
        this.text = "Hello " + name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
