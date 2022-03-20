package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;


public class AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    private ClientDao clientDao;
    private AccountDao accountDao;

    public AccountService() {
        clientDao = new ClientDao();
        accountDao = new AccountDao();
    }

    public AccountService(ClientDao mockClientDao, AccountDao mockAccountDao) {
        clientDao = mockClientDao;
        accountDao = mockAccountDao;
    }

    public Account getAccount(String idString, String clientIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("getAccount method called");
        try {
            int clientId = checkForClient(clientIdString);
            int id = Integer.parseInt(idString);

            Account a = accountDao.getAccount(id, clientId);
            if (a == null) {
                throw new AccountNotFoundException("Account with id " + id + " was not found for client with id " + clientId);
            }

            return a;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    public List<Account> getAccounts(String clientIdString) throws SQLException, ClientNotFoundException {
        logger.info("getAccounts method called");
        try {
            int clientId = checkForClient(clientIdString);

            return accountDao.getAccounts(clientId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }

    public List<Account> getAccounts(String clientIdString, String balanceBoundString, boolean upperBound) throws SQLException, ClientNotFoundException {
        logger.info("getAccounts method called");
        try {
            int clientId = checkForClient(clientIdString);
            int balanceBound = Integer.parseInt(balanceBoundString);

            return accountDao.getAccounts(clientId, balanceBound, upperBound);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client id and balance params must be valid ints");
        }
    }

    public List<Account> getAccounts(String clientIdString, String balanceLowerBoundString, String balanceUpperBoundString) throws SQLException, ClientNotFoundException {
        logger.info("getAccounts method called");
        try {
            int clientId = checkForClient(clientIdString);
            int balanceLowerBound = Integer.parseInt(balanceLowerBoundString);
            int balanceUpperBound = Integer.parseInt(balanceUpperBoundString);

            return accountDao.getAccounts(clientId, balanceLowerBound, balanceUpperBound);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client id and balance params must be valid ints");
        }
    }

    public Account addAccount(String clientIdString, Account a) throws SQLException, ClientNotFoundException {
        logger.info("addAccount method called");
        try {
            int clientId = checkForClient(clientIdString);

            validateAccountInformation(a);

            a.setClientId(clientId);
            return accountDao.addAccount(a);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }

    }

    public void updateAccount(String idString, String clientIdString, Account a) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("updateAccount method called");
        try {
            int clientId = checkForClient(clientIdString);
            int id = Integer.parseInt(idString);

            validateAccountInformation(a);

            a.setId(id);
            a.setClientId(clientId);
            if (!accountDao.updateAccount(a)) {
                throw new AccountNotFoundException("Account with id " + id + " was not found for client with id " + clientId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    public void deleteAccount(String idString, String clientIdString) throws SQLException, ClientNotFoundException, AccountNotFoundException {
        logger.info("deleteAccount method called");
        try {
            int clientId = checkForClient(clientIdString);
            int id = Integer.parseInt(idString);
            if (!accountDao.deleteAccount(id, clientId)) {
                throw new AccountNotFoundException("Account with id " + id + " was not found for client with id " + clientId);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ids provided for client and account must be valid ints");
        }
    }

    private int checkForClient(String clientIdString) throws SQLException, ClientNotFoundException {
        int clientId = Integer.parseInt(clientIdString);

        if (clientDao.getClient(clientId) == null) {
            throw new ClientNotFoundException("Client with id " + clientId + " was not found");
        }

        return clientId;
    }

    private void validateAccountInformation(Account a) {
        a.setType(a.getType().trim());

        if(!(a.getType().contains("Checking") || a.getType().contains("Savings"))) {
            throw new IllegalArgumentException("Account type must contain one of Checking or Savings. Input was " + a.getType());
        }

        if(a.getBalance() < 0) {
            throw new IllegalArgumentException("A negative balance is not valid. Balance provided was " + a.getBalance());
        }
    }
}
