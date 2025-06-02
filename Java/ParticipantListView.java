import java.util.Observable;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("deprecation")

public class ParticipantListView extends javax.swing.JFrame implements java.util.Observer {
    private ParticipantListModel model;
    private JList<String> listComponent;

    public ParticipantListView(ParticipantListModel model, int x, int y) {
        this.model = model;
        model.addObserver(this);
        initComponents();
        setLocation(x, y);
        setTitle("Secure Chat - Participants");
        setSize(400, 500);
        setResizable(false);
        ModernTheme.styleFrame(this);
        setVisible(true);
    }

    private void initComponents() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        ModernTheme.stylePanel(container);

        // Create header panel
        JPanel headerPanel = ModernTheme.createHeaderPanel("Participants");
        container.add(headerPanel, BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create and style list component
        listComponent = new JList<>();
        ModernTheme.styleList(listComponent);
        listComponent.setCellRenderer(new ParticipantCellRenderer());

        // Create styled scroll pane
        JScrollPane scrollPane = ModernTheme.createStyledScrollPane(listComponent);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Update list with current participants
        updateList();

        // Add info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel countLabel = new JLabel("Total participants: " + model.getParticipants().size());
        countLabel.setFont(ModernTheme.REGULAR_FONT);
        infoPanel.add(countLabel, BorderLayout.WEST);

        JButton closeButton = new JButton("Close");
        ModernTheme.styleButton(closeButton, false);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        infoPanel.add(buttonPanel, BorderLayout.EAST);

        container.add(contentPanel, BorderLayout.CENTER);
        container.add(infoPanel, BorderLayout.SOUTH);

        // Add container to frame
        setContentPane(container);
    }

    private void updateList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (User user : model.getParticipants()) {
            listModel.addElement(user.getPseudoName() + " (" + user.getId() + ")");
        }
        listComponent.setModel(listModel);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateList();
    }

    // Custom cell renderer for participants
    private class ParticipantCellRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = 1L;

        private String[] extractUsernameParts(String participantText) {
            String username = participantText;
            String id = "";

            int openParenIndex = participantText.lastIndexOf('(');
            int closeParenIndex = participantText.lastIndexOf(')');

            if (openParenIndex > 0 && closeParenIndex > openParenIndex) {
                username = participantText.substring(0, openParenIndex).trim();
                id = participantText.substring(openParenIndex + 1, closeParenIndex);
            }

            return new String[] { username, id };
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            if (isSelected) {
                panel.setBackground(ModernTheme.PRIMARY_COLOR);
            } else {
                panel.setBackground(index % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
            }

            String participantText = (String) value;

            // Extract username and ID
            final String[] parts = extractUsernameParts(participantText);
            final String username = parts[0];
            final String id = parts[1];

            // Create avatar circle
            JPanel avatarPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(ModernTheme.PRIMARY_COLOR);
                    g2d.fillOval(0, 0, getWidth(), getHeight());

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));

                    String initial = username.length() > 0 ? username.substring(0, 1).toUpperCase() : "?";
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(initial)) / 2;
                    int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                    g2d.drawString(initial, x, y);
                    g2d.dispose();
                }
            };

            avatarPanel.setPreferredSize(new Dimension(36, 36));
            panel.add(avatarPanel, BorderLayout.WEST);

            // User info panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(panel.getBackground());

            JLabel nameLabel = new JLabel(username);
            nameLabel.setFont(ModernTheme.REGULAR_FONT);
            nameLabel.setForeground(isSelected ? Color.WHITE : ModernTheme.TEXT_COLOR);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel idLabel = new JLabel("ID: " + id);
            idLabel.setFont(ModernTheme.SMALL_FONT);
            idLabel.setForeground(isSelected ? new Color(220, 220, 220) : ModernTheme.LIGHT_TEXT_COLOR);
            idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            infoPanel.add(nameLabel);
            infoPanel.add(idLabel);

            panel.add(infoPanel, BorderLayout.CENTER);

            return panel;
        }
    }
}