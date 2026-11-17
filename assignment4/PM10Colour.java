import javafx.scene.paint.Color;

/**
 * colour definition for pollutant pm10
 *
 * @author Raihan Islam
 * @version 1.0
 */
public class PM10Colour extends PollutantColour
{ 
    public PM10Colour()
    {
        super("PM10", 17, 19);
    }
    
    @Override
    public Color getColor(double value)
    {
        if (value <= lowValue)
        {
            return Color.rgb(255, 138, 122, 0.5);  // Light Red
        }
        else if (value <= midValue)
        {
            return Color.rgb(255, 0, 0, 0.5);  // Red
        }
        else 
        {
            return Color.rgb(110, 20, 8, 0.3);  // Dark Red
        }
    }
}

