package model;

public class Person {
    private String name, skype, avatar, role;
    private int viewType, id, experienceYear;
    private String firstName, lastName, email;
    int salary;
    int idPerson;

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

    public Person(int idPerson, String firstName, String lastName, String email, String avatar, String role, int salary, int experienceYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatar = avatar;
        this.role = role;
        this.salary = salary;
        this.experienceYear = experienceYear;
        this.idPerson = idPerson;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
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
