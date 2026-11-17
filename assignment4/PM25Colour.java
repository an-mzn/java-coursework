import javafx.scene.paint.Color;

/**
 * colour definition for pollutant pm2.5
 *
 * @author Raihan Islam
 * @version 1.0
 */
public class PM25Colour extends PollutantColour
{
    public PM25Colour() {
        super("PM2.5", 11, 12);
    }

    @Override
    public Color getColor(double value) {
        if (value <= lowValue)
        {
            return Color.rgb(144, 238, 144, 0.5);  // Light Green
        }
        else if (value <= midValue) 
        {
            return Color.rgb(0, 255, 0, 0.5);  // Green
        }
        else 
        {
            return Color.rgb(0, 100, 0, 0.3);  // Dark Green
        }
    }
}

