package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientServiceTest {

    private ClientDao mockDao;
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        mockDao = mock(ClientDao.class);
        clientService = new ClientService(mockDao);
    }

    @Test
    public void test_getClients_positive() throws SQLException {
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"));
        mockClients.add(new Client(1, "Jane", "Doe", "1234 Abc Street", "2000-01-01"));
        mockClients.add(new Client(1, "Johnny", "Carson", "4321 Cba Drive", "2000-01-01"));

        when(mockDao.getClients()).thenReturn(mockClients);

        List<Client> actual = clientService.getClients();
        List<Client> expected = mockClients;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getClient_positive() throws SQLException, ClientNotFoundException {
        when(mockDao.getClient(eq(1)))
                .thenReturn(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"));

        Client actual = clientService.getClient("1");
        Client expected = new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getClient_clientNotFound() {
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClient("10");
        });
    }

    @Test
    public void test_getClient_invalidId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.getClient("abc");
        });
    }

    @Test
    public void test_getClient_sqlException() throws SQLException {
        when(mockDao.getClient(anyInt())).thenThrow(SQLException.class);
        Assertions.assertThrows(SQLException.class, () -> {
            clientService.getClient("5");
        });
    }

    @Test
    public void test_addClient_positive() throws SQLException {
        when(mockDao.addClient(eq(new Client(0, "John", "Doe", "1234 Abc Street", "2000-01-01"))))
                .thenReturn(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"));

        Client actual = clientService.addClient(new Client(0, "John", "Doe", "1234 Abc Street", "2000-01-01"));
        Client expected = new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addClient_positiveWithLeadingAndTrailingSpacesInNameAndAddress() throws SQLException {
        when(mockDao.addClient(eq(new Client(0, "John", "Doe", "1234 Abc Street", "2000-01-01"))))
                .thenReturn(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"));

        Client actual = clientService.addClient(new Client(0, "  John   ", "  Doe ", "  1234 Abc Street   ", "2000-01-01"));
        Client expected = new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addClient_nonAlphabeticalCharactersInFirstName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(1, "John1", "Doe", "1234 Abc Street", "2000-01-01"));
        });
    }

    @Test
    public void test_addClient_nonAlphabeticalCharactersInLastName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(1, "John", "Doe2", "1234 Abc Street", "2000-01-01"));
        });
    }

    @Test
    public void test_addClient_invalidAddress() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.addClient(new Client(1, "John", "Doe", "Invalid", "2000-01-01"));
        });
    }

    @Test
    public void test_deleteClient_positive() throws SQLException, ClientNotFoundException {
        when(mockDao.deleteClient(eq(1))).thenReturn(true);

        clientService.deleteClient("1");
    }

    @Test
    public void test_deleteClient_clientNotFound() throws SQLException {
        when(mockDao.deleteClient(eq(1))).thenReturn(false);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteClient("1");
        });
    }

    @Test
    public void test_updateClient_positive() throws SQLException, ClientNotFoundException {
        when(mockDao.updateClient(eq(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"))))
                .thenReturn(true);

        clientService.updateClient("1", new Client(0, "John", "Doe", "1234 Abc Street", "2000-01-01"));
    }

    @Test
    public void test_updateClient_clientNotFound() throws SQLException {
        when(mockDao.updateClient(eq(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"))))
                .thenReturn(false);

        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            clientService.updateClient("1", new Client(0, "John", "Doe", "1234 Abc Street", "2000-01-01"));
        });
    }
}
