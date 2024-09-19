package DAO;


import Util.ConnectionUtil;
import kotlin.NotImplementedError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

public class MessageDAO {

    public static Message postMessage(Message message)
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
        // 8. If message creation not successful, response status should be 400, which is a client side error (controller logic)
    
        // NOTE: In order to actually follow through with this method, we have to make sure we pass all the conditionals in service layer

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

    public List<Message> getAllMessages()
    {   
        // Requirements
        // 1. A a user, should be able to submit a GET request on enpoint localhost:8080/messages.
        // 2. The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
        
        // Conditions
        // 3. It is expected for the list to simply be empty if there are no messages. 
        // 4. The response status should always be 200, which is the default.
        
        // NOTE: We used try catch here but try with resources should really be used to prevent resource leaks in prod env.
        List<Message> messageList = new ArrayList<>();
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select * from message";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        
        ResultSet rs = preparedStatement.executeQuery();
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        
        while(rs.next())
        {
            Message newMessage = new Message();
            newMessage.setMessage_id(rs.getInt("message_id"));
            newMessage.setPosted_by(rs.getInt("posted_by"));
            newMessage.setMessage_text(rs.getString("message_text"));
            newMessage.setTime_posted_epoch(rs.getInt("time_posted_epoch"));

            messageList.add(newMessage);
        }
        
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }  
        return messageList;         
    }

    public static Message getMessageById(int message_id)
    {
        // Requirements
        // 1. I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

        // Conditions
        // 2. The response body should contain a JSON representation of the message identified by the message_id. 
        // 3. It is expected for the response body to simply be empty if there is no such message.
        // 4. The response status should always be 200, which is the default. AKA No 404 Client Side Error will be thrown here

        // NOTE: Returning just nothing is safer when working with APIs and such so we don't run the risk of nullpointerrexception or something like that   
        // Ultimately, we're returning a message here, whether it's properties have values or it's empty
        Message message = new Message();
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select * from message where (message_id) = (?)";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        preparedStatement.setInt(1,message_id);
        
        ResultSet rs = preparedStatement.executeQuery();
        
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        if (rs.next()) {
             // Need to create an object like we did so that we can return (potentially) an object 
            message.setMessage_id(rs.getInt("message_id"));
            message.setMessage_text(rs.getString("message_text"));
            message.setPosted_by(rs.getInt("posted_by"));
            message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        
        return message == null ? new Message() : message;
    }

    public Message deleteMessageById(int message_id)
    {
        // Requirements
        // 1. As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

        // Conditions
        // 1. The dresponeletion of an existing message should remove an existing message from the database. 
        // 2. If the message existed, the response body should contain the now-deleted message. 
        // 3. The response status should be 200, which is the default.
        // 4. If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.       

        // NOTE: Important to note that delete doesn't return a resultset
        // NOTE: Instead of using executeQuery we will use executeUpdate

        Message message = new Message(); // Instantiating a new message object
        // First, we need to see if we can't retrive a message by message id. Hey!! We've already created that method
        message = getMessageById(message_id); // Regardless of what this returns (empty object or full object) we need to set it to a message
        if(message != null)
        {
            try
            {
                Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
                
                String sql = "delete from message where (message_id) = (?)";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql); 
                preparedStatement.setInt(1,message_id);
                
                preparedStatement.executeUpdate(sql);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            } 
        }
        return message;  
    }

    public static Message updateMessageById(Message message, int message_id)
    {
        // Requirements
        // 1. As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
        // 2. The request body should contain a new message_text values to replace the message identified by message_id. The request body can not be guaranteed to contain any other information.

        // 3. The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. (definitely going to need a service layer for this business logic)
        // 4. If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch)
        // 5. If the update is successful the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
        // 6. If the update of the message is not successful for any reason, the response status should be 400. (Client error)        

        // Ultimately, were going to use the message.getMessageText where message.getMessageId = some id
        // Instantiating a new message object
        // First, we need to see if we can't retrive a message by message id. Hey!! We've already created that method
            try
            {
                Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
                
                String sql = "update message" +
                "set message_text = (?) " +
                " where message_id = (?)";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql); 
                preparedStatement.setString(1,message.getMessage_text());
                preparedStatement.setInt(2,message_id);
                
                preparedStatement.executeUpdate(sql);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            } 
        return message;  
    }
    
    public List<String> getAllMessagesFromSingleUser(int account_id)
    {
        // 1. As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
        // 2. The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database.
        // 3. It is expected for the list to simply be empty if there are no messages. 
        // 4. The response status should always be 200, which is the default.
        List<String> messageListByUser = new ArrayList<>();
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select message_text from message where message_id = (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        
        ResultSet rs = preparedStatement.executeQuery();
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        
        while(rs.next())
        {
            messageListByUser.add(rs.getString("message_text"));
        }
        
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }  
        return messageListByUser;  
    }

    public static boolean checkIfMessageIdExists(int message_id)
    {
        // We can use this method in our MessageService layer
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select message_id from message";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        
        
        ResultSet rs = preparedStatement.executeQuery();
        
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        if (rs.next()) {
             // Need to create an object like we did so that we can return (potentially) an object 
            if(rs.getInt("message_id") == message_id)
            {
                return true;
            }
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        
        return false;
    }

}
