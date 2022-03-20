package com.revature.dao;

import com.revature.model.Account;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public Account getAccount(int id, int clientId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM accounts WHERE id = ? AND client_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, clientId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                int balance = rs.getInt("balance");
                String type = rs.getString("account_type");
                return new Account(id, clientId, balance, type);
            }
        }

        return null;
    }

    public List<Account> getAccounts(int clientId) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM accounts where client_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int balance = rs.getInt("balance");
                String type = rs.getString("account_type");
                accounts.add(new Account(id, clientId, balance, type));
            }
        }

        return accounts;
    }

    public List<Account> getAccounts(int clientId, int balanceBound, boolean upperBound) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql;
            if(upperBound)
                sql = "SELECT * FROM accounts where client_id = ? AND balance < ?";
            else
                sql = "SELECT * FROM accounts where client_id = ? AND balance > ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, balanceBound);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int balance = rs.getInt("balance");
                String type = rs.getString("account_type");
                accounts.add(new Account(id, clientId, balance, type));
            }
        }

        return accounts;
    }

    public List<Account> getAccounts(int clientId, int balanceLowerBound, int balanceUpperBound) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM accounts where client_id = ? AND balance > ? AND balance < ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            pstmt.setInt(2, balanceLowerBound);
            pstmt.setInt(3, balanceUpperBound);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int balance = rs.getInt("balance");
                String type = rs.getString("account_type");
                accounts.add(new Account(id, clientId, balance, type));
            }
        }

        return accounts;
    }

    public Account addAccount(Account account) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO accounts (client_id, balance, account_type) VALUES (?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, account.getClientId());
            pstmt.setInt(2, account.getBalance());
            pstmt.setString(3, account.getType());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            return new Account(id, account.getClientId(), account.getBalance(), account.getType());
        }
    }

    public boolean deleteAccount(int id, int clientId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM accounts WHERE id = ? AND client_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, clientId);

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }

    public boolean updateAccount(Account account) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE accounts " +
                    "SET balance = ?, " +
                    "account_type = ? " +
                    "WHERE id = ? AND client_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, account.getBalance());
            pstmt.setString(2, account.getType());
            pstmt.setInt(3, account.getId());
            pstmt.setInt(4, account.getClientId());

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }
}
