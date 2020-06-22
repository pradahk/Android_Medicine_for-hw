package com.example.androidlogin;


public class FormDrug {
    //리스트에 띄울 목록
    private String name; //품목명
    private String company; // 업소명
    private String image;//이미지 주소
    private String category; //분류명
    private String mark; // 전문일반구분

    //검색할때 사용, 리스트에 띄우지 않음
    private String shape; //모양
    private String color; //색상
    private String type; //제형
    private String markfront; // 식별 표시 앞
    private String markback; // 식별 표시 뒤



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarkfront() {
        return markfront;
    }

    public void setMarkfront(String markfront) {
        this.markfront = markfront;
    }

    public String getMarkback() {
        return markback;
    }

    public void setMarkback(String markback) {
        this.markback = markback;
    }
}
