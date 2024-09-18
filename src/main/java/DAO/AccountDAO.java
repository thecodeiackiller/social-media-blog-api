package DAO;

import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;

public class AccountDAO {

    public static Account registerUser(Account account) throws SQLException
    {
       
        // 1. As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
        // 2. The body will contain a representation of a JSON Account, but will not contain an account_id.
        // 3. The registration will be successful if and only if the username is not blank
        // 4. The registration will be successful if the password is at least 4 characters long
        // 5. The registration will be successful if the Account with that username does not already exist. 
        // 6. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
        // 7. If the registration is not successful, the response status should be 400. (Client error)
        try{
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
            String sql = "insert into account (username,password) values (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); 
            preparedStatement.setString(1, account.getUsername()); //
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate(); // QUESTION: Why is this executeUpdate rather than executeQuery
            // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while(rs.next())
            {
                int generated_account = (int) rs.getLong(1);
                return new Account(generated_account, account.getUsername(), account.getPassword());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Account verifyUser(String checkedUsername)
    {
        // Basically, we want to see the the Account we return is null or if there's actual data.
        
        // 1. As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
        // 2. The request body will contain a JSON representation of an Account, not containing an account_id. 
        // 3. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.

        // 4. The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
        // 5. If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
        // 6. If the login is not successful, the response status should be 401. (Unauthorized)
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select * from account where (username) = (?)";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        preparedStatement.setString(1,checkedUsername);
        
        ResultSet rs = preparedStatement.executeQuery();
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        if (rs.next()) {
            Account account = new Account(); // Need to create an object like we did so that we can return (potentially) an object 
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
            account.setAccount_id(rs.getInt("account_id"));

            return account;
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
             return null;
    }

    public static List<String> getAllCurrentUsernames()
    {
        // NOTE: We used try catch here but try with resources should really be used to prevent resource leaks in prod env.
        List<String> usernameList = new ArrayList<String>();
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select username from account";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        
        ResultSet rs = preparedStatement.executeQuery();
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        
        while(rs.next())
        {
            String username = rs.getString("username");
            usernameList.add(username);
        }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }                
        return usernameList;

    }

    public static Account verifyUserById(int userId)
    {
        // Basically, we want to see the the Account we return is null or if there's actual data.
        // We want to return account and then get the username from the Account object that is returned
        // If there is Account is null then we know our conditional will fail in MessageService.java
        
        try
        {
            Connection connection = ConnectionUtil.getConnection(); // Looks like we also have to import our java.util packages (java.sql.*)
            
        String sql = "select * from account where (account_id) = (?)";
        
        PreparedStatement preparedStatement = connection.prepareStatement(sql); 
        preparedStatement.setInt(1,userId);
        
        ResultSet rs = preparedStatement.executeQuery();
        // Need a ResultSet here as we are returning in the body of the JSON an Account along with its associated id
        if (rs.next()) {
            Account account = new Account(); // Need to create an object like we did so that we can return (potentially) an object 
            account.setUsername(rs.getString("username"));
            account.setPassword(rs.getString("password"));
            account.setAccount_id(rs.getInt("account_id"));

            return account;
        }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        return null;
    }

}



