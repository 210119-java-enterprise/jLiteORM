package com.revature.testModels;

import com.revature.annotations.Column;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.Table;

import java.util.Objects;

@Table(tableName = "checking_accounts")
public class CheckingAccount {

    //Column is also part of a composite primary key in user_checking_accounts
    @JoinColumn(columnName = "checking_id")
    @Id(columnName = "checking_id")
    private int id;

    @Column(columnName = "balance")
    private double balance;


    public CheckingAccount(){super();}

    public CheckingAccount(double balance) {

        this.balance = balance;
    }

    public CheckingAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckingAccount that = (CheckingAccount) o;
        return id == that.id && Double.compare(that.balance, balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance);
    }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }

    public String balanceView(){

        String strDouble = String.format("%.2f", balance);
        //System.out.println(strDouble); // print 2.00


        return "Balance: $"+strDouble;
    }

}
