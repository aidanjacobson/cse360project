package cse360project;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cse360project.utils.MessageType;
import cse360project.utils.Role;

public class Message implements Serializable {
    private MessageType messageType;
    private String messageContent;
    private User sender;
    private Role senderRole;
    private User thread;
    private Timestamp messageTimestamp;

    /**
     * Constructor for Message
     * @param messageType the type of message
     * @param messageContent the content of the message
     * @param sender the user who sent the message
     * @param senderRole the role of the sender
     * @param thread the user or thread this message is part of
     * @param messageTimestamp the timestamp when the message was sent
     */
    public Message(MessageType messageType, String messageContent, User sender, Role senderRole, User thread, Timestamp messageTimestamp) {
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.sender = sender;
        this.senderRole = senderRole;
        this.thread = thread;
        this.messageTimestamp = messageTimestamp;
    }

    // Getters and Setters
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
        MessageType messageType = MessageType.valueOf(rs.getString("messageType"));
        String messageContent = rs.getString("messageContent");
        User sender = User.fromResultSet(rs); // Assumes User.fromResultSet() exists
        Role senderRole = Role.valueOf(rs.getString("senderRole"));
        User thread = User.fromResultSet(rs); // Adjust if thread is another type
        Timestamp messageTimestamp = rs.getTimestamp("messageTimestamp");

        return new Message(messageType, messageContent, sender, senderRole, thread, messageTimestamp);
    }
}
