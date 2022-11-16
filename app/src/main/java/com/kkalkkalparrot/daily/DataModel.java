package com.kkalkkalparrot.daily;

public class DataModel {
    String title;
    String user;
    String image_path;
    int num;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public DataModel(String title, String image_path) {
        this.title = title;
        this.image_path = image_path;
    }
}
