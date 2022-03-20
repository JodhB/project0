package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private static Logger logger = LoggerFactory.getLogger(ClientService.class);

    private ClientDao clientDao;

    public ClientService() {
        clientDao = new ClientDao();
    }

    public ClientService(ClientDao mockDao) {
        clientDao = mockDao;
    }

    public List<Client> getClients() throws SQLException {
        logger.info("getClients method called");
        return clientDao.getClients();
    }

    public Client getClient(String idString) throws SQLException, ClientNotFoundException {
        logger.info("getClient method called");
        try {
            int id = Integer.parseInt(idString);

            Client c = clientDao.getClient(id);
            if(c == null) {
                throw new ClientNotFoundException("Client with id " + id + " was not found");
            }

            return c;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }

    public Client addClient(Client c) throws SQLException {
        logger.info("addClient method called");
        validateClientInformation(c);
        return clientDao.addClient(c);
    }

    public void updateClient(String idString, Client c) throws SQLException, ClientNotFoundException {
        logger.info("updateClient method called");
        try {
            int id = Integer.parseInt(idString);

            validateClientInformation(c);

            c.setId(id);
            if(!clientDao.updateClient(c)) {
                throw new ClientNotFoundException("Client with id " + id + " was not found");
            }
        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for student must be a valid int");
        }
    }

    public void deleteClient(String idString) throws SQLException, ClientNotFoundException {
        logger.info("deleteClient method called");
        try {
            int id = Integer.parseInt(idString);

            if(!clientDao.deleteClient(id)) {
                throw new ClientNotFoundException("Client with id " + id + " was not found");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }

    private void validateClientInformation(Client c) {
        c.setFirstName(c.getFirstName().trim());
        c.setLastName(c.getLastName().trim());
        c.setAddress(c.getAddress().trim());

        if (!c.getFirstName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("First name must only have alphabetical characters. First name input was " + c.getFirstName());
        }

        if (!c.getLastName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Last name must only have alphabetical characters. Last name input was " + c.getLastName());
        }

        if(!c.getAddress().matches("\\d+\\s([a-zA-Z0-9]+(\\s?))+")) {
            throw new IllegalArgumentException("Invalid address. Input was " + c.getAddress());
        }
    }
}
