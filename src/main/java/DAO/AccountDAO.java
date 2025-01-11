package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;
import java.util.logging.Logger;

public class AccountDAO {
    
    private Logger logger = Logger.getGlobal();
    private Connection sqlConnection = ConnectionUtil.getConnection();
    /*
     * Create a new account.
     * The input provides the username and password variables,
     *  while the account_id variable is generated automatically.
     * @return a copy of the created account, or null in case of an SQL error.
     */
    public Account createAccount(Account account) {
        try {
            PreparedStatement insertStatement = sqlConnection.prepareStatement("INSERT INTO account (username, password) VALUES (?, ?);");
            insertStatement.setString(1,account.getUsername());
            insertStatement.setString(2,account.getPassword());
            insertStatement.execute();
            // Username has unique constaint: this will return our created account
            return fetchAccountByUsername(account.getUsername());
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            return null;
        }
    }
    /*
     * @return the account with the specified username, or null if no such account exists.
     */
    public Account fetchAccountByUsername(String username) {
        try {
            PreparedStatement fetchStatement = sqlConnection.prepareStatement("SELECT * FROM account WHERE username = ?;");
            fetchStatement.setString(1,username);
            ResultSet results = fetchStatement.executeQuery();
            return accountResult(results);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }
    private Account accountResult(ResultSet results) throws SQLException {
        if (!results.first()) return null;
        Account out = new Account();
        out.setAccount_id(results.getInt("account_id"));
        out.setUsername(results.getString("username"));
        out.setPassword(results.getString("password"));
        return out;
    }
    /*
     * @return the account with the specified id, or null if no such account exists.
     */
    public Account fetchAccountByID(int user_id) {
        try {
            PreparedStatement fetchStatement = sqlConnection.prepareStatement("SELECT * FROM account WHERE account_id = ?;");
            fetchStatement.setInt(1,user_id);
            ResultSet results = fetchStatement.executeQuery();
            return accountResult(results);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }
}
