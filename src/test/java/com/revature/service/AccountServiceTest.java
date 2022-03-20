package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private ClientDao mockClientDao;
    private AccountDao mockAccountDao;
    private AccountService accountService;

    @BeforeEach
    public void setup() throws SQLException {
        mockClientDao = mock(ClientDao.class);
        mockAccountDao = mock(AccountDao.class);
        accountService = new AccountService(mockClientDao, mockAccountDao);
        when(mockClientDao.getClient(eq(1)))
                .thenReturn(new Client(1, "John", "Doe", "1234 Abc Street", null));
    }

    @Test
    public void test_getAccount_positive() throws SQLException, ClientNotFoundException, AccountNotFoundException {
        when(mockAccountDao.getAccount(eq(1), eq(1)))
                .thenReturn(new Account(1, 1, 100, "Savings"));

        Account actual = accountService.getAccount("1", "1");
        Account expected = new Account(1, 1, 100, "Savings");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getAccount_clientNotFound() throws SQLException {
        when(mockClientDao.getClient(eq(1)))
                .thenReturn(null);
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            accountService.getAccount("1",  "1");
        });
    }

    @Test
    public void test_getAccount_accountNotFound() {
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccount("1",  "1");
        });
    }

    @Test
    public void test_getAccount_invalidId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accountService.getAccount("1",  "abc");
        });
    }

    @Test
    public void test_getAccount_sqlException() throws SQLException {
        when(mockAccountDao.getAccount(eq(1), eq(1)))
                .thenThrow(SQLException.class);
        Assertions.assertThrows(SQLException.class, () -> {
            accountService.getAccount("1",  "1");
        });
    }

    @Test
    public void test_getAccountsClientId_positive() throws SQLException, ClientNotFoundException {
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 1, 100, "Savings"));
        mockAccounts.add(new Account(2, 1, 200, "Checking"));
        mockAccounts.add(new Account(3, 1, 1000, "Savings"));

        when(mockAccountDao.getAccounts(eq(1))).thenReturn(mockAccounts);

        List<Account> actual = accountService.getAccounts("1");
        List<Account> expected = mockAccounts;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getAccountsClientIdOneBalanceParam_positive() throws SQLException, ClientNotFoundException {
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 1, 100, "Savings"));
        mockAccounts.add(new Account(2, 1, 200, "Checking"));
        mockAccounts.add(new Account(3, 1, 1000, "Savings"));

        when(mockAccountDao.getAccounts(eq(1), eq(10), eq(false))).thenReturn(mockAccounts);

        List<Account> actual = accountService.getAccounts("1", "10", false);
        List<Account> expected = mockAccounts;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getAccountsBothBalanceParams_positive() throws SQLException, ClientNotFoundException {
        List<Account> mockAccounts = new ArrayList<>();
        mockAccounts.add(new Account(1, 1, 100, "Savings"));
        mockAccounts.add(new Account(2, 1, 200, "Checking"));
        mockAccounts.add(new Account(3, 1, 1000, "Savings"));

        when(mockAccountDao.getAccounts(eq(1), eq(10), eq(2000))).thenReturn(mockAccounts);

        List<Account> actual = accountService.getAccounts("1", "10", "2000");
        List<Account> expected = mockAccounts;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addAccount_positive() throws SQLException, ClientNotFoundException {
        when(mockAccountDao.addAccount(new Account(0, 1, 100, "Savings")))
                .thenReturn(new Account(1, 1, 100, "Savings"));

        Account actual = accountService.addAccount("1", new Account(0, 0, 100, "Savings"));
        Account expected = new Account(1, 1, 100, "Savings");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addAccount_invalidAccountType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accountService.addAccount("1", new Account(0, 0, 100, "abc"));
        });
    }

    @Test
    public void test_addAccount_invalidBalance() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            accountService.addAccount("1", new Account(0, 0, -100, "Savings"));
        });
    }

    @Test
    public void test_deleteAccount_positive() throws SQLException, ClientNotFoundException, AccountNotFoundException {
        when(mockAccountDao.deleteAccount(eq(1), eq(1))).thenReturn(true);

        accountService.deleteAccount("1", "1");
    }

    @Test
    public void test_deleteAccount_accountNotFound() throws SQLException {
        when(mockAccountDao.deleteAccount(eq(1), eq(1))).thenReturn(false);

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteAccount("1", "1");
        });
    }

    @Test
    public void test_updateAccount_positive() throws SQLException, ClientNotFoundException, AccountNotFoundException {
        when(mockAccountDao.updateAccount(new Account(1, 1, 200, "Savings")))
                .thenReturn(true);

        accountService.updateAccount("1", "1", new Account(0, 0, 200, "Savings"));
    }

    @Test
    public void test_updateAccount_accountNotFound() throws SQLException {
        when(mockAccountDao.updateAccount(new Account(1, 1, 200, "Savings")))
                .thenReturn(false);

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateAccount("1", "1", new Account(0, 0, 200, "Savings"));
        });
    }
}
