import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

/**
 * abstract class which determines the colour and the values associated 
 * with that colour for a pollutant
 *
 * @author Raihan Islam
 * @version 1.0
 */
public abstract class PollutantColour {
    protected String pollutantName; // name of pollutant
    protected int lowValue; // low values for pollutant
    protected int midValue; // medium values of pollutant
    
    public PollutantColour(String pollutantName, int low, int mid ) {
        this.pollutantName = pollutantName;
        lowValue = low;
        midValue = mid;
        
    }
    
    /**
     * @return the colour depending on the pollution value
     * @param value the data point's pollution value
     */
    public abstract Color getColor(double value);
    
    /**
     * draws a single data point onto the map
     * @param gc the graphics context to draw onto the canvas
     * @param x the x coordinate of the pixel to draw at
     * @param y the y cooordinate of the pixel to draw at
     * @param value the pollution value of the data point
     */
    public void drawDataPoint(GraphicsContext gc, double x, double y, double value) {
        gc.setFill(getColor(value));
        gc.fillRect(x, y, 43, 43);
        
        gc.setStroke(Color.rgb(0,0,0,0.3));  // black transparent outline for each square
        gc.setLineWidth(2);  
        gc.strokeRect(x, y, 43, 43);
    }
    
    /**
     * @return the lowValue
     */
    public int getLowValue()
    {
        return lowValue;
    }
    
    /**
     * @return the midValue
     */
    public int getMidValue()
    {
        return midValue;
    }
}
