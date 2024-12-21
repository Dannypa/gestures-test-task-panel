import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void panelExample() {
        JPanel panel = new GridBagPreprocessingPanel();
        new MouseFollowAndResizeFrame(
                panel,
                new Dimension(
                        MouseFollowAndResizeFrame.SCREEN_WIDTH / 2,
                        MouseFollowAndResizeFrame.SCREEN_HEIGHT / 2
                )
        );
    }

    public static void memeExample() {
        try {
            BufferedImage meme = ImageIO.read(new File("resources/meme.png"));
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.drawImage(meme, 0, 0, getWidth(), getHeight(), this);
                }
            };
            new MouseFollowAndResizeFrame(
                    panel,
                    new Dimension(
                            meme.getWidth(),
                            meme.getHeight()
                    )
            );
        } catch (IOException e) {
            System.out.println("Can't open the meme :(");
        }

    }


    public static void main(String[] args) {
        panelExample();
//        memeExample();
    }
}