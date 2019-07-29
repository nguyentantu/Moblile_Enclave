package model;

public class Teams {
    private String name;
    private String projectName;
    private int totalMember;
    private int id;

    public Teams() {
    }

    public Teams(String name, String projectName, int totalMember, int id) {
        this.name = name;
        this.projectName = projectName;
        this.totalMember = totalMember;
        this.id = id;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(int totalMember) {
        this.totalMember = totalMember;
    }
}
