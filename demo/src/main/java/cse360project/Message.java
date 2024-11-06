package cse360project;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cse360project.utils.MessageType;
import cse360project.utils.Role;

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
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Role getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(Role senderRole) {
        this.senderRole = senderRole;
    }

    public User getThread() {
        return thread;
    }

    public void setThread(User thread) {
        this.thread = thread;
    }

    public Timestamp getMessageTimestamp() {
        return messageTimestamp;
    }

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
        MessageType messageType = MessageType.valueOf(rs.getString("messageType"));
        String messageContent = rs.getString("messageContent");
        User sender = User.fromResultSet(rs); // Assumes User.fromResultSet() exists
        Role senderRole = Role.valueOf(rs.getString("senderRole"));
        User thread = User.fromResultSet(rs); // Adjust if thread is another type
        Timestamp messageTimestamp = rs.getTimestamp("messageTimestamp");

        return new Message(id,messageType, messageContent, sender, senderRole, thread, messageTimestamp);
    }
}
