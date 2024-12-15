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
 * Draws the passed panel and makes it follow the mouse such that mouse is at the center of the panel.
 * Resizes the panel based on the mouse movement: the dimensions are updated by the formula
 * currentD = min(originalD / b + k * distance(mouse, side it entered from), originalD)
 */
public class MouseFollowResizePanel extends JPanel {

    /**
     * Initial scaling factor b from the formula above.
     */
    private final int INITIAL_SCALE = 2;

    /**
     * Scaling factor k from the formula above.
     */
    private final double SCALING_FACTOR = 0.001;

    /**
     * The side from which the mouse entered last time.
     */
    private Side entranceSide;

    /**
     * The original size of the panel.
     */
    private final Dimension originalPanelSize;

    public MouseFollowResizePanel(JPanel panel, Dimension panelSize) {
        this.setLayout(null);

        add(panel);
        originalPanelSize = panelSize;

        MouseInputAdapter handler = new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                entranceSide = getClosestSide(e.getLocationOnScreen());
                panel.setVisible(true);
                panel.setSize(new Dimension(
                        originalPanelSize.width / INITIAL_SCALE,
                        originalPanelSize.height / INITIAL_SCALE
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                panel.setVisible(false);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                assert entranceSide != null;

                int distanceToSide = getDistanceToSide(e.getLocationOnScreen(), entranceSide);
                panel.setSize(getCurrentPanelSize(distanceToSide));
                Point currentMousePosition = e.getPoint();
                currentMousePosition.x -= panel.getSize().width / 2;
                currentMousePosition.y -= panel.getSize().height / 2;
                panel.setLocation(currentMousePosition);
                panel.revalidate();
            }
        };

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }

    /**
     * Applies the scaling formula to a number (panel dimension).
     *
     * @param original       Original dimension.
     * @param distanceToSide Current distance from mouse to the side of the panel it entered from.
     * @return The result of the scaling of the original dimension given by the formula in the class description.
     */
    private int getCurrentPanelDimension(int original, int distanceToSide) {
        return Math.min(
                original / INITIAL_SCALE + (int) ((distanceToSide * SCALING_FACTOR) * original),
                original
        );
    }

    /**
     * Applies the scaling formula to get the current panel size.
     *
     * @param distanceToSide Current distance from mouse to the side of the panel it entered from.
     * @return The result of scaling of the panel size given by the formula in the class description.
     */
    private Dimension getCurrentPanelSize(int distanceToSide) {
        return new Dimension(
                getCurrentPanelDimension(originalPanelSize.width, distanceToSide),
                getCurrentPanelDimension(originalPanelSize.height, distanceToSide)
        );
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