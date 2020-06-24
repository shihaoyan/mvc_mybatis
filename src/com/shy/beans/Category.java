package com.shy.beans;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:50
 * 描述：
 */
public class Category {

    private Integer cid;
    private String cname;

    public Category(Integer cid, String cname) {
        this.cid = cid;
        this.cname = cname;
    }

    public Category() {
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid=" + cid +
                ", cname=" + cname +
                '}';
    }
}
