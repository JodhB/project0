package com.revature.main;

import com.revature.controller.*;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project0 {

    public static Logger logger = LoggerFactory.getLogger(Project0.class);

    public static void main(String[] args) {
        Javalin app = Javalin.create();

        app.before(ctx -> {
            logger.info(ctx.method() + " request received for " + ctx.path());
        });

        mapControllers(app, new ClientController(), new AccountController(), new TransactionController(), new ExceptionController());

        app.start();
    }

    public static void mapControllers(Javalin app, Controller... controllers) {
        for(Controller c: controllers) {
            c.mapEndpoints(app);
        }
    }

}
