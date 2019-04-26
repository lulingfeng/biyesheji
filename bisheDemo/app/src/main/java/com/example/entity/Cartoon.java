package com.example.entity;

public class Cartoon {
    private String name;
    private String title;
    private String content;
    private String id;
    private String detaiInfo;

    public String getDetaiInfo() {
        return detaiInfo;
    }

    public void setDetaiInfo(String detaiInfo) {
        this.detaiInfo = detaiInfo;
    }

    public Cartoon(String name, String title, String content){
        this.name=name;
        this.title=title;
        this.content=content;
    }
    public Cartoon(String content){
            this.content=content;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
