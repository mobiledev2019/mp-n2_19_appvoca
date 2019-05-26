package com.luyendd.learntoeic.obj;

public class Topic {
    private int id;
    private String name;
    private String translate;
    private int favourite;
    private int pass;
    private int notPass;


    public Topic(int id, String name, String translate, int favourite, int pass, int notPass) {
        this.id = id;
        this.name = name;
        this.translate = translate;
        this.favourite = favourite;
        this.pass = pass;
        this.notPass = notPass;
    }

    public Topic(int id, String name, String translate, int favourite) {
        this.id = id;
        this.name = name;
        this.translate = translate;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getNotPass() {
        return notPass;
    }

    public void setNotPass(int notPass) {
        this.notPass = notPass;
    }


}
