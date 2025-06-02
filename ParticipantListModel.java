import java.util.Vector;

@SuppressWarnings("deprecation")

public class ParticipantListModel extends java.util.Observable {

    private Vector<User> participants = new Vector<>();

    public ParticipantListModel() {

        // Add some dummy users for testing
        participants.add(new User("1", "admin", SecurityUtils.hashSHA256("admin")));
        participants.add(new User("3", "test", SecurityUtils.hashSHA256("test")));
        participants.add(new User("2", "user1", SecurityUtils.hashSHA256("user1")));

        System.out.println("Dummy Users:");
        participants.forEach(user -> System.out.println("  " + user));

    }

    public User registerParticipant(String id, String username, String password) {
        validateUnique(id, username);
        String hashedPassword = SecurityUtils.hashSHA256(password);
        User newUser = new User(id, username, hashedPassword);
        participants.add(newUser);
        return newUser; // Return the created user
    }

    private void validateUnique(String id, String username) {
        for (User user : participants) {
            if (user.getId().equals(id)) throw new IllegalArgumentException("ID exists");
            if (user.getPseudoName().equals(username)) throw new IllegalArgumentException("Username exists");
        }
    }

    public User authenticate(String pseudo, String password) {
        // Loop through participants to find a match
        System.out.println("\n--- Authentication Debug ---");
        System.out.println("Input username: " + pseudo);
        System.out.println("Input password (raw): " + password); // WARNING: Only for debugging

        String hashedPassword = SecurityUtils.hashSHA256(password); // Hash input password
        System.out.println("Hashed input password: " + hashedPassword);

        for (User user : participants) {
            System.out.println("Checking against: " + user); // Uses toString()
            if (user.getPseudoName().equals(pseudo) && user.getPassword().equals(hashedPassword)) {
                System.out.println("Hash match found !!!!!");
                return user;
            }
        }
        System.out.println("No matching user found !!!!!!!!");
        return null; // No match found
    }

    public Vector<User> getParticipants() {
        return participants;
    }
}
