import javax.swing.*;
import java.util.Observable;
import java.util.Observer;
import java.awt.*;
@SuppressWarnings("deprecation")

public class CourseView extends JFrame implements Observer {
    private final JTextArea contentArea;
    private final CourseModel courseModel;

    public CourseView(CourseModel courseModel, int h, int v) {
        super("Course Content");
        this.courseModel = courseModel;
        this.courseModel.addObserver(this);

        // Initialize content area
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setFont(ModernTheme.REGULAR_FONT);
        contentArea.setForeground(ModernTheme.TEXT_COLOR);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        initComponents();
        setLocation(h, v);
        setSize(700, 500);
        setTitle("Secure Chat - Course Materials");
        ModernTheme.styleFrame(this);
        setVisible(true);
    }

    private void initComponents() {
        // Set up the main container with BorderLayout
        JPanel container = new JPanel(new BorderLayout());
        ModernTheme.stylePanel(container);

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ModernTheme.SECONDARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Course: " + courseModel.getCourseId());
        titleLabel.setFont(ModernTheme.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel pathLabel = new JLabel("Path: " + courseModel.getCoursePath());
        pathLabel.setFont(ModernTheme.SMALL_FONT);
        pathLabel.setForeground(new Color(220, 220, 220));
        headerPanel.add(pathLabel, BorderLayout.SOUTH);

        container.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Set content
        contentArea.setText(courseModel.getContent());

        // Create styled scroll pane
        JScrollPane scrollPane = ModernTheme.createStyledScrollPane(contentArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        container.add(contentPanel, BorderLayout.CENTER);

        // Footer with close button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(ModernTheme.BACKGROUND_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton closeButton = new JButton("Close");
        ModernTheme.styleButton(closeButton, false);
        closeButton.addActionListener(e -> dispose());
        footerPanel.add(closeButton);

        container.add(footerPanel, BorderLayout.SOUTH);

        // Add container to frame
        setContentPane(container);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CourseModel) {
            contentArea.setText(((CourseModel) o).getContent());
        }
    }
}