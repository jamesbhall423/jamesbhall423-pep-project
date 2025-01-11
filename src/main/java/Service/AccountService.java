package Service;

import java.util.logging.Logger;

import DAO.AccountDAO;
import Model.Account;
public class AccountService {
    private Logger logger = Logger.getGlobal();
    AccountDAO accountDAO = new AccountDAO();
    /*
     * Create or register a new account.
     * The registration will be successful if and only if the username is not blank,
     *  the password is at least 4 characters long,
     *  and an Account with that username does not already exist.
     *  @return the newly created account, or null if no account was created.
     */
    public Account createAccount(Account account) {
        logger.info("Creating account");
        String username = account.getUsername();
        if (username==null||username.equals("")||account.getPassword().length()<4) {
            logger.warning("Invalid account creation: username: + "+username+" password: "+account.getPassword());
            return null;
        } 
        // account table has unique constraint
        return accountDAO.createAccount(account);
    }
    /*
     *  @return the account mathing the username and password if it exists. Otherwise, return null
     */
    public Account loginToAccount(Account login) {
        logger.info("logging into account");
        Account account = accountDAO.fetchAccountByUsername(login.getUsername());
        if (account==null||!account.getPassword().equals(login.getPassword()))  {
            logger.info("Invalid login: account attempt: "+login+" matched account: "+account);
            return null;
        }
        else return account;
    }
}
