package com.revature.controller;

import com.revature.exception.ClientNotFoundException;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.TransactionNotFoundException;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionController implements Controller{

    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private ExceptionHandler illegalArgument = (exception, ctx) -> {
        logger.warn("User input was an illegal argument. Exception message: " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    };

    private ExceptionHandler resourceNotFound = (exception, ctx) -> {
        logger.warn("User attempted to retrieve a resource that was not found. Exception message: " + exception.getMessage());
        ctx.status(404);
        ctx.json(exception.getMessage());
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(IllegalArgumentException.class, illegalArgument);
        app.exception(ClientNotFoundException.class, resourceNotFound);
        app.exception(AccountNotFoundException.class, resourceNotFound);
        app.exception(TransactionNotFoundException.class, resourceNotFound);
    }
}
