import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
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
 * Currently prints the distance to the side when mouse moves.
 * TODO: draw a meme inside when mouse enters and resize it when mouse moves
 */
public class MouseMemeResizePanel extends JPanel {

    /**
     * The side from which the mouse entered last time.
     */
    private Side entranceSide;

    public MouseMemeResizePanel() {
        MouseInputAdapter handler = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                entranceSide = getClosestSide(e.getLocationOnScreen());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                assert entranceSide != null;
                System.out.println(getDistanceToSide(e.getLocationOnScreen(), entranceSide));
            }
        };

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
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
     * @param p The point whose coordinate is returned.
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
     * @param p The point which we compute the distance from.
     * @param side Determines the side we compute the distance to.
     * @return The distance from the point to the panel's side.
     */
    private int getDistanceToSide(Point p, Side side) {
        return Math.abs(getRelevantCoordinate(p, side) - getPanelSide(side));
    }
}
