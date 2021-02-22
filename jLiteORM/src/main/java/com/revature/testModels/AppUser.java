package com.revature.testModels;


import java.util.Objects;
import com.revature.annotations.*;

@Table(tableName="app_users")
public class AppUser {

    //Column is also part of a composite primary key in user_checking_accounts
    @Serial(columnName="user_id")
    @PrimaryKey(columnName="user_id")
    @JoinColumn(columnName = "user_id")
    @Id(columnName="user_id")
    private int userId;

    @Column(columnName="username")
    private String username;

    @Column(columnName="password")
    private String password;

    @Column(columnName="first_name")
    private String firstName;


    @Column(columnName="last_name")
    private String lastName;


    public AppUser(){
        super();
    }

    public AppUser(AppUser copy){

        this.userId = copy.userId;
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

    public AppUser(int userId, String username, String password, String firstName, String lastName) {
        this(username, password, firstName, lastName);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        return userId == appUser.userId && Objects.equals(username, appUser.username) && Objects.equals(password, appUser.password) && Objects.equals(firstName, appUser.firstName) && Objects.equals(lastName, appUser.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, firstName, lastName);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
