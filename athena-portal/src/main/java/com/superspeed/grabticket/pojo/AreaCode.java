package com.superspeed.grabticket.pojo;

public class AreaCode {
    // 拼音首字母
    private String abbrLowerSpelling;
    // 拼音首字母
    private String name;
    // 拼音大写
    private String upperSpelling;
    // 排序
    private String sort;

    public AreaCode() {}

    public AreaCode(String abbrLowerSpelling, String name, String upperSpelling, String sort) {
        this.abbrLowerSpelling = abbrLowerSpelling;
        this.name = name;
        this.upperSpelling = upperSpelling;
        this.sort = sort;
    }

    public String getAbbrLowerSpelling() {
        return abbrLowerSpelling;
    }

    public void setAbbrLowerSpelling(String abbrLowerSpelling) {
        this.abbrLowerSpelling = abbrLowerSpelling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpperSpelling() {
        return upperSpelling;
    }

    public void setUpperSpelling(String upperSpelling) {
        this.upperSpelling = upperSpelling;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "AreaCode{" +
                "abbrLowerSpelling='" + abbrLowerSpelling + '\'' +
                ", name='" + name + '\'' +
                ", upperSpelling='" + upperSpelling + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }
}
