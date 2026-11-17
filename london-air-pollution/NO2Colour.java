import javafx.scene.paint.Color;

/**
 * colour definition for pollutant no2
 *
 * @author Raihan Islam
 * @version 1.0
 */
public class NO2Colour extends PollutantColour
{
    public NO2Colour() {
        super("NO2", 20, 27);
    }

    @Override
    public Color getColor(double value) {
        if (value <= lowValue) 
        {
            return Color.rgb(173, 216, 230, 0.5);  // Light Blue
        }
        else if (value <= midValue) 
        {
            return Color.rgb(30, 144, 255, 0.5);  // Blue
        }
        else 
        {
            return Color.rgb(0, 0, 139, 0.3);  // Dark Blue
        }
    }
}
