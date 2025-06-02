import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * ModernTheme provides consistent styling for the application UI components.
 * It defines colors, fonts, and component styling methods for a modern look and feel.
 */
public class ModernTheme {
    // Color scheme
    public static final Color PRIMARY_COLOR = new Color(66, 133, 244);      // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 168, 83);     // Green
    public static final Color ACCENT_COLOR = new Color(234, 67, 53);        // Red
    public static final Color BACKGROUND_COLOR = new Color(248, 249, 250);  // Light gray
    public static final Color TEXT_COLOR = new Color(60, 64, 67);           // Dark gray
    public static final Color LIGHT_TEXT_COLOR = new Color(154, 160, 166);  // Light text
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    
    // Borders and padding
    public static final int PADDING = 15;
    public static final int SMALL_PADDING = 8;
    public static final int BORDER_RADIUS = 8;
    
    /**
     * Applies modern styling to a JButton
     */
    public static void styleButton(JButton button, boolean isPrimary) {
        Color bgColor = isPrimary ? PRIMARY_COLOR : BACKGROUND_COLOR;
        Color fgColor = isPrimary ? Color.WHITE : TEXT_COLOR;
        
        button.setFont(REGULAR_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // Add rounded corners with empty border for padding
        Border roundedBorder = new LineBorder(bgColor, 1, true);
        Border emptyBorder = new EmptyBorder(SMALL_PADDING, PADDING, SMALL_PADDING, PADDING);
        button.setBorder(new CompoundBorder(roundedBorder, emptyBorder));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(isPrimary ? 
                    PRIMARY_COLOR.darker() : new Color(230, 230, 230));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Applies modern styling to a JTextField
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(REGULAR_FONT);
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        
        // Add rounded corners with empty border for padding
        Border roundedBorder = new LineBorder(LIGHT_TEXT_COLOR, 1, true);
        Border emptyBorder = new EmptyBorder(SMALL_PADDING, SMALL_PADDING, SMALL_PADDING, SMALL_PADDING);
        textField.setBorder(new CompoundBorder(roundedBorder, emptyBorder));
    }
    
    /**
     * Applies modern styling to a JPasswordField
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(REGULAR_FONT);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(TEXT_COLOR);
        
        // Add rounded corners with empty border for padding
        Border roundedBorder = new LineBorder(LIGHT_TEXT_COLOR, 1, true);
        Border emptyBorder = new EmptyBorder(SMALL_PADDING, SMALL_PADDING, SMALL_PADDING, SMALL_PADDING);
        passwordField.setBorder(new CompoundBorder(roundedBorder, emptyBorder));
    }
    
    /**
     * Applies modern styling to a JLabel
     */
    public static void styleLabel(JLabel label) {
        label.setFont(REGULAR_FONT);
        label.setForeground(TEXT_COLOR);
    }
    
    /**
     * Applies modern styling to a JPanel
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
    }
    
    /**
     * Applies modern styling to a JFrame
     */
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Applies modern styling to a JComboBox
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(REGULAR_FONT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_COLOR);
        
        // Add rounded corners with empty border for padding
        Border roundedBorder = new LineBorder(LIGHT_TEXT_COLOR, 1, true);
        Border emptyBorder = new EmptyBorder(SMALL_PADDING, SMALL_PADDING, SMALL_PADDING, SMALL_PADDING);
        comboBox.setBorder(new CompoundBorder(roundedBorder, emptyBorder));
    }
    
    /**
     * Applies modern styling to a JList
     */
    public static void styleList(JList<?> list) {
        list.setFont(REGULAR_FONT);
        list.setBackground(Color.WHITE);
        list.setForeground(TEXT_COLOR);
        list.setSelectionBackground(PRIMARY_COLOR);
        list.setSelectionForeground(Color.WHITE);
    }
    
    /**
     * Creates a styled scroll pane
     */
    public static JScrollPane createStyledScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(new LineBorder(LIGHT_TEXT_COLOR, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }
    
    /**
     * Creates a header panel with title
     */
    public static JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
}
