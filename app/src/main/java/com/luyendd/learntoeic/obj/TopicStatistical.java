package com.luyendd.learntoeic.obj;

public class TopicStatistical {
    private int id;

    private String name;

    private String translate;

    private int favourite;

    private int level1;

    private int level2;

    private int level3;

    private int isActive;

    private String learnPerTotal;

    public TopicStatistical(int id, String name, String translate, int favourite, int level1,
                            int level2, int level3, int isActive, String learnPerTotal) {
        this.id = id;
        this.name = name;
        this.translate = translate;
        this.favourite = favourite;
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        this.isActive = isActive;
        this.learnPerTotal = learnPerTotal;
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

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getLearnPerTotal() {
        return learnPerTotal;
    }

    public void setLearnPerTotal(String learnPerTotal) {
        this.learnPerTotal = learnPerTotal;
    }
}
