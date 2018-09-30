package com.example.matsal.todolist;

import java.util.Objects;

public class NoteLists {
    private String content;
    private Integer id;
    private Integer position;

    public NoteLists(){

    }
    public NoteLists(String content, Integer id) {
        this.content = content;
        this.id = id;
    }
    public NoteLists(Integer id, Integer position){
        this.id = id;
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteLists lista = (NoteLists) o;
        return Objects.equals(content, lista.content) &&
                Objects.equals(id, lista.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(content, id);
    }

    //return only content, because I want to show only the content of object - note
    @Override
    public String toString() {
        return content;
    }
}
