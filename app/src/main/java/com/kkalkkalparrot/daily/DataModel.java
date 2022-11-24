package com.kkalkkalparrot.daily;

public class DataModel {
    String title;
    String user;
    String image_path;
    String gid;
    String fid;
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

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.fid = gid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public DataModel(String title, String image_path, String gid, String fid) {
        if (title.length()>10){
            this.title = title.substring(0,10)+"...";
        }
        else{
            this.title = title;
        }
        this.image_path = image_path;
        this.gid = gid;
        this.fid = fid;
    }
}
