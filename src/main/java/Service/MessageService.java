package Service;

import Model.Message;

import java.util.List;
import java.util.logging.Logger;

import DAO.MessageDAO;

public class MessageService {
    
    private Logger logger = Logger.getGlobal();
    private MessageDAO messageDAO = new MessageDAO();
    /*
     * The creation of the message will be successful if and only if the message_text is not blank,
     *  is under 255 characters,
     *  and posted_by refers to a real, existing user.
     *  @return the created message, or null if no message created
     */
    public Message createMessage(Message to_create) {
        logger.info("Creating message "+to_create);
        String message_text = to_create.getMessage_text();
        if (message_text==null||message_text.equals("")||message_text.length()>=255) {
            logger.warning("invalid message: cannot be created");
            return null;
        } 
        // foreign key (posted_by) automatically checked by contraint
        Message message = messageDAO.createMessage(to_create);
        if (message==null) logger.warning("message has not been created");
        else logger.info("message created");
        return message;
    }
    /*
     * @return a list of all nessages in the database
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    /*
     * @return the message with the given id, or null if no such message exists.
     */
    public Message getMessage(int message_id) {
        return messageDAO.getMessage(message_id);
    }
    /*
     * Removes the message with the given id from the database.
     * @return the message with the given id, or null if no message existed.
     */
    public Message deleteMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }
    /*
     * Sets the text of the message given by message_id to the new_text
     * @return the message with the given id, or null if no message existed.
     */
    public Message updateMessage(int message_id, String new_text) {
        logger.info("updating message for "+message_id +" to "+new_text);
        if (new_text==null||new_text.equals("")||new_text.length()>=255) return null;
        return messageDAO.updateMessage(message_id,new_text);
    }
    /*
     * @return all the messages with the given user_id
     */
    public List<Message> getAllMessagesFromUser(int user_id) {
        return messageDAO.getAllMessagesFromUser(user_id);
    }
}
