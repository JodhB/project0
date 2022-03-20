package com.revature.controller;

import com.revature.model.Account;
import com.revature.service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController implements Controller{

    private AccountService accountService;

    public AccountController() {
        accountService = new AccountService();
    }

    private Handler getAccountById = ctx -> {
        String id = ctx.pathParam("account_id");
        String clientId = ctx.pathParam("client_id");

        ctx.json(accountService.getAccount(id, clientId));
        ctx.status(200);
    };

    private Handler getAllAccountsForClient = ctx -> {
        String clientId = ctx.pathParam("client_id");
        String balanceUpperBound = ctx.queryParam("amountLessThan");
        String balanceLowerBound = ctx.queryParam("amountGreaterThan");

        if(balanceLowerBound != null && balanceUpperBound != null)
            ctx.json(accountService.getAccounts(clientId, balanceLowerBound, balanceUpperBound));
        else if(balanceLowerBound != null)
            ctx.json(accountService.getAccounts(clientId, balanceLowerBound, false));
        else if(balanceUpperBound != null)
            ctx.json(accountService.getAccounts(clientId, balanceUpperBound, true));
        else
            ctx.json(accountService.getAccounts(clientId));
        ctx.status(200);
    };

    private Handler addAccount = ctx -> {
        String clientId = ctx.pathParam("client_id");
        Account accountToAdd = ctx.bodyAsClass(Account.class);
        Account addedAccount = accountService.addAccount(clientId, accountToAdd);

        ctx.status(201);
        ctx.json(addedAccount);
    };

    private Handler updateAccount = ctx -> {
        String id = ctx.pathParam("account_id");
        String clientId = ctx.pathParam("client_id");
        Account accountToUpdate = ctx.bodyAsClass(Account.class);
        accountService.updateAccount(id, clientId, accountToUpdate);

        ctx.status(200);
        ctx.json("Update Successful");
    };

    private Handler deleteAccount = ctx -> {
        String id = ctx.pathParam("account_id");
        String clientId = ctx.pathParam("client_id");
        accountService.deleteAccount(id, clientId);

        ctx.status(200);
        ctx.json("Delete Successful");
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/clients/{client_id}/accounts/{account_id}", getAccountById);
        app.get("/clients/{client_id}/accounts", getAllAccountsForClient);
        app.post("/clients/{client_id}/accounts", addAccount);
        app.put("/clients/{client_id}/accounts/{account_id}", updateAccount);
        app.delete("/clients/{client_id}/accounts/{account_id}", deleteAccount);
    }
}
