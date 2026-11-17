import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

/**
 * The main application class for the London Air Pollution Viewer.
 * It is responsible for creating the UI and loading data.
 *
 * @author Ayan Mamun, Raihan Islam, Abeer Hussain, Karim He
 * @version 1.0
 */
public class MapViewer extends Application {
    private DataLoader dataLoader;
    private Map<String, DataSet> pollutionData; // Stores preloaded data
    private Scene scene; // Declare the scene variable as a class member

    /**
     * The main entry point for the JavaFX application.
     *
     * @param stage The primary stage for this application, onto which the scene and UI components will be set.
     */
    @Override
    public void start(Stage stage) {
        dataLoader = new DataLoader();
        pollutionData = new HashMap<>();

        preloadAllPollutionData(); // Preload all pollution data

        // Create a TabPane
        TabPane tabPane = new TabPane();
        TabStyler.applyHeaderStyle(tabPane); // Apply header style to TabPane

        // Main Layout
        StackPane mainStackPane = new StackPane();
        mainStackPane.setPadding(new Insets(10));
        mainStackPane.getChildren().add(tabPane);

        // Scene and Stage Setup
        scene = new Scene(mainStackPane, 675, 675);
        WelcomeTab welcomeTab = createWelcomeTab(tabPane);

        URL cssURL = getClass().getResource("styles.css");  // styles.css for the button
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.out.println("CSS file not found: styles.css");  // Debug message
        }

        createStatsTab(tabPane); // Create stats tab
        createComparisonTab(tabPane); // Create year comparison tab

        stage.setTitle("London Air Pollution Viewer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the welcome tab and adds it to the TabPane.
     *
     * @param parent The TabPane to which the welcome tab is added.
     */
    private WelcomeTab createWelcomeTab(TabPane parent) {
        WelcomeTab welcomeTab = new WelcomeTab(parent);
        parent.getTabs().add(welcomeTab);
        return welcomeTab; //Return welcome tab
    }

    /**
     * Creates the pollution stats tab and adds it to the TabPane.
     *
     * @param parent The TabPane to which the stats tab is added.
     */
    private void createStatsTab(TabPane parent) {
        Tab statsTab = new PollutionStatsTab("Pollution Stats");
        parent.getTabs().add(statsTab);
    }

    /**
     * Creates the year comparison tab and adds it to the TabPane.
     *
     * @param parent The TabPane to which the comparison tab is added.
     */
    private void createComparisonTab(TabPane parent) {
        Tab comparisonTab = new YearComparisonTab("Year Comparison");
        parent.getTabs().add(comparisonTab);
    }

    /**
     * Preloads all pollution data for different pollutants and years to improve performance.
     */
    private void preloadAllPollutionData() {
        String[] pollutants = {"no2", "pm10", "pm2.5"};
        String[] years = {"2018", "2019", "2020", "2021", "2022", "2023"};

        for (String pollutant : pollutants) {
            String directory = switch (pollutant) {
                case "no2" -> "no2";
                case "pm10" -> "pm10";
                case "pm2.5" -> "pm2.5";
                default -> "";
            };

            for (String year : years) {
                String filePath = "UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv";
                DataSet dataSet = dataLoader.loadDataFile(filePath);

                if (dataSet != null) {
                    pollutionData.put(pollutant + year, dataSet);
                }
            }
        }
    }

    /**
     * Retrieves preloaded pollution data for a specific pollutant and year.
     *
     * @param pollutant The pollutant type (e.g., "no2", "pm10", "pm2.5").
     * @param year The year for which to retrieve the data.
     * @return The DataSet containing the pollution data, or null if the data is not found.
     */
    public DataSet getPreloadedData(String pollutant, String year) {
        return pollutionData.get(pollutant + year);
    }
}