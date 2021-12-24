package com.example.lookdiary;

public class dressName {
    int cnt;
    String dressName;

    dressName() { }

    dressName(int cnt, String dressName) {
        this.cnt = cnt;
        this.dressName = dressName;
    }

    public int getCnt() {
        return cnt;
    }

    public String getDressName() {
        return dressName;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public void setDressName(String dressName) {
        this.dressName = dressName;
    }
}



