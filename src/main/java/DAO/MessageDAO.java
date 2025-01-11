package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    private Logger logger = Logger.getGlobal();
    private Connection sqlConnection = ConnectionUtil.getConnection();
    /*
     * Create a new message with values (except message_id) given by to_create
     *  @return the created message, or null if no message created
     */
    public Message createMessage(Message to_create) {
        try {
            String[] generatedColumns = new String[]{"message_id"};
            PreparedStatement insertStatement = sqlConnection.prepareStatement("INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);",generatedColumns);
            insertStatement.setInt(1,to_create.getPosted_by());
            insertStatement.setString(2,to_create.getMessage_text());
            insertStatement.setLong(3,to_create.getTime_posted_epoch());

            int rows = insertStatement.executeUpdate();
            if (rows==0) {
                logger.warning("DAO: message not inserted");
                return null;
            } 
            logger.info("message inserted");
            ResultSet generatedKeys = insertStatement.getGeneratedKeys();
            generatedKeys.next();
            return getMessage(generatedKeys.getInt("message_id"));
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }
    /*
     * @return a list of all nessages in the database
     */
    public List<Message> getAllMessages() {
        logger.info("Retreving all messages");
        List<Message> out = new ArrayList<Message>();
        try {
            Statement queryStatement = sqlConnection.createStatement();
            ResultSet results = queryStatement.executeQuery("SELECT * FROM message;");
            multiMessageResult(results, out);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return out;
    }
    private void multiMessageResult(ResultSet results, List<Message> out) throws SQLException {
        results.beforeFirst();
        while (results.next()) {
            out.add(gatherMessage(results));
            logger.info("message added to list");
        }
    }
    /*
     * @return the message with the given id, or null if no such message exists.
     */
    public Message getMessage(int message_id) {
        logger.info("Getting message with id "+message_id);
        try {
            PreparedStatement fetchStatement = sqlConnection.prepareStatement("SELECT * FROM message WHERE message_id = ?;");
            fetchStatement.setInt(1,message_id);
            ResultSet results = fetchStatement.executeQuery();
            return singleMessageResult(results);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }
    private Message singleMessageResult(ResultSet results) throws SQLException {
        if (!results.first()) return null;
        return gatherMessage(results);
    }
    private Message gatherMessage(ResultSet resultSet) throws SQLException {
        Message out = new Message();
        out.setMessage_id(resultSet.getInt("message_id"));
        out.setPosted_by(resultSet.getInt("posted_by"));
        out.setMessage_text(resultSet.getString("message_text"));
        out.setTime_posted_epoch(resultSet.getLong("time_posted_epoch"));
        return out;
    }
    /*
     * Removes the message with the given id from the database.
     * @return the message with the given id, or null if no message existed.
     */
    public Message deleteMessage(int message_id) {
        Message toReturn = getMessage(message_id);
        try {
            PreparedStatement updateStatement = sqlConnection.prepareStatement("DELETE FROM message WHERE message_id = ?;");
            updateStatement.setInt(1,message_id);
            updateStatement.execute();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return toReturn;
    }
    /*
     * Sets the text of the message given by message_id to the new_text
     * @return the message with the given id, or null if no message existed.
     */
    public Message updateMessage(int message_id, String new_text) {
        Message message = getMessage(message_id);
        if (message==null) return null;
        message.setMessage_text(new_text);
        try {
            PreparedStatement updateStatement = sqlConnection.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            updateStatement.setString(1,new_text);
            updateStatement.setInt(2,message_id);
            int rows = updateStatement.executeUpdate();
            logger.info(rows+" rows updated");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
        return message;
    }
    /*
     * @return all the messages with the given user_id
     */
    public List<Message> getAllMessagesFromUser(int user_id) {
        List<Message> out = new ArrayList<Message>();
        try {
            PreparedStatement queryStatement = sqlConnection.prepareStatement("SELECT * FROM message WHERE posted_by = ?;");
            queryStatement.setInt(1,user_id);
            ResultSet results = queryStatement.executeQuery();
            multiMessageResult(results, out);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return out;
    }
}
