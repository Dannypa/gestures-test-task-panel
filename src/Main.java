import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage meme = ImageIO.read(new File("resources/meme.png"));

            MouseMemeResizePanel panel = new MouseMemeResizePanel(meme);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 1000);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.add(panel);
            frame.setVisible(true);
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }
}