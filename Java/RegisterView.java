import java.util.Observable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("deprecation")

public class RegisterView extends JFrame implements java.util.Observer {
    ParticipantListModel participantmodel;
    private JTextField idField, usernameField;
    private JPasswordField passwordField;
    private final MessageListModel messageModel;

    public RegisterView(ParticipantListModel participantmodel, MessageListModel messageModel,
                    int h, int v) {
        this.participantmodel = participantmodel;
        this.messageModel = messageModel;
        initComponents();
        setLocation(h, v);
        setTitle("Secure Chat - Create Account");
        setSize(400, 550);
        setResizable(false);
        ModernTheme.styleFrame(this);
        setVisible(true);
    }

    private void initComponents() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        ModernTheme.stylePanel(container);

        // Create header panel with app title
        JPanel headerPanel = ModernTheme.createHeaderPanel("Create Account");
        container.add(headerPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // ID field
        JLabel idLabel = new JLabel("User ID");
        ModernTheme.styleLabel(idLabel);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(idLabel);
        formPanel.add(Box.createVerticalStrut(5));

        idField = new JTextField();
        ModernTheme.styleTextField(idField);
        idField.setAlignmentX(Component.LEFT_ALIGNMENT);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(idField);
        formPanel.add(Box.createVerticalStrut(15));

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
        JButton registerButton = new JButton("Create Account");
        ModernTheme.styleButton(registerButton, true);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(10));

        JButton backButton = new JButton("Back to Login");
        ModernTheme.styleButton(backButton, false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(backButton);

        container.add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (!validateInputs(id, username, password))
                return;

            try {
                User newUser = participantmodel.registerParticipant(id, username, password);
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                // Open WelcomeView with the new user
                // Determine if this view is on the left side of the screen
                boolean isLeftSide = getX() < (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
                new WelcomeView(newUser, participantmodel, messageModel, getX() + 50, getY() + 50, isLeftSide);
                dispose(); // Close registration window
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            new LoginView(participantmodel, messageModel, getX(), getY());
            dispose();
        });

        // Add container to frame
        setContentPane(container);
        
        registerButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (!validateInputs(id, username, password))
                return;

            try {

                User newUser = participantmodel.registerParticipant(id, username, password);
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                // Open WelcomeView with the new user
                // Determine if this view is on the left side of the screen
                boolean isLeftSide = getX() < (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
                new WelcomeView(newUser, participantmodel, messageModel, getX() + 50, getY() + 50, isLeftSide);
                dispose(); // Close registration window
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean validateInputs(String id, String username, String password) {
        if (id.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are required!");
            return false;
        }

        if (!id.matches("\\d+")) {
            showError("ID must be numeric");
            return false;
        }

        if (!SecurityUtils.isValidInput(username)) {
            showError("Username: 1-20 alphanumeric chars");
            return false;
        }

        if (SecurityUtils.containsSQLPatterns(password)) {
            showError("Password contains invalid patterns");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
   
    @Override
    public void update(Observable o, Object arg) {
        // This method is required by the Observer interface but not used in this class
    }

}