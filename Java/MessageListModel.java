import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
@SuppressWarnings("deprecation")

public class MessageListModel extends java.util.Observable {
    private Vector<Message> MessageList = new Vector<Message>();
    private Map<String, User> userMap = new HashMap<>(); // Cache of users by pseudoName

    
    //Registers a user in the message model for encryption/decryption
    public void registerUser(User user) {
        userMap.put(user.getPseudoName(), user);
        System.out.println("Registered user in MessageListModel: " + user.getPseudoName());
    }

    
    //Gets a user by their pseudoName 
    public User getUser(String pseudoName) {
        return userMap.get(pseudoName);
    }

    //Sends an encrypted message from sender to receiver 
    public void sendMessage(String senderName, String receiverName, String content) {
        User sender = getUser(senderName);
        User receiver = getUser(receiverName);

        if (sender == null || receiver == null) {
            System.err.println("Error: Sender or receiver not found in user map");
            // Create unencrypted message as fallback
            Message message = new Message(senderName, receiverName, content);
            MessageList.add(message);
            setChanged();
            notifyObservers(message);
            return;
        }

        // Encrypt the message with receiver's public key
        String encryptedContent = sender.encryptMessage(content, receiver);

        System.out.println("\n--- Message Encryption Debug ---");
        System.out.println("From: " + senderName + " To: " + receiverName);
        System.out.println("Original content: " + content);
        System.out.println("Encrypted content: " + encryptedContent);

        // Create message with encrypted content
        Message message = new Message(senderName, receiverName, encryptedContent);

        // If the current user is the receiver, decrypt it immediately
        if (sender == receiver) {
            String decryptedContent = receiver.decryptMessage(encryptedContent);
            message.setDecryptedContent(decryptedContent);
            System.out.println("Self-message decrypted: " + decryptedContent);
        }

        MessageList.add(message);
        setChanged();
        notifyObservers(message);
    }

    /**
     * Attempts to decrypt a message for the specified user
     */
    public void decryptMessage(Message message, String userPseudoName) {
        // Only decrypt if the user is the intended receiver
        if (!message.getReceiver().equals(userPseudoName)) {
            return;
        }

        User receiver = getUser(userPseudoName);
        if (receiver != null && !message.isDecrypted()) {
            String decryptedContent = receiver.decryptMessage(message.getEncryptedContent());
            message.setDecryptedContent(decryptedContent);

            System.out.println("\n--- Message Decryption Debug ---");
            System.out.println("Decrypted message for: " + userPseudoName);
            System.out.println("From: " + message.getSender());
            System.out.println("Encrypted: " + message.getEncryptedContent());
            System.out.println("Decrypted: " + decryptedContent);

            // Notify observers that the message has been updated
            setChanged();
            notifyObservers(message);
        }
    }

    /**
     * Gets all messages
     */
    public Vector<Message> getMessages() {
        return new Vector<>(MessageList);
    }

    /**
     * Gets messages for a specific user, attempting to decrypt them if needed
     */
    public Vector<Message> getMessagesForUser(String userPseudoName) {
        Vector<Message> userMessages = new Vector<>();

        for (Message message : MessageList) {
            // If user is sender or receiver, include the message
            if (message.getSender().equals(userPseudoName) ||
                message.getReceiver().equals(userPseudoName)) {

                // Try to decrypt if user is the receiver and message isn't decrypted
                if (message.getReceiver().equals(userPseudoName) && !message.isDecrypted()) {
                    decryptMessage(message, userPseudoName);
                }

                userMessages.add(message);
            }
        }

        return userMessages;
    }
}