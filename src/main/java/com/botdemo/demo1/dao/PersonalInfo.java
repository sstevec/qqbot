package com.botdemo.demo1.dao;

public class PersonalInfo {
    private String group_id;
    private String user_id;
    private String nickname;
    private String card; // 群备注
    private String sex;
    private int age;
    private String area;
    private String join_time;
    private String last_send_time;
    private String level;
    private String role;
    private boolean unfriendly;
    private String title;
    private String title_expire_time;
    private boolean card_changeable;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    public String getLast_send_time() {
        return last_send_time;
    }

    public void setLast_send_time(String last_send_time) {
        this.last_send_time = last_send_time;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isUnfriendly() {
        return unfriendly;
    }

    public void setUnfriendly(boolean unfriendly) {
        this.unfriendly = unfriendly;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_expire_time() {
        return title_expire_time;
    }

    public void setTitle_expire_time(String title_expire_time) {
        this.title_expire_time = title_expire_time;
    }

    public boolean isCard_changeable() {
        return card_changeable;
    }

    public void setCard_changeable(boolean card_changeable) {
        this.card_changeable = card_changeable;
    }
}
