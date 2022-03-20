package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Transaction;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public Transaction getTransaction(int id, int accountId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM transactions WHERE id = ? AND account_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, accountId);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                int amount = rs.getInt("amount");
                String type = rs.getString("transaction_type");
                String description = rs.getString("description");
                String date = rs.getDate("transaction_date").toString();
                return new Transaction(id, accountId, amount, type, description, date);
            }
        }

        return null;
    }

    public List<Transaction> getTransactions(int accountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM transactions where account_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, accountId);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int amount = rs.getInt("amount");
                String type = rs.getString("transaction_type");
                String description = rs.getString("description");
                String date = rs.getDate("transaction_date").toString();
                transactions.add(new Transaction(id, accountId, amount, type, description, date));
            }
        }

        return transactions;
    }

    public List<Transaction> getTransactions(int accountId, String descriptionContains) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try(Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM transactions where account_id = ? AND description LIKE ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            pstmt.setString(2, "%" + descriptionContains + "%");

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int amount = rs.getInt("amount");
                String type = rs.getString("transaction_type");
                String description = rs.getString("description");
                String date = rs.getDate("transaction_date").toString();
                transactions.add(new Transaction(id, accountId, amount, type, description, date));
            }
        }

        return transactions;
    }

    public Transaction addTransaction(Transaction transaction) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO transactions (account_id, amount, transaction_type, description, transaction_date) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, transaction.getAccountId());
            pstmt.setInt(2, transaction.getAmount());
            pstmt.setString(3, transaction.getType());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setDate(5, Date.valueOf(transaction.getDate()));

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            return new Transaction(id, transaction.getAccountId(), transaction.getAmount(), transaction.getType(), transaction.getDescription(), transaction.getDate());
        }
    }

    public boolean deleteTransaction(int id, int accountId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM transactions WHERE id = ? AND account_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, accountId);

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }

    public boolean updateTransaction(Transaction transaction) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE transactions " +
                    "SET amount = ?, " +
                    "transaction_type = ?, " +
                    "description = ?, " +
                    "transaction_date = ? " +
                    "WHERE id = ? AND account_id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, transaction.getAmount());
            pstmt.setString(2, transaction.getType());
            pstmt.setString(3, transaction.getDescription());
            pstmt.setDate(4, Date.valueOf(transaction.getDate()));
            pstmt.setInt(5, transaction.getId());
            pstmt.setInt(6, transaction.getAccountId());

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }
}
