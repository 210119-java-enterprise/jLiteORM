package com.revature.reflection.testModels;


import java.util.Objects;
import com.revature.annotations.*;

@Table(tableName="app_user")
public class AppUser {

    //Is this field annotated 'Id' or 'GenerateValue' or plain @Column?
    @Id(columnName="id")
    private int id;

    @Column(columnName="user_name")
    private String username;

    @Column(columnName="password")
    private String password;

    @Column(columnName="first_Name")
    private String firstName;

    @Column(columnName="last_Name")
    private String lastName;


    public AppUser(){
        super();
    }

    public AppUser(AppUser copy){

        this.id = copy.id;
        this.username = copy.username;
        this.password = copy.password;
        this.firstName = copy.firstName;
        this.lastName = copy.lastName;
    }

    public AppUser(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AppUser(int id, String username, String password, String firstName, String lastName) {
        this(username, password, firstName, lastName);
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return id == appUser.id && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password) && Objects.equals(firstName, appUser.firstName) && Objects.equals(lastName, appUser.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, firstName, lastName);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
