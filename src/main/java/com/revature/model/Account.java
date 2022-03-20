package com.revature.model;

import java.util.Objects;

public class Account {
    private int id;
    private int clientId;
    private int balance;
    private String type;

    public Account() {
    }

    public Account(int id, int clientId, int balance, String type) {
        this.id = id;
        this.clientId = clientId;
        this.balance = balance;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && clientId == account.clientId && balance == account.balance && Objects.equals(type, account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, balance, type);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }
}
