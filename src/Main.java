import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage meme = ImageIO.read(new File("resources/meme.png"));

            new MemeResizeFrame(meme);
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }
}