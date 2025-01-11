package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.*;

import java.util.List;

import Model.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an account_id.
        app.post("register",(ctx)-> {
            Account output = accountService.createAccount(ctx.bodyAsClass(Account.class));
            if (output!=null) {
                //The response status should be 200 OK, which is the default.
                ctx.json(output).status(200);
            } else {
                //If the registration is not successful, the response status should be 400. (Client error)
                ctx.status(400);
            }
        });


        //POST localhost:8080/login. The request body will contain a JSON representation of an Account, not containing an account_id.
        app.post("login",(ctx)-> {
            Account output = accountService.loginToAccount(ctx.bodyAsClass(Account.class));
            if (output!=null) {
                //If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK
                ctx.json(output).status(200);
            } else {
                //If the login is not successful, the response status should be 401. (Unauthorized)
                ctx.status(401);
            }
        });

        //POST localhost:8080/messages The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.
        app.post("messages",(ctx)-> {
            Message output = messageService.createMessage(ctx.bodyAsClass(Message.class));
            if (output!=null) {
                //If successful, the response status should be 200
                ctx.json(output).status(200);
            } else {
                //If the creation of the message is not successful, the response status should be 400. (Client error)
                ctx.status(400);
            }
        });

        //GET localhost:8080/messages
        app.get("messages",(ctx)-> {
            List<Message> output = messageService.getAllMessages();
            //The response status should always be 200
            ctx.json(output).status(200);
        });

        //GET localhost:8080/messages/{message_id}
        app.get("messages/{message_id}", (ctx)-> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message message = messageService.getMessage(message_id);
                if (message!=null) ctx.json(message);
                //The response status should always be 200
                ctx.status(200);
            } catch (NumberFormatException e) {
                // Not specified by instructions, but this is a bad request, not simply a nonexistent message.
                ctx.status(400);
            }
        });

        //DELETE localhost:8080/messages/{message_id}
        app.delete("messages/{message_id}",(ctx)-> {
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                Message message = messageService.deleteMessage(message_id);
                if (message!=null) ctx.json(message);
                //The response status should be 200 regardless if message exists
                ctx.status(200);
            } catch (NumberFormatException e) {
                // Not specified by instructions, but this is a bad request, not simply a nonexistent message.
                // This preserves idempotentance
                ctx.status(400);
            }
        });

        //PATCH localhost:8080/messages/{message_id}
        app.patch("messages/{message_id}",(ctx)-> {
            Message message = null;
            try {
                int message_id = Integer.parseInt(ctx.pathParam("message_id"));
                message = messageService.updateMessage(message_id,ctx.bodyAsClass(Message.class).getMessage_text());
            } catch (NumberFormatException e) {
            }
            //If successful, the response status should be 200
            if (message!=null) ctx.json(message).status(200);
            //If the update of the message is not successful for any reason, the response status should be 400. (Client error)
            else ctx.status(400);
        });

        //GET localhost:8080/accounts/{account_id}/messages
        app.get("accounts/{account_id}/messages", (ctx)-> {
            try {
                int account_id = Integer.parseInt(ctx.pathParam("account_id"));
                List<Message> messages = messageService.getAllMessagesFromUser(account_id);
                ctx.json(messages);
                //The response status should always be 200
                ctx.status(200);
            } catch (NumberFormatException e) {
                // Not specified by instructions, but this is a bad request, not simply a nonexistent message.
                ctx.status(400);
            }
        });
        return app;
    }



}