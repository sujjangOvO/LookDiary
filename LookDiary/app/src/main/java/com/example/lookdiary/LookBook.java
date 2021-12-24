package com.example.lookdiary;

public class LookBook {
    String hat, top, pants, shoes, acc1, acc2, acc3;

    /*
    public LookBook(String hat, String top, String pants, String shoes) { //악세사리 0개
        this.hat = hat;
        this.top = top;
        this.pants = pants;
        this.shoes = shoes;
    }

    // dress인 경우 pants를 null로
    public LookBook(String hat, String top, String pants, String shoes, String acc1) { //악세사리 1개
        this.hat = hat;
        this.top = top;
        this.pants = pants;
        this.shoes = shoes;
        this.acc1 = acc1;
    }

    public LookBook(String hat, String top, String pants, String shoes, String acc1, String acc2) { //악세사리 2개
        this.hat = hat;
        this.top = top;
        this.pants = pants;
        this.shoes = shoes;
        this.acc1 = acc1;
        this.acc2 = acc2;
    } */

    public LookBook(String hat, String top, String pants, String shoes, String acc1, String acc2, String acc3) { //악세사리 3개
        this.hat = hat;
        this.top = top;
        this.pants = pants;
        this.shoes = shoes;
        this.acc1 = acc1;
        this.acc2 = acc2;
        this.acc3 = acc3;
    }

    public String get(){
        String content = this.hat+"\n"+this.top+"\n"+this.pants+"\n"+this.shoes+
                    "\n"+this.acc1+"\n"+this.acc2+"\n"+this.acc3;
        return content;
    }

    public String getAcc1() {
        return acc1;
    }

    public void setAcc1(String acc1) {
        this.acc1 = acc1;
    }

    public String getAcc2() {
        return acc2;
    }

    public void setAcc2(String acc2) {
        this.acc2 = acc2;
    }

    public String getAcc3() {
        return acc3;
    }

    public void setAcc3(String acc3) {
        this.acc3 = acc3;
    }

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getPants() {
        return pants;
    }

    public void setPants(String pants) {
        this.pants = pants;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }


}
