package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 // NOTE: We define our enpoints in this controller class Context class. We'll use the status method in Context class to return status codes
 // NOTE: To do this, we can also throw a possible JsonProcessingException if things go wrong, or just handle the request status through an if else conditional statement
public class SocialMediaController {
    // NOTE: Before anything, we likely need to instantiate the service classes
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        // Now we can use the services in these services. Also remember that DI created and uses services. 
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerNewUserHandler);
        app.start(8080);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerNewUserHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        // We know were going to need to call the underlying method at some point
        Account account = mapper.readValue(ctx.body(), Account.class);
        account = accountService.addNewUser(account);
        // Need to return a body of the account in json format using the writetooptions method or whatever
        
        if(account != null)
        {
            ctx.json((account)).status(201);
        }
        ctx.status(400).result("Something went wrong the client is to blame, baby.");
        // Also if registration not successful, response status should be 400
    }

    


}