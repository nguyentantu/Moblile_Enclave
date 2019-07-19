package model;

public class Engineers {
    private int id;
    private String name;
    private String username;
    private String skype;
    private int totalEngineer;
    private String avatar;
    public int getTotalEngineer() {
        return totalEngineer;
    }

    public void setTotalEngineer(int totalEngineer) {
        this.totalEngineer = totalEngineer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String email) {
        this.skype = email;
    }

    @Override
    public String toString() {
        return id +"\n" + name + "\n" + username +"\n" + skype;
    }
}
