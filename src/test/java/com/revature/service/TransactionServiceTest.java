package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.dao.TransactionDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.TransactionNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    private ClientDao mockClientDao;
    private AccountDao mockAccountDao;
    private TransactionDao mockTransactionDao;
    private TransactionService transactionService;

    @BeforeEach
    public void setup() throws SQLException {
        mockClientDao = mock(ClientDao.class);
        mockAccountDao = mock(AccountDao.class);
        mockTransactionDao = mock(TransactionDao.class);
        transactionService = new TransactionService(mockClientDao, mockAccountDao, mockTransactionDao);
        when(mockClientDao.getClient(eq(1)))
                .thenReturn(new Client(1, "John", "Doe", "1234 Abc Street", "2000-01-01"));
        when(mockAccountDao.getAccount(eq(1), eq(1)))
                .thenReturn(new Account(1, 1, 100, "Savings"));
    }

    @Test
    public void test_getTransaction_positive() throws SQLException, ClientNotFoundException, TransactionNotFoundException, AccountNotFoundException {
        when(mockTransactionDao.getTransaction(eq(1),  eq(1)))
                .thenReturn(new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));

        Transaction actual = transactionService.getTransaction("1", "1", "1");
        Transaction expected = new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_getTransaction_clientNotFound() throws SQLException {
        when(mockClientDao.getClient(eq(1)))
                .thenReturn(null);
        Assertions.assertThrows(ClientNotFoundException.class, () -> {
            transactionService.getTransaction("1",  "1",  "1");
        });
    }

    @Test
    public void test_getTransaction_accountNotFound() throws SQLException {
        when(mockAccountDao.getAccount(eq(1), eq(1)))
                .thenReturn(null);
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getTransaction("1",  "1",  "1");
        });
    }

    @Test
    public void test_getTransaction_transactionNotFound() {
        Assertions.assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.getTransaction("1",  "1",  "1");
        });
    }

    @Test
    public void test_getTransaction_invalidId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransaction("1",  "1",  "abc");
        });
    }

    @Test
    public void test_getTransaction_sqlException() throws SQLException {
        when(mockTransactionDao.getTransaction(eq(1),  eq(1)))
                .thenThrow(SQLException.class);
        Assertions.assertThrows(SQLException.class, () -> {
            transactionService.getTransaction("1",  "1", "1");
        });
    }

    @Test
    public void test_getTransactions_positive() throws SQLException, ClientNotFoundException, AccountNotFoundException {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));
        mockTransactions.add(new Transaction(2, 1, 100, "Withdrawal", "Test", "2000-01-01"));
        mockTransactions.add(new Transaction(3, 1, 100, "Deposit", "Payment", "2000-01-01"));

        when(mockTransactionDao.getTransactions(1)).thenReturn(mockTransactions);

        List<Transaction> actual = transactionService.getTransactions("1", "1");
        List<Transaction> expected = mockTransactions;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addTransaction_positive() throws SQLException, ClientNotFoundException, AccountNotFoundException {
        when(mockTransactionDao.addTransaction(new Transaction(0, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01")))
                .thenReturn(new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));

        Transaction actual = transactionService.addTransaction("1", "1", new Transaction(0, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));
        Transaction expected = new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void test_addTransaction_invalidType() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.addTransaction("1", "1",  new Transaction(1, 1, 100, "abc", "Internet Bill Payment - VISA", "2000-01-01"));
        });
    }

    @Test
    public void test_addTransaction_invalidAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.addTransaction("1", "1",  new Transaction(1, 1, -100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));
        });
    }

    @Test
    public void test_deleteTransaction_positive() throws SQLException, ClientNotFoundException, TransactionNotFoundException, AccountNotFoundException {
        when(mockTransactionDao.deleteTransaction(1, 1)).thenReturn(true);

        transactionService.deleteTransaction("1", "1", "1");
    }

    @Test
    public void test_deleteTransaction_transactionNotFound() throws SQLException {
        when(mockTransactionDao.deleteTransaction(1, 1)).thenReturn(false);

        Assertions.assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction("1", "1", "1");
        });
    }

    @Test
    public void test_updateTransaction_positive() throws SQLException, ClientNotFoundException, TransactionNotFoundException, AccountNotFoundException {
        when(mockTransactionDao.updateTransaction(new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"))).thenReturn(true);

        transactionService.updateTransaction("1", "1", "1", new Transaction(0, 0, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));
    }

    @Test
    public void test_updateTransaction_transactionNotFound() throws SQLException {
        when(mockTransactionDao.updateTransaction(new Transaction(1, 1, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"))).thenReturn(false);

        Assertions.assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.updateTransaction("1", "1", "1", new Transaction(0, 0, 100, "Withdrawal", "Internet Bill Payment - VISA", "2000-01-01"));
        });
    }
}
