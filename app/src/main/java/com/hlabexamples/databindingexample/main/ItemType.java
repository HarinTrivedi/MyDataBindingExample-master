package com.hlabexamples.databindingexample.main;

enum  ItemType {
    HOME(0), FAV(1);

    ItemType(int type) {
        this.type = type;
    }

    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
