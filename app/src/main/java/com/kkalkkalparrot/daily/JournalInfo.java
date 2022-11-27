package com.kkalkkalparrot.daily;

public class JournalInfo {

    private String JournalName; // 저널 제목
    private String JournalContent; // 저널 내용
    private String timestamp; // 작성 시간
    private String[] GPS; // 작성 위치
    private String uid; // 작성자 아이디
    private String Journal_IMG; // 저널에 추가된 사진
    private String[] tag; // 작성 태그

    private String documentName;

    public JournalInfo(String JournalName) {
        this.JournalName = JournalName;
        //this.Journal_IMG = Journal_IMG;

    }

    public String getJournalName() {
        return JournalName;
    }

    public void setJournalName(String journalName) {
        JournalName = journalName;
    }

    public String getJournalContent() {
        return JournalContent;
    }

    public void setJournalContent(String journalContent) {
        JournalContent = journalContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String[] getGPS() {
        return GPS;
    }

    public void setGPS(String[] GPS) {
        this.GPS = GPS;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getJournal_IMG() {
        return Journal_IMG;
    }

    public void setJournal_IMG(String journal_IMG) {
        Journal_IMG = journal_IMG;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
