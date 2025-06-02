import java.security.*;
import java.util.Base64;

public class User {
    private final String id;
    private final String pseudoName;
    private final String passwordHash;

    // RSA key pair for encryption/decryption
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String publicKeyString; // Serialized public key for sharing

    public User(String id, String pseudoName, String passwordHash) {
        if (!SecurityUtils.isValidInput(pseudoName)) {
            throw new IllegalArgumentException("Invalid pseudonym format");
        }
        this.id = id;
        this.pseudoName = pseudoName;
        this.passwordHash = passwordHash;

        // Generate RSA key pair for this user
        generateKeyPair();
    }

    // Getters
    public String getId() { return id; }
    public String getPseudoName() { return pseudoName; }
    public String getPassword() { return passwordHash; }
    public PublicKey getPublicKey() { return publicKey; }
    public PrivateKey getPrivateKey() { return privateKey; }
    public String getPublicKeyString() { return publicKeyString; }

    /**
Generates a new RSA key pair for this user
     */
    private void generateKeyPair() {
        try {
            KeyPair keyPair = RSAUtils.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            this.publicKeyString = RSAUtils.publicKeyToString(publicKey);

            System.out.println("Generated RSA keys for user: " + pseudoName);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to generate RSA keys: " + e.getMessage());
        }
    }

    /**
     * Encrypts a message using the recipient's public key
     */
    public String encryptMessage(String message, User recipient) {
        if (recipient.getPublicKey() == null) {
            return "[ENCRYPTION FAILED - No public key]";
        }
        return RSAUtils.encrypt(message, recipient.getPublicKey());
    }

    /**
     * Decrypts a message using this user's private key
     */
    public String decryptMessage(String encryptedMessage) {
        if (privateKey == null) {
            return "[DECRYPTION FAILED - No private key]";
        }
        return RSAUtils.decrypt(encryptedMessage, privateKey);
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, pseudo=%s]", id, pseudoName);
    }
}