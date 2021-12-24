package com.example.lookdiary;

public class Dress {
    private String name;
    private String cost;
    private String brande;
    private String size;
    private String type;

    public Dress(){}

    public Dress(String name, String cost, String brande, String size, String type){
        this.brande = brande;
        this.cost = cost;
        this.size = size;
        this.name = name;
        this.type = type;
    }

    public String get(){
        String content=this.name+"\n"+this.cost+"\n"+this.brande+"\n"+this.size+
                "\n"+this.type;
        return content;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getBrande() {
        return brande;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setBrande(String brande) {
        this.brande = brande;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }
}
