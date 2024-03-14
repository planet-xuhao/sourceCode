package com.hao.mybatis.po;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author xuh
 * @date 2024/3/5
 */
public class Blog implements Serializable {

    private int id;

    private String title;

    private User author;

    private String body;

    private List<Comment> comment;

    Map<String, String> labels;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
}
