import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import javafx.scene.chart.*;

/**
 * Unit test class for the PollutionStatsTab class.
 * 
 * This class contains tests for the PollutionStatsTab class, which displays
 * pollution data, allows selection of years and pollutants, and updates a bar chart.
 * Each test verifies the functionality of specific components and methods, 
 * such as selecting years, selecting pollutants, computing average and highest values,
 * and updating the graph.
 * 
 * @author Abeer Hussain
 * @version 1.0
>>>>>>> 103870f5a35cbdf91e3df631e5db644f5523673a
 */
public class PollutionStatsTabTest {

    // The instance of PollutionStatsTab being tested
    private PollutionStatsTab pollutionStatsTab;

    /**
     * Initializes the PollutionStatsTab object before each test.
     * Sets up a mock DataLoader to simulate data loading.
     */
    @BeforeEach
    public void setUp() {
        // Initialise the PollutionStatsTab with mock data
        pollutionStatsTab = new PollutionStatsTab("Pollution Stats");

        // Set mock data loader 
        pollutionStatsTab.setDataLoader(new MockDataLoader());
    }

    /**
     * Tests the selection of a year (2020) button in the UI.
     * Verifies that the year "2020" is selected after the button is clicked.
     */
    @Test
    public void testSelectYearButton() {
        // Simulate clicking the "2020" button after the JavaFX app is launched
        Platform.runLater(() -> {
            
            HBox hbox = (HBox) pollutionStatsTab.getContent().lookup(".hbox");

            // Directly access the ToggleButton for "2020" 
            ToggleButton button2020 = (ToggleButton) hbox.getChildren().get(2);
            assertNotNull(button2020, "Button for year 2020 should be found");
            button2020.fire(); // Simulate clicking

            // Check that the year "2020" is selected
            Set<String> selectedYears = pollutionStatsTab.getSelectedYears();
            assertTrue(selectedYears.contains("2020"));
        });
    }

    /**
     * Tests the selection of a pollutant (NO2) button in the UI.
     * Verifies that "no2" is the selected pollutant after the button is clicked.
     */
    @Test
    public void testSelectPollutantButton() {
        // Simulate clicking the "NO2" button
        Platform.runLater(() -> {
            
            HBox hbox = (HBox) pollutionStatsTab.getContent().lookup(".hbox");

            // Directly access the ToggleButton for "NO2" 
            ToggleButton no2Button = (ToggleButton) hbox.getChildren().get(0);
            assertNotNull(no2Button, "Button for pollutant NO2 should be found");
            no2Button.fire(); // Simulate clicking

            // Check if the selected pollutant is "no2"
            String selectedPollutant = pollutionStatsTab.getSelectedPollutant();
            assertEquals("no2", selectedPollutant);
        });
    }

    /**
     * Tests the computation of the average pollutant value.
     * Verifies that the computed average for 2020 is correct based on the mock data.
     */
    @Test
    public void testComputeAverageValue() {
        // Simulate the calculation of average values using the mock data
        double avgValue = pollutionStatsTab.computeAverageValue("pm10", "pm10", "2020");

        // Verify that the computed average is correct
        // Based on the mock data, it should be (20.0 + 40.0) / 2 = 30.0
        assertEquals(30.0, avgValue, 0.1);  
    }

    /**
     * Tests the computation of the highest pollutant value.
     * Verifies that the computed highest value for 2020 is correct based on the mock data.
     */
    @Test
    public void testComputeHighestValue() {
        // Simulate the calculation of highest values using the mock data
        double highestValue = pollutionStatsTab.computeHighestValue("pm10", "pm10", "2020");

        // Verify that the computed highest value is correct
        // Based on the mock data, the highest value should be 40.0 (from the second data point)
        assertEquals(40.0, highestValue, 0.1);  
    }
    
    /**
     * Tests the data displayed in the BarChart after selecting a year and pollutant.
     * Verifies that the graph contains correct data points for the selected year and pollutant.
     */
    @Test
    public void testGraphData() {
        // Simulate clicking the "2020" button after the JavaFX app is launched
        Platform.runLater(() -> {
            
            BarChart<String, Number> barChart = pollutionStatsTab.getBarChart();
            assertNotNull(barChart, "BarChart should not be null.");

            // Simulate selecting "2020" as a year
            ToggleButton button2020 = (ToggleButton) pollutionStatsTab.getContent().lookup(".hbox .toggle-button:nth-child(3)");
            assertNotNull(button2020, "Button for year 2020 should be found");
            button2020.fire(); // Simulate clicking "2020"

            // Simulate selecting "NO2" as the pollutant
            ToggleButton no2Button = (ToggleButton) pollutionStatsTab.getContent().lookup(".hbox .toggle-button:nth-child(1)");
            assertNotNull(no2Button, "Button for pollutant NO2 should be found");
            no2Button.fire(); // Simulate clicking "NO2"

            // Trigger graph update after selections
            pollutionStatsTab.updateGraph();

            // Check if the BarChart contains the correct data
            XYChart.Series<String, Number> series = barChart.getData().get(0);
            assertNotNull(series, "The chart series should not be null.");
            assertEquals("no2", series.getName(), "The series name should match the selected pollutant.");

            // Check the number of data points in the series
            assertEquals(1, series.getData().size(), "There should be one data point for the selected year.");

            // Verify the data point's X and Y values (for year 2020)
            XYChart.Data<String, Number> dataPoint = series.getData().get(0);
            assertEquals("2020", dataPoint.getXValue(), "The X value should be '2020'.");
            assertEquals(20.0, dataPoint.getYValue().doubleValue(), 0.1, "The Y value should be 20.0 (based on mock data).");
        });
    }

    // Mock DataLoader class to simulate the loading of data
    private class MockDataLoader extends DataLoader {
        @Override
        public DataSet loadDataFile(String filePath) {
            return createMockDataSet(filePath);
        }
    }

    /**
     * Helper method to create a mock DataSet for testing.
     * This method simulates a dataset containing pollutant values for specific years.
     * 
     * @param filePath the file path used to simulate loading a data file
     * @return a DataSet with mock data
     */
    private DataSet createMockDataSet(String filePath) {
        String pollutant = filePath.contains("pm10") ? "pm10" : "no2";
        DataSet dataSet = new DataSet(pollutant, "2020", "mean", "µg/m³");
        if (pollutant.equals("no2")) {
            dataSet.addData(new String[]{"1", "10", "10", "10.0"});
            dataSet.addData(new String[]{"2", "20", "20", "20.0"});
        } else if (pollutant.equals("pm10")) {
            dataSet.addData(new String[]{"1", "10", "10", "20.0"});
            dataSet.addData(new String[]{"2", "20", "20", "40.0"});
        }
        return dataSet;
    }
}