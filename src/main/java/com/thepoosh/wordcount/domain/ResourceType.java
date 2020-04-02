package com.thepoosh.wordcount.domain;

public enum ResourceType {
    PATH("path"),
    URL("url"),
    TEXT("text");

    private final String name;

    ResourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
