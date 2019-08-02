package model;

public class Person {
    private String name, skype, avatar, role;
    private int viewType, id, experienceYear;

    public Person() {
    }

    public Person(String name, String skype, int viewType, int id, String avatar, int experienceYear) {
        this.name = name;
        this.skype = skype;
        this.viewType = viewType;
        this.id = id;
        this.avatar = avatar;
        this.experienceYear = experienceYear;
    }
    public Person(String name, String skype, int id, String avatar, String role) {
        this.name = name;
        this.skype = skype;
        this.id = id;
        this.avatar = avatar;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getExperienceYear() {
        return experienceYear;
    }

    public void setExperienceYear(int experienceYear) {
        this.experienceYear = experienceYear;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getSkype() {
        return skype;
    }

    public void setSkype(String position) {
        this.skype = skype;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
