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
        if(message.message_text.length() > 255)
        {
            throw new IllegalArgumentException();
        }
        if(message.message_text == "")
        {
            throw new IllegalArgumentException();    
        }
        
            int verifiedUser = message.getPosted_by();
            if(AccountDAO.verifyUserById(verifiedUser) != null) 
            //We currently have verifiedUser as type int. We can't really get the string username without the account table. 
            // But then again, I'm  thinking this might be a time for a join.
            // But then again, I'm thinking we can just create a new method that returns a message based on the id so why create a join 
            {
                return MessageDAO.postMessage(message);
            }
            else
            {
                throw new IllegalArgumentException();
            }
        
    }

    public List<Message> returnAllMessages()
    {
       return MessageDAO.getAllMessages();
        
    }

    public Message getNewMessageById(int message_id)
    {
        return MessageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id)
    {
        return MessageDAO.deleteMessageById(message_id);
    }

    public Message updateAMessageById(Message message, int message_id)
    {
       
        // This service supports the update updateMessageById() method in MessageDAO.java
        // Requirements: the update of a message should be successful if and only if:
        // 1. the message id already exists - feel like we need another method to do this (I finally understand the need to for loops and foreach loops. For just checking if something is there so we can do something else.)
        // 2  new message_text is not blank 
        // 3. Message text not over 255 characters. (definitely going to need a service layer for this business logic)

        if(MessageDAO.checkIfMessageIdExists(message_id) == false)
        {
            throw new IllegalArgumentException(); 
        } 
        else if(message.getMessage_text() == "")
        {
            throw new IllegalArgumentException();
        }
        else if (message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException();
        }
        else {
            return MessageDAO.updateMessageById(message,message_id);
        }

    }

    public void getAllMessagesFromSingularUser(int account_id)
    {
        MessageDAO messageDAO = new MessageDAO();
        messageDAO.getAllMessagesFromSingleUser(account_id);
        // This service method would be for the getAllMessagesFromSingleUser in the MessageDAO.
        // Might not need any business logic here in the service layer, but we can verify later
        // Also a big note
    }







    
}
