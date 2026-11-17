package simulation.gui;

import simulation.Clock;
import simulation.entities.Entity;
import simulation.field.Field;
import simulation.field.FieldStats;
import simulation.common.Location;
import simulation.weather.WeatherCondition;

import java.awt.*;
import javax.swing.*;

/**
 * A graphical view of the simulation grid.
 * The view displays a colored rectangle for each location
 * representing its contents. It uses a default background color.
 *
 * @author David J. Barnes
 * @author Michael Kölling
 * @author Karim Malik
 * @author Ayan Mamun
 * @version 7.1
 */
public class SimulatorView extends JFrame {
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final JLabel stepLabel;
    private final JLabel population;
    private final FieldView fieldView;

    // A statistics object computing and storing simulation information
    private final FieldStats stats;

    /**
     * Create a view of the given width and height.
     *
     * @param height     The simulation's height.
     * @param width      The simulation's width.
     * @param fieldStats Reference to the field stats instance.
     */
    public SimulatorView(int height, int width, FieldStats fieldStats) {
        stats = fieldStats;

        setTitle("Tundra Environmental Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        contents.add(stepLabel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Show the current status of the field.
     *
     * @param step  Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(Clock simulationClock, WeatherCondition weatherCondition, Field field) {
        if (!isVisible()) {
            setVisible(true);
        }

        String label = STEP_PREFIX + simulationClock.getStep() + "      Weather: " + weatherCondition.toString() + "      Hour: " + simulationClock.getTimeString();
        stepLabel.setText(label);

        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Entity entity = field.getEntityAt(new Location(row, col));
                if (entity != null) {
                    Color color = entity.getColor();
                    if (color != null) {
                        fieldView.drawMark(col, row, entity.getColor());
                    } else {
                        fieldView.drawMark(col, row, UNKNOWN_COLOR);
                    }
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Provide a graphical view of a rectangular field. This is
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this
     * for your project if you like.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private final int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                Dimension currentSize = getSize();
                if (size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                } else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
