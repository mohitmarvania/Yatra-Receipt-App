package com.example.yatra_receipt;

public class pdfClass {
    // Store pdf name and url in realtime database.
    public String name, url;

    public pdfClass() {
    }

    public pdfClass(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
