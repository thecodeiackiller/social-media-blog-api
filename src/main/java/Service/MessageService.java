package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public Message postNewMessage(Message message)
    {
        // In our MessageDAO, we are returning Author just like in this method
        // Note: Once an author is returned from this method, we will then serialize the properties on that object back to JSON (which is what we want if post is successful)

        // Now let's implement our conditionals:
        // 3. Message posting will be successful if and only if the message_text is not blank (service layer)
        // 4. Message is not over 255 characters (this logic will happen in the service layer)
        // 5. Message has to be posted by a real user (check for this in service layer (message_id is foreign key to user_id)) 
        // NOTE: We can probably just use the method we created in the AccountService class for this (number 5)
        if((message.message_text.length() > 255) && (message.message_text != null))
        {
            int verifiedUser = message.getMessage_id();
            if(AccountDAO.verifyUserById(verifiedUser) != null) 
            //We currently have verifiedUser as type int. We can't really get the string username without the account table. 
            // But then again, I'm  thinking this might be a time for a join.
            // But then again, I'm thinking we can just create a new method that returns a message based on the id so why create a join 
            {
                MessageDAO.postMessage(message);
            }
        }
        return null;
    }

    public List<Message> returnAllMessages()
    {
        throw new UnsupportedOperationException();
        // This is for the getAllMessages() method for the MessgaesDAO
        // Not sure if we actually need a service method for this method, but need to keep eyes peeled.
    }

    public void getNewMessageById()
    {
        throw new UnsupportedOperationException("Not yet implemented");
        // NOTE: This is for the getMessageById() method in the MessageDAO.java class
        // NOTE: It will ultimately be this method that is called in the controller which handles the web requests
        // NOTE: Judging by the look of it, It doesn't look like there needs to be any business logic here, but keep eyes peeled
        
        // Requirements
        // 1. I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

        // Conditions
        // 2. The response body should contain a JSON representation of the message identified by the message_id. 
        // 3. It is expected for the response body to simply be empty if there is no such message.
        // 4. The response status should always be 200, which is the default.

    }







    
}
