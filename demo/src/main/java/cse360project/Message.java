package cse360project;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.MessageType;
import cse360project.utils.Role;

/**
 * Represents a message in the application
 */
public class Message implements Serializable {
	private int id;
	private MessageType messageType;
    private String messageContent;
    private User sender;
    private Role senderRole;
    private User thread;
    private Timestamp messageTimestamp;

    /**
     * Constructor for Message
     * @param id the unique identifier for the message
     * @param messageType the type of message
     * @param messageContent the content of the message
     * @param sender the user who sent the message
     * @param senderRole the role of the sender
     * @param thread the user or thread this message is part of
     * @param messageTimestamp the timestamp when the message was sent
     */
    public Message(int id,MessageType messageType, String messageContent, User sender, Role senderRole, User thread, Timestamp messageTimestamp) {
    	this.id = id;
    	this.messageType = messageType;
        this.messageContent = messageContent;
        this.sender = sender;
        this.senderRole = senderRole;
        this.thread = thread;
        this.messageTimestamp = messageTimestamp;
    }
    
    // Overloaded constructor without ID, for cases where ID is not initially needed
    public Message(MessageType messageType, String messageContent, User sender, Role senderRole, User thread, Timestamp messageTimestamp) {
        this(-1, messageType, messageContent, sender, senderRole, thread, messageTimestamp);
    }

    // Getters and Setters
    
    /**
     * Get the ID of the message
     * @return the ID of the message
     */
    public int getId() {
        return id;
    }

    /**
     * Set the ID of the message
     * @param id the ID of the message
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Get the type of message
     * @return the type of message
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Set the type of message
     * @param messageType the type of message
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Get the content of the message
     * @return the content of the message
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * Set the content of the message
     * @param messageContent the content of the message
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * Get the sender of the message
     * @return the sender of the message
     */
    public User getSender() {
        return sender;
    }

    /**
     * Set the sender of the message
     * @param sender the sender of the message
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Get the role of the sender
     * @return the role of the sender
     */
    public Role getSenderRole() {
        return senderRole;
    }

    /**
     * Set the role of the sender
     * @param senderRole the role of the sender
     */
    public void setSenderRole(Role senderRole) {
        this.senderRole = senderRole;
    }

    /**
     * Get the thread this message is part of
     * @return the user whos thread this message is part of
     */
    public User getThread() {
        return thread;
    }

    /**
     * Set the thread this message is part of
     * @param thread the user whos thread this message is part of
     */
    public void setThread(User thread) {
        this.thread = thread;
    }

    /**
     * Get the timestamp of the message
     * @return the timestamp of the message
     */
    public Timestamp getMessageTimestamp() {
        return messageTimestamp;
    }

    /**
     * Set the timestamp of the message
     * @param messageTimestamp the timestamp of the message
     */
    public void setMessageTimestamp(Timestamp messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    /**
     * Create a Message object from a ResultSet
     * @param rs the ResultSet from the database query
     * @return the Message object
     * @throws SQLException
     */
    public static Message fromResultSet(ResultSet rs) throws SQLException {
    	int id = rs.getInt("message_id");
    	MessageType messageType = MessageType.stringToMessageType(rs.getString("messageType"));
        String messageContent = rs.getString("messageContent");
        
        String senderUsername = rs.getString("senderUsername");
        User sender = DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username = '" + senderUsername + "'");
        
        Role senderRole = Role.valueOf(rs.getString("senderRole"));
        
        String threadUsername = rs.getString("threadUsername");
        User thread = (threadUsername != null) ? DatabaseHelper.getOneUser("SELECT * FROM cse360users WHERE username = '" + threadUsername + "'") : null;
                
        Timestamp messageTimestamp = rs.getTimestamp("messageTimestamp");

        return new Message(id,messageType, messageContent, sender, senderRole, thread, messageTimestamp);
    }
}
