package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    public Client getClient(int id) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM clients WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String date = rs.getDate("date_of_birth").toString();
                return new Client(id, firstName, lastName, address, date);
            }
        }

        return null;
    }

    public List<Client> getClients() throws SQLException {
        List<Client> clients = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "SELECT * FROM clients";

            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String address = rs.getString("address");
                String date = rs.getDate("date_of_birth").toString();
                clients.add(new Client(id, firstName, lastName, address, date));
            }
        }

        return clients;
    }

    public Client addClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO clients (first_name, last_name, address, date_of_birth) VALUES (?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getAddress());
            pstmt.setDate(4, Date.valueOf(client.getDateOfBirth()));

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            return new Client(id, client.getFirstName(), client.getLastName(), client.getAddress(), client.getDateOfBirth());
        }
    }

    public boolean deleteClient(int id) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM clients WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }

    public boolean updateClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE clients " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "address = ?, " +
                    "date_of_birth = ? " +
                    "WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setString(3, client.getAddress());
            pstmt.setDate(4, Date.valueOf(client.getDateOfBirth()));
            pstmt.setInt(5, client.getId());

            if(pstmt.executeUpdate() == 1)
                return true;
        }

        return false;
    }
}
