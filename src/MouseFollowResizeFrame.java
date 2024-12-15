import javax.swing.*;
import java.awt.*;

/**
 * Frame that handles the MouseResize panel.
 */
public class MouseFollowResizeFrame extends JFrame {
    final int SCREEN_WIDTH = 1000;
    final int SCREEN_HEIGHT = 1000;

    MouseFollowResizeFrame() {
        JPanel innerPanel = new PreprocessingPanel();

        MouseFollowResizePanel panel = new MouseFollowResizePanel(
                innerPanel, new Dimension(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2)
        );
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}
