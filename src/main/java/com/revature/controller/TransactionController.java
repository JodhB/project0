package com.revature.controller;

import com.revature.model.Transaction;
import com.revature.service.TransactionService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

public class TransactionController implements Controller{

    private TransactionService transactionService;

    public TransactionController() {
        transactionService = new TransactionService();
    }

    private Handler getTransactionById = ctx -> {
        String id = ctx.pathParam("transaction_id");
        String clientId = ctx.pathParam("client_id");
        String accountId = ctx.pathParam("account_id");

        ctx.json(transactionService.getTransaction(id, clientId, accountId));
        ctx.status(200);
    };

    private Handler getAllTransactionsForAccount = ctx -> {
        String clientId = ctx.pathParam("client_id");
        String accountId = ctx.pathParam("account_id");
        String descriptionContains = ctx.queryParam("descriptionContains");

        if(descriptionContains == null)
            ctx.json(transactionService.getTransactions(clientId, accountId));
        else
            ctx.json(transactionService.getTransactions(clientId, accountId, descriptionContains));
        ctx.status(200);
    };

    private Handler addTransaction = ctx -> {
        String clientId = ctx.pathParam("client_id");
        String accountId = ctx.pathParam("account_id");
        Transaction transactionToAdd = ctx.bodyAsClass(Transaction.class);
        Transaction addedTransaction = transactionService.addTransaction(clientId, accountId, transactionToAdd);

        ctx.status(201);
        ctx.json(addedTransaction);
    };

    private Handler updateTransaction = ctx -> {
        String id = ctx.pathParam("transaction_id");
        String clientId = ctx.pathParam("client_id");
        String accountId = ctx.pathParam("account_id");
        Transaction transactionToUpdate = ctx.bodyAsClass(Transaction.class);
        transactionService.updateTransaction(id, clientId, accountId, transactionToUpdate);

        ctx.status(200);
        ctx.json("Update Successful");
    };

    private Handler deleteTransaction = ctx -> {
        String id = ctx.pathParam("transaction_id");
        String clientId = ctx.pathParam("client_id");
        String accountId = ctx.pathParam("account_id");
        transactionService.deleteTransaction(id, clientId, accountId);

        ctx.status(200);
        ctx.json("Delete Successful");
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/clients/{client_id}/accounts/{account_id}/transactions/{transaction_id}", getTransactionById);
        app.get("/clients/{client_id}/accounts/{account_id}/transactions", getAllTransactionsForAccount);
        app.post("/clients/{client_id}/accounts/{account_id}/transactions", addTransaction);
        app.put("/clients/{client_id}/accounts/{account_id}/transactions/{transaction_id}", updateTransaction);
        app.delete("/clients/{client_id}/accounts/{account_id}/transactions/{transaction_id}", deleteTransaction);
    }
}
