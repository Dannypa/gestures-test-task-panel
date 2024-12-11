import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Enum that represents the four sides that the panel has.
 */
enum Side {
    TOP, RIGHT, BOTTOM, LEFT
}

/**
 * The panel that records when mouse enters and the side it enters from.
 * Draws a meme if the mouse is inside the panel.
 * Currently prints the distance to the side when mouse moves.
 * TODO: draw a meme inside when mouse enters and resize it when mouse moves
 */
public class MouseMemeResizePanel extends JPanel {

    /**
     * The side from which the mouse entered last time.
     */
    private Side entranceSide;

    /**
     * The meme we are going to draw.
     */
    private final BufferedImage meme;
    /**
     * The original size of the meme.
     */
    private final Dimension originalMemeSize;
    /**
     * The current size of the meme; depends on the mouse position.
     */
    private Dimension currentMemeSize;
    /**
     * Determines if the meme is visible; basically checks if the mouse is inside the panel.
     */
    private boolean isVisible = false;

    public MouseMemeResizePanel(String pathToMeme) throws IOException {
        this.setLayout(null);

        meme = ImageIO.read(new File(pathToMeme));
        originalMemeSize = new Dimension(meme.getWidth(), meme.getHeight());
        currentMemeSize = new Dimension(meme.getWidth() / 2, meme.getHeight() / 2);

        MouseInputAdapter handler = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                entranceSide = getClosestSide(e.getLocationOnScreen());
                isVisible = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                isVisible = false;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                assert entranceSide != null;
//                System.out.println(getDistanceToSide(e.getLocationOnScreen(), entranceSide));
            }
        };

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isVisible) {
            g.drawImage(meme, 0, 0, currentMemeSize.width, currentMemeSize.height, this);
        }
    }

    /**
     * @param side Determines which side is considered.
     * @return The relevant coordinate of the panel's side.
     * If the side is TOP or BOTTOM, returns its y; otherwise returns its x.
     */
    private int getPanelSide(Side side) {
        // if side in {top, bottom} returns y; otherwise returns x
        Point panelLocation = getLocationOnScreen();
        Dimension panelSize = getSize();

        if (side == Side.TOP) return panelLocation.y;
        else if (side == Side.RIGHT) return panelLocation.x + panelSize.width;
        else if (side == Side.BOTTOM) return panelLocation.y + panelSize.height;
        else return panelLocation.x;
    }

    /**
     * @param p    The point whose coordinate is returned.
     * @param side The side that we compute the coordinate for.
     * @return The coordinate relevant for this side.
     * If the side is TOP or BOTTOM, returns its y; otherwise returns its x.
     */
    private int getRelevantCoordinate(Point p, Side side) {
        if (side == Side.TOP || side == Side.BOTTOM) {
            return p.y;
        } else {
            return p.x;
        }
    }

    /**
     * @param p The point which we compute the distance from.
     * @return The side that is closest to the point.
     */
    private Side getClosestSide(Point p) {
        // not always clear: what if it is diagonal?
        // for now let's just return something; may be improved later

        int[][] distances = new int[][]{
                new int[]{Math.abs(getPanelSide(Side.TOP) - p.y), 0}, // top
                new int[]{Math.abs(getPanelSide(Side.RIGHT) - p.x), 1}, // right
                new int[]{Math.abs(getPanelSide(Side.BOTTOM) - p.y), 2}, // bottom
                new int[]{Math.abs(getPanelSide(Side.LEFT) - p.x), 3}, // left
        };
        Arrays.sort(distances, Comparator.comparingInt(a -> a[0]));

        return Side.values()[distances[0][1]];
    }

    /**
     * @param p    The point which we compute the distance from.
     * @param side Determines the side we compute the distance to.
     * @return The distance from the point to the panel's side.
     */
    private int getDistanceToSide(Point p, Side side) {
        return Math.abs(getRelevantCoordinate(p, side) - getPanelSide(side));
    }
}
