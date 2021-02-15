package com.revature.testModels;


import java.util.Objects;
import com.revature.annotations.*;

@Table(tableName="app_users")
public class AppUser {

    //Column is also part of a composite primary key in user_checking_accounts
    @JoinColumn(columnName = "user_id")
    @Id(columnName="user_id")
    @Column(columnName="user_id")
    //Changed to userId
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
    @Getter(getterName = "getUserId")
    public int getUserId() {
        return userId;
    }

    @Setter(setterName = "setUserId")
    @SetterId(setterName = "setUserId")
    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Getter(getterName = "getUsername")
    public String getUsername() {
        return username;
    }

    @Setter(setterName = "setUsername")
    public void setUsername(String username) {
        this.username = username;
    }


    @Getter(getterName = "getPassword")
    public String getPassword() {
        return password;
    }

    @Setter(setterName = "setPassword")
    public void setPassword(String password) {
        this.password = password;
    }


    @Getter(getterName = "getFirstName")
    public String getFirstName() {
        return firstName;
    }

    @Setter(setterName = "setFirstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    @Getter(getterName = "getLastName")
    public String getLastName() {
        return lastName;
    }

    @Setter(setterName = "setLastName")
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
