package com.kkalkkalparrot.daily;

public class JournalInfo {

    private String JournalName;
    private String documentName;

    public JournalInfo(String JournalName, String documentName) {
        this.JournalName = JournalName;
        this.documentName = documentName;
    }

    public String getJournalName() {
        return JournalName;
    }

    public void setJournalName(String JournalName) {
        this.JournalName = JournalName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
