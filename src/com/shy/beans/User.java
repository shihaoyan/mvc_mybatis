package com.shy.beans;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:12
 * 描述：
 */
public class User {
    private Integer uid;
    private Integer account;
    private String username;
    private String password;
    private String sex;
    private String phone;
    private Integer state;

    public User(Integer uid, Integer account, String username, String password, String sex, String phone, Integer state) {
        this.uid = uid;
        this.account = account;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.phone = phone;
        this.state = state;
    }

    public User() {
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", account=" + account +
                ", useranme='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", state=" + state +
                '}';
    }
}
