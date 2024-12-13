import javax.swing.*;
import java.awt.image.BufferedImage;

public class MemeResizeFrame extends JFrame {
    MemeResizeFrame(BufferedImage meme) {
        MemeResizePanel panel = new MemeResizePanel(meme);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}
