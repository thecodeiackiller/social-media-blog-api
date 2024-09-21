package Service;

import Model.Account;

import java.util.ArrayList;
import java.sql.SQLException;
import java.util.*;
import DAO.AccountDAO;
import DAO.MessageDAO;

public class AccountService {
    public Account addNewUser(Account account)
    {
        // DAO method registerUser input here after conditionals
        // 3. The registration will be successful if and only if the username is not blank
        // 4. The registration will be successful if the password is at least 4 characters long
        // 5. The registration will be successful if the Account with that username does not already exist. 
        try
        {
            if((account.getUsername() != null) && (account.getPassword().length() > 3))
        {
            List<String> checkUserNameList = new ArrayList<String>();
            checkUserNameList = AccountDAO.getAllCurrentUsernames();
            
            for(String user : checkUserNameList)
            {
                // NOTE: In C#, we can use == to compare string, but in Java we need .equals as == compares the references, not the values in Java
                if (user.equals(account.getUsername())) {
                    System.out.println("This username already exists. Please pick another one");
                    
                }
                else
                {
                    return AccountDAO.registerUser(account);
                }
            }
        } 
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Account checkIfUserExists(String username)
    {
        // We know that this is going to take in a username string and that same string can be used to get the data from the db
        Account serviceAccountObj = new Account();
        serviceAccountObj = AccountDAO.verifyUser(username);
        if(serviceAccountObj == null) // We cannot use .equals method here because serviceAccountObj could potentially be null which would lead to a nullpointerexception
        {
            return null;
        }
        return serviceAccountObj; 
    }

    
}
