import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WelcomeView extends JFrame {
    private final User user;
    private final ParticipantListModel participantModel;
    private final MessageListModel messageModel;
    private final boolean isLeftSide; // Track if this view is on the left side

    public WelcomeView(User user, ParticipantListModel model,
                    MessageListModel messageModel, int h, int v, boolean isLeftSide) {
        this.user = user;
        this.participantModel = model;
        this.messageModel = messageModel;
        this.isLeftSide = isLeftSide;

        // Register this user with the message model for encryption/decryption
        messageModel.registerUser(user);

        // Register all other users for message encryption
        for (User otherUser : model.getParticipants()) {
            messageModel.registerUser(otherUser);
        }

        initializeUI();
        setLocation(h, v);
        setTitle("Secure Chat - Dashboard - " + user.getPseudoName());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        ModernTheme.styleFrame(this);
        setVisible(true);

        System.out.println("Opened WelcomeView for " + user.getPseudoName() +
                " on " + (isLeftSide ? "left" : "right") + " side at x=" + h + ", y=" + v);
    }

    private void initializeUI() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        ModernTheme.stylePanel(container);

        // Create header panel with welcome message
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernTheme.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getPseudoName() + "!");
        welcomeLabel.setFont(ModernTheme.TITLE_FONT);
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel userIdLabel = new JLabel("User ID: " + user.getId());
        userIdLabel.setFont(ModernTheme.SMALL_FONT);
        userIdLabel.setForeground(new Color(220, 220, 220));
        headerPanel.add(userIdLabel, BorderLayout.SOUTH);

        container.add(headerPanel, BorderLayout.NORTH);

        // Create main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Add section title
        JLabel sectionLabel = new JLabel("Applications");
        sectionLabel.setFont(ModernTheme.HEADER_FONT);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(sectionLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Create card panel for applications
        JPanel cardPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        cardPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Chat card
        JPanel chatCard = createAppCard("Chat", "Secure messaging with other users", ModernTheme.PRIMARY_COLOR);
        chatCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openChat(null);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                chatCard.setBackground(ModernTheme.PRIMARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                chatCard.setBackground(ModernTheme.PRIMARY_COLOR);
            }
        });

        // Courses card
        JPanel coursesCard = createAppCard("Courses", "Access learning materials", ModernTheme.SECONDARY_COLOR);
        coursesCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                openCourses(null);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                coursesCard.setBackground(ModernTheme.SECONDARY_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                coursesCard.setBackground(ModernTheme.SECONDARY_COLOR);
            }
        });

        cardPanel.add(chatCard);
        cardPanel.add(coursesCard);
        contentPanel.add(cardPanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Logout button
        JButton logoutButton = new JButton("Log Out");
        ModernTheme.styleButton(logoutButton, false);
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 40));
        logoutButton.addActionListener(e -> performLogout());
        contentPanel.add(logoutButton);

        container.add(contentPanel, BorderLayout.CENTER);

        // Add container to frame
        setContentPane(container);
    }

    private JPanel createAppCard(String title, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(ModernTheme.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(ModernTheme.SMALL_FONT);
        descLabel.setForeground(new Color(240, 240, 240));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descLabel);

        return card;
    }

    private void performLogout() {
        this.dispose(); // Close current window
        // Reopen login window at current position
        System.out.println("Log out success"); // Debug
        new LoginView(participantModel, messageModel, this.getX(), this.getY());
    }

    private void openChat(ActionEvent evt) {
        // Calculate position based on whether this view is on left or right side
        int chatX = isLeftSide ? 100 : (int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 900);
        int chatY = 100;

        System.out.println("Opening ChatView for " + user.getPseudoName() +
                " on " + (isLeftSide ? "left" : "right") + " side at x=" + chatX + ", y=" + chatY);

        new ChatView(messageModel, user, participantModel, chatX, chatY);
    }

    private void openCourses(ActionEvent evt) {
        // Calculate position based on whether this view is on left or right side
        int courseX = isLeftSide ? 150 : (int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 850);
        int courseY = 150;

        System.out.println("Opening CourseView for " + user.getPseudoName() +
                " on " + (isLeftSide ? "left" : "right") + " side at x=" + courseX + ", y=" + courseY);

        CourseModel courseModel = new CourseModel(1, "/TP3.pdf", "Initial content");
        new CourseView(courseModel, courseX, courseY);
    }
}