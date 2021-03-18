package com.muchlish.scan_ai.activity.entity;

public class Barcode {
    int id;
    String barcode, type, desc;

    public Barcode(int id, String barcode, String type, String desc) {
        this.id = id;
        this.barcode = barcode;
        this.type = type;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Barcode{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
