package com.shy.beans;

/**
 * @author 石皓岩
 * @create 2020-06-18 12:14
 * 描述：
 */
public class Admin {
    private Integer id;
    private String account;
    private String password;

    public Admin(Integer id, String account, String password) {
        this.id = id;
        this.account = account;
        this.password = password;
    }

    public Admin() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
