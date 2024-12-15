import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedImage meme = ImageIO.read(new File("resources/meme.png"));

            new MouseFollowResizeFrame(meme);
        } catch (IOException e) {
            System.out.println("Can't open the meme: " + e.getMessage());
        }
    }
}