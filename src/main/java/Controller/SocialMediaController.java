package Controller;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
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
        app.get("/", this::defaultRootHandler);
       
        app.post("/register", this::registerNewUserHandler);
        // app.start(8080); Commenting this line out as Error was thrown that there was an IllegalStateException - Server already started 
        app.post("/login", this::verifyNewUserHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::retriveAllMessagesHandler);
        app.get("messages/{message_id}", this::retriveMessageById);
        app.delete("messages/{message_id}", this::deleteMessageById);
        app.patch("messages/{message_id}", this::updateMessageById);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void defaultRootHandler(Context ctx) throws JsonProcessingException
    {
        ctx.result("Welcome to the API");
    }

    private void registerNewUserHandler(Context ctx) throws JsonProcessingException
    {
        try{
            ObjectMapper mapper = new ObjectMapper();
        // We know were going to need to call the underlying method at some point
        Account account = mapper.readValue(ctx.body(), Account.class);
        account = accountService.addNewUser(account);
        // Need to return a body of the account in json format using the writetooptions method or whatever
        
        if(account != null)
        {
            ctx.json((account)).status(200);
        }
    }
        catch (IllegalArgumentException e)
        {
            ctx.status(400).result("");
        }
        catch (JsonProcessingException e)
        {
            ctx.status(400).result("Please format your JSON correctly");
        }
        catch (Exception e)
        {
            ctx.status(500).result("Ergg, somethings up with the server:" + e.getMessage());
        }
    }

    private void verifyNewUserHandler(Context ctx) throws JsonProcessingException
    {
        System.out.println("verify handler has been hit");
        // First, need to wrap this in a try catch to catch possible exception
        try{
            // The Context class here comes from the Javalin API
        // Object mapper will come from Jackson dependency
        ObjectMapper mapper = new ObjectMapper();
        // Need to take in the json which included a user name or password and convert that to an Account object
        Account verifiableAccount = mapper.readValue(ctx.body(),Account.class);
        // Need to pass the verifiableAccount into our Account service method. The method returns a boolean, but should It return a Account object?
        Account potentiallyVerifiedAccount = accountService.checkIfUserExists(verifiableAccount.getUsername(), verifiableAccount.getPassword()); // This returns boolean, maybe needs to return Account.
        // Need to return back some json
        if(potentiallyVerifiedAccount != null)
        {
            ctx.json(potentiallyVerifiedAccount).status(200);
        }
        else
        {
            ctx.status(401).result("");
        }
        }
        catch(JsonProcessingException e)
        {
            ctx.status(401);
        }
        catch(Exception e)
        {
            ctx.result(e.getMessage()).status(500);
        }
    
    }

    public void postMessageHandler(Context ctx) throws JsonProcessingException
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message verifiedMessage = messageService.postNewMessage(message);

            if(verifiedMessage != null)
            {
                ctx.json(verifiedMessage).status(200);
            }
        }
        catch (IllegalArgumentException e)
        {
            ctx.status(400);
        }
    }

    public void retriveAllMessagesHandler(Context ctx) throws JsonProcessingException
    {
            List<Message> messages = new ArrayList<Message>();
            messages = messageService.returnAllMessages();
            ctx.json(messages).status(200);
        
    }

    public void retriveMessageById(Context ctx) throws JsonProcessingException
    {
        // In order to do this, we need to extract the path parameter in the request similar to what we did with Blazor this morning
        // And like we did in blazor with PlanId:int, we have to do this in java with Integer.parseInt(ctx.pathParam("message_id"))
        // We literally aren't mapping anything here. We're getting the integer value from the endpoint request
        // Extract the message_id from the path parameter
    Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
    Message message = messageService.getNewMessageById(message_id);
    if (message.getMessage_text() != null) {   
        ctx.json(message).status(200);
    } else {
        
        ctx.result("").status(200);
    }  
    }

    public void deleteMessageById(Context ctx) throws JsonProcessingException
    {
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);
        if (message.getMessage_text() != null) {   
            ctx.json(message).status(200);
        } else {
            
            ctx.result("").status(200);
        }  
    }

    public void updateMessageById(Context ctx) throws JsonProcessingException
    {
        Integer message_id = Integer.parseInt(ctx.pathParam("message_id"));
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            // We can now use JsonNode class to be able to read values from the Json. This is like JsonNode or JsonDocument in C# .NET
            JsonNode body = mapper.readTree(ctx.body());

            String message = body.get("message_text").asText();
            Message returnMessage = messageService.updateAMessageById(message,message_id);

            if(returnMessage != null)
            {
                ctx.json(returnMessage).status(200);
            }
        }
        catch (IllegalArgumentException e)
        {
            ctx.status(400);
        }
    }
}