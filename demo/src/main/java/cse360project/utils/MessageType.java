package cse360project.utils;

/**
 * Enum representing the different types of messages in the system.
 */
public enum MessageType {
    GENERIC,
    SPECIFIC;

    /**
     * Convert an enum MessageType into a string specifying the type.
     * 
     * @param messageType the MessageType to convert
     * @return a string specifying the type of the messag e
     */
    public static String messageTypeToString(MessageType messageType) {
        if (messageType == MessageType.GENERIC) {
            return "GENERIC";
        }
        if (messageType == MessageType.SPECIFIC) {
            return "SPECIFIC";
        }
        return null;
    }

    /**
     * Convert a string specifying a message type into a MessageType.
     * 
     * @param type a string that specifies the type of the message
     * @return a MessageType corresponding to the string
     */
    public static MessageType stringToMessageType(String type) {
        if (type.toUpperCase().equals("GENERIC")) {
            return MessageType.GENERIC;
        }
        if (type.toUpperCase().equals("SPECIFIC")) {
            return MessageType.SPECIFIC;
        }
        return null;
    }
}
