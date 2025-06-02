import javax.swing.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")

public class ChatView extends JFrame implements Observer {
    private final MessageListModel messageModel;
    private final User currentUser;

    private JComboBox<String> participantsBox;
    private final DefaultComboBoxModel<String> participantList = new DefaultComboBoxModel<>();
    private final DefaultListModel<String> messageList = new DefaultListModel<>();
    private JList<String> messageDisplay;
    private JTextField messageInput;
    private JLabel statusLabel;

    public ChatView(MessageListModel messageModel, User participant,
                    ParticipantListModel participantModel, int h, int v) {

        this.messageModel = messageModel;
        this.currentUser = participant;
        messageModel.addObserver(this);

        initComponents();
        updateParticipantList(participantModel.getParticipants());

        setSize(800, 600);
        setLocation(h, v);
        setTitle("Secure Chat - " + currentUser.getPseudoName());
        setResizable(true);
        ModernTheme.styleFrame(this);
        setVisible(true);
    }

    private void initComponents() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout(0, 0));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernTheme.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Chat with");
        titleLabel.setFont(ModernTheme.HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Status label for selected recipient
        statusLabel = new JLabel("Select a recipient");
        statusLabel.setFont(ModernTheme.REGULAR_FONT);
        statusLabel.setForeground(new Color(220, 220, 220));
        headerPanel.add(statusLabel, BorderLayout.EAST);

        container.add(headerPanel, BorderLayout.NORTH);

        // Create main content panel with BorderLayout instead of split pane
        JPanel mainContentPanel = new JPanel(new BorderLayout(0, 0));

        // Left panel - Chat messages
        JPanel chatPanel = new JPanel(new BorderLayout(0, 0));
        chatPanel.setBackground(Color.WHITE);

        // Message display with custom cell renderer
        messageDisplay = new JList<>(messageList);
        ModernTheme.styleList(messageDisplay);
        messageDisplay.setCellRenderer(new MessageCellRenderer());

        JScrollPane messageScrollPane = new JScrollPane(messageDisplay);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        messageScrollPane.getViewport().setBackground(Color.WHITE);
        chatPanel.add(messageScrollPane, BorderLayout.CENTER);

        // Message input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageInput = new JTextField();
        ModernTheme.styleTextField(messageInput);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonsPanel.setBackground(ModernTheme.BACKGROUND_COLOR);

        JButton clearButton = new JButton("Clear");
        ModernTheme.styleButton(clearButton, false);

        JButton sendButton = new JButton("Send");
        ModernTheme.styleButton(sendButton, true);

        buttonsPanel.add(clearButton);
        buttonsPanel.add(sendButton);

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.EAST);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        // Right panel - Participants (with fixed width)
        JPanel participantsPanel = new JPanel(new BorderLayout());
        participantsPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        participantsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        participantsPanel.setPreferredSize(new Dimension(200, Integer.MAX_VALUE)); // Fixed width

        JLabel participantsLabel = new JLabel("Participants");
        participantsLabel.setFont(ModernTheme.HEADER_FONT);
        participantsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        participantsPanel.add(participantsLabel, BorderLayout.NORTH);

        participantsBox = new JComboBox<>(participantList);
        ModernTheme.styleComboBox(participantsBox);
        participantsBox.setPreferredSize(new Dimension(180, 35));

        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboBoxPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        comboBoxPanel.add(participantsBox);
        participantsPanel.add(comboBoxPanel, BorderLayout.CENTER);

        // Add a border to visually separate the panels
        participantsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, ModernTheme.LIGHT_TEXT_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Add panels to main content panel
        mainContentPanel.add(chatPanel, BorderLayout.CENTER);
        mainContentPanel.add(participantsPanel, BorderLayout.EAST);
        container.add(mainContentPanel, BorderLayout.CENTER);

        // Add action listeners
        participantsBox.addActionListener(e -> {
            String selected = (String) participantsBox.getSelectedItem();
            if (selected != null) {
                statusLabel.setText("Chatting with: " + selected);
                titleLabel.setText("Chat with " + selected);
            } else {
                statusLabel.setText("Select a recipient");
                titleLabel.setText("Chat");
            }
        });

        // Clear button action listener
        clearButton.addActionListener(e -> {
            messageInput.setText("");
            messageInput.requestFocus();
        });

        // Send button action listener
        sendButton.addActionListener(e -> {
            String message = messageInput.getText().trim();
            String receiver = (String) participantsBox.getSelectedItem();

            if (!message.isEmpty() && receiver != null) {
                messageModel.sendMessage(
                        currentUser.getPseudoName(), // Use logged-in user as sender
                        receiver, // Selected participant as receiver
                        message);
                messageInput.setText("");
            }
        });

        // Add container to frame
        setContentPane(container);
    }

    // Custom cell renderer for chat messages
    private class MessageCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;
        private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            JPanel panel = new JPanel(new BorderLayout(5, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            String messageText = (String) value;
            String[] parts = messageText.split(": ", 2);

            if (parts.length == 2) {
                String header = parts[0]; // "sender to receiver"
                String content = parts[1]; // message content with encryption status

                boolean isFromCurrentUser = header.startsWith(currentUser.getPseudoName());
                boolean isEncrypted = content.contains("[Encrypted]");
                boolean isDecrypted = content.contains("[Decrypted]");

                // Remove the encryption status tag for display
                if (isEncrypted) {
                    content = content.replace(" [Encrypted]", "");
                } else if (isDecrypted) {
                    content = content.replace(" [Decrypted]", "");
                }

                // Create message bubble
                JPanel bubblePanel = new JPanel();
                bubblePanel.setLayout(new BoxLayout(bubblePanel, BoxLayout.Y_AXIS));

                // Set alignment and colors based on sender
                if (isFromCurrentUser) {
                    panel.setBackground(new Color(240, 240, 240));
                    bubblePanel.setBackground(new Color(240, 240, 240));
                    panel.add(bubblePanel, BorderLayout.EAST);
                } else {
                    panel.setBackground(Color.WHITE);
                    bubblePanel.setBackground(Color.WHITE);
                    panel.add(bubblePanel, BorderLayout.WEST);
                }

                // Header (sender and receiver)
                JLabel headerLabel = new JLabel(header);
                headerLabel.setFont(ModernTheme.SMALL_FONT);
                headerLabel.setForeground(ModernTheme.LIGHT_TEXT_COLOR);
                bubblePanel.add(headerLabel);

                // Message content
                JLabel contentLabel = new JLabel(content);
                contentLabel.setFont(ModernTheme.REGULAR_FONT);
                bubblePanel.add(contentLabel);

                // Encryption status indicator
                JLabel encryptionLabel = new JLabel();
                encryptionLabel.setFont(ModernTheme.SMALL_FONT);

                if (isEncrypted) {
                    encryptionLabel.setText("Encrypted");
                    encryptionLabel.setForeground(ModernTheme.ACCENT_COLOR);
                } else if (isDecrypted) {
                    encryptionLabel.setText("Decrypted");
                    encryptionLabel.setForeground(ModernTheme.SECONDARY_COLOR);
                }
                bubblePanel.add(encryptionLabel);

                // Time (simulated)
                JLabel timeLabel = new JLabel(LocalDateTime.now().format(timeFormatter));
                timeLabel.setFont(ModernTheme.SMALL_FONT);
                timeLabel.setForeground(ModernTheme.LIGHT_TEXT_COLOR);
                bubblePanel.add(timeLabel);
            }

            return panel;
        }
    }

    private void updateParticipantList(Vector<User> participants) {
        participantList.removeAllElements();
        for (User p : participants) {
            // Exclude current user from recipient list
            if (!p.getPseudoName().equals(currentUser.getPseudoName())) {
                participantList.addElement(p.getPseudoName());
            }
        }
        if (participantList.getSize() > 0 && participantsBox != null) {
            participantsBox.setSelectedIndex(0);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MessageListModel) {
            // Get messages relevant to the current user and decrypt if needed
            Vector<Message> messages = messageModel.getMessagesForUser(currentUser.getPseudoName());

            SwingUtilities.invokeLater(() -> {
                messageList.clear();
                for (Message msg : messages) {
                    // Format message with encryption status
                    String formatted;

                    if (msg.getReceiver().equals(currentUser.getPseudoName()) && msg.isDecrypted()) {
                        // Message is for current user and decrypted
                        formatted = String.format("%s to %s: %s [Decrypted]",
                                msg.getSender(),
                                msg.getReceiver(),
                                msg.getContent());
                    } else if (msg.getSender().equals(currentUser.getPseudoName())) {
                        // Message is from current user (encrypted for recipient)
                        formatted = String.format("%s to %s: %s [Encrypted]",
                                msg.getSender(),
                                msg.getReceiver(),
                                msg.getContent());
                    } else {
                        // Message is not for current user (can't decrypt)
                        formatted = String.format("%s to %s: %s [Encrypted]",
                                msg.getSender(),
                                msg.getReceiver(),
                                msg.getEncryptedContent());
                    }

                    messageList.addElement(formatted);
                }
            });
        } else if (o instanceof ParticipantListModel) {
            updateParticipantList(((ParticipantListModel) o).getParticipants());
        }
    }
}