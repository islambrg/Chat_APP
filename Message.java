@SuppressWarnings("deprecation")

public class Message extends java.util.Observable {
    private static int messageIdCounter = 1; 
    private final int messageId;
    private final String sender;
    private final String receiver;
    private final String encryptedContent; 
    private String decryptedContent; // Decrypted content (if available)
    private boolean isDecrypted = false;

    // Creates a new message with encrypted content
    public Message(String sender, String receiver, String encryptedContent) {
        this.messageId = messageIdCounter++;
        this.sender = sender;
        this.receiver = receiver;
        this.encryptedContent = encryptedContent;
    }

    // Creates a new message with both encrypted and decrypted content
    public Message(String sender, String receiver, String encryptedContent, String decryptedContent) {
        this(sender, receiver, encryptedContent);
        this.decryptedContent = decryptedContent;
        this.isDecrypted = true;
    }

    // Getters
    public int getMessageId() { return messageId; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }

    // Gets the encrypted content of the message
    public String getEncryptedContent() {
        return encryptedContent;
    }

    // Gets the decrypted content if available, otherwise returns the encrypted content
    public String getContent() {
        return isDecrypted ? decryptedContent : encryptedContent;
    }

    // Sets the decrypted content for this message
    public void setDecryptedContent(String decryptedContent) {
        this.decryptedContent = decryptedContent;
        this.isDecrypted = true;
    }

    // Checks if this message has been decrypted
    public boolean isDecrypted() {
        return isDecrypted;
    }
}
