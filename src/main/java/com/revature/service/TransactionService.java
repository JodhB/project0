package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.dao.TransactionDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.exception.TransactionNotFoundException;
import com.revature.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {

    private static Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private ClientDao clientDao;
    private AccountDao accountDao;
    private TransactionDao transactionDao;

    public TransactionService() {
        clientDao = new ClientDao();
        accountDao = new AccountDao();
        transactionDao = new TransactionDao();
    }

    public TransactionService(ClientDao mockClientDao, AccountDao mockAccountDao, TransactionDao mockTransactionDao) {
        clientDao = mockClientDao;
        accountDao = mockAccountDao;
        transactionDao = mockTransactionDao;
    }

    public Transaction getTransaction(String idString, String clientIdString, String accountIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException, TransactionNotFoundException {
        logger.info("getTransaction method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            int id = Integer.parseInt(idString);

            Transaction t = transactionDao.getTransaction(id, accountId);
            if(t == null) {
                throw new TransactionNotFoundException("Transaction with id " + id + " was not found for account with id " + accountId);
            }

            return t;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client, account, and transaction must be valid ints");
        }
    }

    public List<Transaction> getTransactions(String clientIdString, String accountIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("getTransactions method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            return transactionDao.getTransactions(accountId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    public List<Transaction> getTransactions(String clientIdString, String accountIdString, String descriptionContains) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("getTransactions method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            return transactionDao.getTransactions(accountId, descriptionContains);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    public Transaction addTransaction(String clientIdString, String accountIdString, Transaction t) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("addTransaction method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            validateTransactionInformation(t);

            t.setAccountId(accountId);
            return transactionDao.addTransaction(t);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    public void updateTransaction(String idString, String clientIdString, String accountIdString, Transaction t) throws SQLException, ClientNotFoundException, AccountNotFoundException, TransactionNotFoundException {
        logger.info("updateTransaction method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            int id = Integer.parseInt(idString);

            validateTransactionInformation(t);

            t.setId(id);
            t.setAccountId(accountId);
            if(!transactionDao.updateTransaction(t)) {
                throw new TransactionNotFoundException("Transaction with id " + id + " was not found for account with id " + accountId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client, account, and transaction must be valid ints");
        }
    }

    public void deleteTransaction(String idString, String clientIdString, String accountIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException, TransactionNotFoundException {
        logger.info("deleteTransaction method called");
        try {
            int accountId = checkForClientAndAccount(clientIdString, accountIdString);
            int id = Integer.parseInt(idString);
            if(!transactionDao.deleteTransaction(id, accountId)) {
                throw new TransactionNotFoundException("Transaction with id " + id + " was not found for account with id " + accountId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client, account, and transaction must be valid ints");
        }
    }

    private int checkForClientAndAccount(String clientIdString, String accountIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        int clientId = Integer.parseInt(clientIdString);

        if (clientDao.getClient(clientId) == null) {
            throw new ClientNotFoundException("Client with id " + clientId + " was not found");
        }

        int accountId = Integer.parseInt(accountIdString);

        if (accountDao.getAccount(accountId, clientId) == null) {
            throw new AccountNotFoundException("Account with id " + accountId + " was not found for client with id " + clientId);
        }

        return accountId;
    }

    private void validateTransactionInformation(Transaction t) {
        t.setType(t.getType().trim());
        t.setDescription(t.getDescription().trim());

        if(!(t.getType().equals("Withdrawal") || t.getType().equals("Deposit"))) {
            throw new IllegalArgumentException("Account type must be one of Withdrawal or Deposit. Input was " + t.getType());
        }

        if(t.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0. Input was " + t.getAmount());
        }
    }
}
