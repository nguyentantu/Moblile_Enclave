package model;

public class Engineers {
    private int id;
    private String name;
    private String username;
    private String email;
    private int totalEngineer;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return id +"\n" + name + "\n" + username +"\n" + email;
    }
}
