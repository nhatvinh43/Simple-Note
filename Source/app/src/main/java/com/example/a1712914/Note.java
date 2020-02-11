package com.example.a1712914;

public class Note {
    private String title;
    private String date;
    private String tags;
    private String content;
    public int align = 1;

    public Note(String title, String date, String tags, String content)
    {
        this.title = title;
        this.date = date;
        this.tags = tags;
        this.content = content;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTags() {
        return tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
