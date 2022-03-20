package com.revature.controller;

import com.revature.model.Client;
import com.revature.service.ClientService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class ClientController implements Controller{

    private ClientService clientService;

    public ClientController() {
        clientService = new ClientService();
    }

    private Handler getAllClients = ctx -> {
        ctx.json(clientService.getClients());
        ctx.status(200);
    };

    private Handler getClientById = ctx -> {
        String id = ctx.pathParam("client_id");
        ctx.json(clientService.getClient(id));
        ctx.status(200);
    };

    private Handler addClient = ctx -> {
        Client clientToAdd = ctx.bodyAsClass(Client.class);
        Client addedClient = clientService.addClient(clientToAdd);

        ctx.status(201);
        ctx.json(addedClient);
    };

    private Handler updateClient = ctx -> {
        Client clientToUpdate = ctx.bodyAsClass(Client.class);
        clientService.updateClient(ctx.pathParam("client_id"), clientToUpdate);

        ctx.status(200);
        ctx.json("Update successful");
    };

    private Handler deleteClient = ctx -> {
        clientService.deleteClient(ctx.pathParam("client_id"));

        ctx.status(200);
        ctx.json("Delete successful");
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/clients", getAllClients);
        app.get("/clients/{client_id}", getClientById);
        app.post("/clients", addClient);
        app.put("/clients/{client_id}", updateClient);
        app.delete("/clients/{client_id}", deleteClient);
    }
}
