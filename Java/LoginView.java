import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
@SuppressWarnings("deprecation")

public class LoginView extends javax.swing.JFrame implements java.util.Observer {
    private final ParticipantListModel participantModel;
    private final MessageListModel messageModel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView(ParticipantListModel participantModel,
                MessageListModel messageModel, int h, int v) {
        this.participantModel = participantModel;
        this.messageModel = messageModel;

        initializeUI();
        setLocation(h, v);
        setTitle("Secure Chat - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        ModernTheme.styleFrame(this);
        setVisible(true);
    }

    private void initializeUI() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        ModernTheme.stylePanel(container);

        // Create header panel with app title
        JPanel headerPanel = ModernTheme.createHeaderPanel("Secure Chat");
        container.add(headerPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Add login form title
        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(ModernTheme.HEADER_FONT);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(loginTitle);
        formPanel.add(Box.createVerticalStrut(20));

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        ModernTheme.styleLabel(usernameLabel);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));

        usernameField = new JTextField();
        ModernTheme.styleTextField(usernameField);
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        ModernTheme.styleLabel(passwordLabel);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));

        passwordField = new JPasswordField();
        ModernTheme.stylePasswordField(passwordField);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(30));

        // Buttons
        JButton loginButton = new JButton("Sign In");
        ModernTheme.styleButton(loginButton, true);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(10));

        JButton registerButton = new JButton("Create Account");
        ModernTheme.styleButton(registerButton, false);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(10));

        JButton closeButton = new JButton("Exit");
        ModernTheme.styleButton(closeButton, false);
        closeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(closeButton);

        container.add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        loginButton.addActionListener(this::performLogin);
        registerButton.addActionListener(this::performRegister);
        closeButton.addActionListener(e -> System.exit(0));

        // Add container to frame
        setContentPane(container);
    }

    private void performLogin(ActionEvent evt) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()).trim();

        System.out.println("Attempting login with: " + username + "/" + password); // Debug
        // Input validation
        if (!SecurityUtils.isValidInput(username)) {
            showError("Invalid username format");
            return;
        }

        if (SecurityUtils.containsSQLPatterns(password)) {
            showError("Password contains suspicious patterns");
            return;
        }

        User user = participantModel.authenticate(username, password);

        if (user != null) {
            System.out.println("Login SUCCESS for: " + user.getPseudoName()); // Debug
            // Pass the current position to maintain left/right positioning
            new WelcomeView(user, participantModel, messageModel, getX(), getY(), isLeftSide());
            this.dispose();// Close the registration window
        } else {
            System.out.println("Login FAILED"); // Debug
            showError("Invalid credentials");
        }
    }

    /**
     * Determines if this login view is on the left side of the screen
     * This helps maintain consistent positioning of subsequent views
     */
    private boolean isLeftSide() {
        // Get screen width
        int screenWidth = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        // If X position is less than half the screen width, it's on the left side
        return getX() < (screenWidth / 2);
    }

    private void performRegister(ActionEvent evt) {
        // Open registration window near the login window
        new RegisterView(participantModel, messageModel, this.getX() + 50, this.getY() + 50);
        this.dispose();// Close the registration window
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Security Alert", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        // Update logic for observer pattern if needed
    }
}