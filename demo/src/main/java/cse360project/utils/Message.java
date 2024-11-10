package cse360project.utils;

import java.sql.Timestamp;

import cse360project.User;

public class Message {

    public enum MessageType {
        TEXT, IMAGE, VIDEO, FILE
    }

    private MessageType messageType;
    private String messageContent;
    private User sender;
    private Role senderRole;
    private User thread;
    private Timestamp messageTimestamp;

    // Constructor
    public Message(MessageType messageType, String messageContent, User sender, Role senderRole, User thread) {
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.sender = sender;
        this.senderRole = senderRole;
        this.thread = thread;
        this.messageTimestamp = new Timestamp(System.currentTimeMillis());
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

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", messageContent='" + messageContent + '\'' +
                ", sender=" + sender.username +
                ", senderRole=" + senderRole +
                ", thread=" + thread.username +
                ", messageTimestamp=" + messageTimestamp +
                '}';
    }
}
