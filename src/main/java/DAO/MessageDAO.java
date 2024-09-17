package DAO;


import Util.ConnectionUtil;
import java.sql.*;

import Model.Message;

public class MessageDAO {

    public Message postMessage(Message message)
    {
        
        
        // Requirements
        // 1. User should be able to post new message through endpoint http://localhost:8080/messages 
        // 2. Reponse body will contain JSON representaion of a message. Persist it to DB. JSON payload won't have message_id // NOTE: this is because its autoincremented

        // Conditions
        // 3. Message posting will be successful if and only if the message_text is not blank (service layer)
        // 4. Message is not over 255 characters (this logic will happen in the service layer)
        // 5. Message has to be posted by a real user (check for this in service layer (message_id is foreign key to user_id)) // NOTE: We can probably just use the method we created in the AccountService class for this
        // 6. Reponse body should contain of JSON of the message including message_id. Reponse status should be 200
        // 7. New message should be persisted to the db
        // 8. If message creation not successful, response status should be 400, which is a client side error
    

        try{
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
            String sql = "insert into message (posted_by,message_text,time_posted_epoch) values (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
            preparedStatement.setInt(1, message.getPosted_by()); //
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            
            
            preparedStatement.executeUpdate(); // QUESTION: Why is this executeUpdate rather than executeQuery
            // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while(rs.next())
            {
                int generated_message_id = (int) rs.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public void getAllMessages()
    {
        throw new UnsupportedOperationException("Not yet implemented");
        
        // Requirements
        // 1. A a user, should be able to submit a GET request on enpoint localhost:8080/messages.
        // 2. The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
        
        // Conditions
        // 3. It is expected for the list to simply be empty if there are no messages. 
        // 4. The response status should always be 200, which is the default.
        
    }

    public void getMessageById()
    {
        throw new UnsupportedOperationException("Not yet implemented");
        
        // Requirements
        // 1. I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

        // Conditions
        // 2. The response body should contain a JSON representation of the message identified by the message_id. 
        // 3. It is expected for the response body to simply be empty if there is no such message.
        // 4. The response status should always be 200, which is the default.

    }

    public void deleteMessageById()
    {
        throw new UnsupportedOperationException("Not yet implemented");
        
        // Requirements
        // 1. As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

        // Conditions
        // 1. The deletion of an existing message should remove an existing message from the database. 
        // 2. If the message existed, the response body should contain the now-deleted message. 
        // 3. The response status should be 200, which is the default.
        // 4. If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.       
    }

    public void updateMessageById()
    {
        throw new UnsupportedOperationException("Not yet implemented");
        
        // Requirements
        // 1. As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
        // 2. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

        // 3. The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
        // 4. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch)
        // 5. If the update is successful the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
        // 6. If the update of the message is not successful for any reason, the response status should be 400. (Client error)        
    }
    
    public void getAllMessagesFromSingleUser()
    {
        throw new UnsupportedOperationException("Not yet implemented");

        // 1. As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
        // 2. The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database.
        // 3. It is expected for the list to simply be empty if there are no messages. 
        // 4. The response status should always be 200, which is the default.
    }

}
