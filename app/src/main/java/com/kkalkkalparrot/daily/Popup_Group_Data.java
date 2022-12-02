package com.kkalkkalparrot.daily;

public class Popup_Group_Data {

    String Group_id;
    String Group_Description;
    String Uid;
    String Document_id;

    public Popup_Group_Data(String group_id, String group_Description, String uid, String document_id) {
        this.Group_id = group_id;
        this.Group_Description = group_Description;
        this.Uid = uid;
        this.Document_id = document_id;
    }

    public String getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(String group_id) {
        Group_id = group_id;
    }

    public String getGroup_Description() {
        return Group_Description;
    }

    public void setGroup_Description(String group_Description) {
        Group_Description = group_Description;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getDocument_id() {
        return Document_id;
    }

    public void setDocument_id(String document_id) {
        Document_id = document_id;
    }
}
