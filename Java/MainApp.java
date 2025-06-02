
public class MainApp {
    public static void main(String[] args) {
        ParticipantListModel participantModel = new ParticipantListModel();
        MessageListModel messageModel = new MessageListModel(); // Shared instance

        // Get screen dimensions to position windows appropriately
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Position the first login view on the left side of the screen
        int leftX = (screenWidth / 4) - 200;  // 1/4 of screen width, minus half window width
        int leftY = (screenHeight / 2) - 250;  // Center vertically
        new LoginView(participantModel, messageModel, leftX, leftY);

        // Position the second login view on the right side of the screen
        int rightX = (3 * screenWidth / 4) - 200;  // 3/4 of screen width, minus half window width
        int rightY = (screenHeight / 2) - 250;  // Center vertically
        new LoginView(participantModel, messageModel, rightX, rightY);

        System.out.println("Login views positioned at:");
        System.out.println("  Left view: x=" + leftX + ", y=" + leftY);
        System.out.println("  Right view: x=" + rightX + ", y=" + rightY);
    }
}
