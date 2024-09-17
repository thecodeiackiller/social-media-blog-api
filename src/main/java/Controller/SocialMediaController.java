package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
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
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this:: handlerThatPostsAndReturnsUser)


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void handlerThatPostsAndReturnsUser(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(),Account.class);


        

        ctx.json(;)
    }


}