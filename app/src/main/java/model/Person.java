package model;

public class Person {
    private String name, skype, avatar;
    private int viewType, id;

    public Person() {
    }

    public Person(String name, String skype, int viewType, int id, String avatar) {
        this.name = name;
        this.skype = skype;
        this.viewType = viewType;
        this.id = id;
        this.avatar = avatar;
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
