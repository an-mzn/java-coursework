import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.*;

/**
 * A JavaFX Tab that displays pollution statistics through bar charts and line graphs.
 * Allows users to select pollutants and years to analyze air pollution data.
 * 
 * @author Abeer Hussain
 * @version 1.0
 */
public class PollutionStatsTab extends Tab {
    private DataLoader dataLoader;
    private Set<String> selectedYears = new HashSet<>();
    private String selectedPollutant = null;
    private ToggleButton selectedPollutantButton = null;
    private VBox statsContainer;
    private BarChart<String, Number> barChart;
    private LineChart<String, Number> lineChart;

    /**
     * Creates a new PollutionStatsTab with all the buttons.
     *
     * @param tabName The name of the tab.
     */

    public PollutionStatsTab(String tabName) {
        super(tabName);
        this.setClosable(false);
        dataLoader = new DataLoader();
        statsContainer = new VBox(10);

        // Create year selection buttons
        ToggleButton button2018 = createYearButton("2018");
        ToggleButton button2019 = createYearButton("2019");
        ToggleButton button2020 = createYearButton("2020");
        ToggleButton button2021 = createYearButton("2021");
        ToggleButton button2022 = createYearButton("2022");
        ToggleButton button2023 = createYearButton("2023");

        HBox yearButtons = new HBox(10, button2018, button2019, button2020, button2021, button2022, button2023);

        // Create pollutant selection buttons
        ToggleButton no2Button = createPollutantButton("NO2", "no2");
        ToggleButton pm25Button = createPollutantButton("PM2.5", "pm25");
        ToggleButton pm10Button = createPollutantButton("PM10", "pm10");

        HBox pollutionButtons = new HBox(10, no2Button, pm25Button, pm10Button);

        // Create action buttons
        Button showAverageButton = new Button("Show Average Pollutant Levels");
        Button showHighestButton = new Button("Show Highest Pollution");
        Button showLineGraphButton = new Button("Show Line Graph Prediction");

        //Assign the style classes for all the action buttons
        showAverageButton.getStyleClass().add("button");
        showHighestButton.getStyleClass().add("button");
        showLineGraphButton.getStyleClass().add("button");

        showAverageButton.setOnAction(e -> showMeanOfAverages());
        showHighestButton.setOnAction(e -> showHighestPollutionStats());
        showLineGraphButton.setOnAction(e -> showLineChartPopup());

        HBox buttonContainer = new HBox(10, showAverageButton, showHighestButton, showLineGraphButton);
        buttonContainer.setPadding(new Insets(10));

        // Setup bar chart
        CategoryAxis barXAxis = new CategoryAxis();
        barXAxis.setLabel("Years");
        NumberAxis barYAxis = new NumberAxis();
        barYAxis.setLabel("Average Pollutant Level");

        barChart = new BarChart<>(barXAxis, barYAxis);
        barChart.setTitle("Average Pollution Levels by Year");

        VBox contentBox = new VBox(15, yearButtons, pollutionButtons, buttonContainer, barChart, statsContainer);
        contentBox.setPadding(new Insets(10));

        this.setContent(contentBox);
    }

    /**
     * Creates a toggle button for selecting a year.
     *
     * @param year The year to represent.
     * @return A ToggleButton for selecting the year.
     */
    private ToggleButton createYearButton(String year) {
        ToggleButton button = new ToggleButton(year);
        //Assign style to the button and remove in line style
        button.getStyleClass().add("toggle-button");
        button.setOnAction(e -> {
                    if (selectedYears.contains(year)) {
                        selectedYears.remove(year);
                    } else {
                        selectedYears.add(year);
                    }
                    updateGraph();
            });
        return button;
    }

    /**
     * Creates a toggle button for selecting a pollutant.
     *
     * @param displayName The display name of the pollutant.
     * @param pollutant   The  pollutant identifier.
     * @return A ToggleButton for selecting the pollutant.
     */
    private ToggleButton createPollutantButton(String displayName, String pollutant) {
        ToggleButton button = new ToggleButton(displayName);
        button.setOnAction(e -> {
                    if (selectedPollutant != null && selectedPollutant.equals(pollutant)) {
                        selectedPollutant = null;
                        button.setStyle("");
                        selectedPollutantButton = null;
                    } else {
                        if (selectedPollutantButton != null) {
                            selectedPollutantButton.setStyle("");
                        }
                        selectedPollutant = pollutant;
                        button.setStyle("-fx-background-color: lightgreen;");
                        selectedPollutantButton = button;
                    }
                    updateGraph();
            });
        return button;
    }

    /**
     * Updates the bar chart based on selected years and pollutant.
     * The bar chart is populated with the pollutant levels for each selected year,
     * sorted chronologically.
     */
    public void updateGraph() {
        barChart.getData().clear();

        if (selectedYears.isEmpty() || selectedPollutant == null) {
            return;
        }

        String directory = switch (selectedPollutant) {
                case "no2" -> "no2";
                case "pm10" -> "pm10";
                case "pm25" -> "pm2.5";
                default -> "";
            };

        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();
        barSeries.setName(selectedPollutant.toUpperCase());

        List<String> sortedYears = new ArrayList<>(selectedYears);
        sortedYears.sort(Comparator.comparingInt(Integer::parseInt));

        for (String year : sortedYears) {
            double avgValue = computeAverageValue(directory, selectedPollutant, year);
            if (avgValue >= 0) {
                barSeries.getData().add(new XYChart.Data<>(year, avgValue));
            }
        }

        barChart.getData().add(barSeries);
    }

    /**
     * Displays a line chart in a separate popup window.
     * and predicting future levels from 2024 to 2028 using
     * linear regression.
     */
    private void showLineChartPopup() {
        if (selectedPollutant == null) {
            statsContainer.getChildren().add(new Label("Select a pollutant to view the line graph."));
            return;
        }

        Stage lineChartStage = new Stage();
        lineChartStage.setTitle("Pollution Trends with Prediction");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Years");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Pollutant Level");

        LineChart<String, Number> popupLineChart = new LineChart<>(xAxis, yAxis);
        popupLineChart.setTitle(selectedPollutant.toUpperCase() + " Trend");

        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        lineSeries.setName(selectedPollutant.toUpperCase() + " (2018-2028)");

        String directory = switch (selectedPollutant) {
                case "no2" -> "no2";
                case "pm10" -> "pm10";
                case "pm25" -> "pm2.5";
                default -> "";
            };

        // Collect actual data points (2018-2023)
        List<Integer> years = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (int year = 2018; year <= 2023; year++) {
            double avgValue = computeAverageValue(directory, selectedPollutant, String.valueOf(year));
            if (avgValue >= 0) {
                years.add(year);
                values.add(avgValue);
                lineSeries.getData().add(new XYChart.Data<>(String.valueOf(year), avgValue));
            }
        }

        // Perform linear regression if we have at least two years of data
        if (years.size() > 1) {
            double[] regressionParams = computeLinearRegression(years, values);
            double m = regressionParams[0];  // Slope
            double b = regressionParams[1];  // Intercept

            // Predict values for 2024-2028
            for (int year = 2024; year <= 2028; year++) {
                double predictedValue = m * year + b;
                lineSeries.getData().add(new XYChart.Data<>(String.valueOf(year), predictedValue));
            }
        }

        popupLineChart.getData().add(lineSeries);

        VBox layout = new VBox(10, popupLineChart);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        lineChartStage.setScene(scene);
        lineChartStage.show();
    }

    /**
     * Computes the gradient (m) and intercept (b) for linear regression.
     *
     * @param years  List of years (x values).
     * @param values Corresponding pollutant levels (y values).
     * @return A double array where index 0 is m and index 1 is b.
     */
    private double[] computeLinearRegression(List<Integer> years, List<Double> values) {
        int n = years.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += years.get(i);
            sumY += values.get(i);
            sumXY += years.get(i) * values.get(i);
            sumX2 += years.get(i) * years.get(i);
        }

        double m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX); // Slope
        double b = (sumY - m * sumX) / n; // Intercept

        return new double[]{m, b};
    }

    /**
     * Computes the average pollution level for a given year.
     *
     * @param pollutant The pollutant to analyze.
     * @param year      The year to analyze.
     * @return The average pollutant level, or -1 if data is unavailable.
     */
    public double computeAverageValue(String directory, String pollutant, String year) {
        DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv");
        if (dataSet == null) return -1;

        double sum = 0;
        int count = 0;

        for (DataPoint point : dataSet.getData()) {
            sum += point.value();
            count++;
        }

        return count == 0 ? 0 : sum / count;
    }

    /**
     * Computes the highest pollution level for a given pollutant in a specific year.
     *
     * @param directory The directory where the pollutant data is stored.
     * @param pollutant The pollutant type (e.g., "no2", "pm25", "pm10").
     * @param year      The year for which the highest pollution value is to be computed.
     * @return The highest recorded pollutant level for the given year, or -1 if data is unavailable.
     */
    public double computeHighestValue(String directory, String pollutant, String year) {
        DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv");
        // If the dataset is not found, return -1 to indicate missing data
        if (dataSet == null) return -1;

        double highestPollution = -1;
        double highestX = 0, highestY = 0;
        for (DataPoint point : dataSet.getData()) {
            if (point.value() > highestPollution) {
                highestPollution = point.value();
                highestX = point.x();
                highestY = point.y();
            }
        }
        return highestPollution;
    }

    /**
     * Displays statistics for the highest pollution levels.
     */
    private void showMeanOfAverages() {
        statsContainer.getChildren().clear();

        if (selectedYears.isEmpty() || selectedPollutant == null) {
            statsContainer.getChildren().add(new Label("Select years and one pollutant to view stats."));
            return;
        }

        String directory = switch (selectedPollutant) {
                case "no2" -> "no2";
                case "pm10" -> "pm10";
                case "pm25" -> "pm2.5";
                default -> "";
            };

        double totalSum = 0;
        int yearCount = 0;

        for (String year : selectedYears) {
            double avgValue = computeAverageValue(directory, selectedPollutant, year);
            if (avgValue >= 0) {
                totalSum += avgValue;
                yearCount++;
            }
        }

        if (yearCount > 0) {
            double meanOfAverages = totalSum / yearCount;
            statsContainer.getChildren().add(new Label("Mean of Yearly Averages: " + meanOfAverages));
        } else {
            statsContainer.getChildren().add(new Label("No valid data found."));
        }
    }

    /**
     * Displays statistics for the highest pollution levels.
     */
    private void showHighestPollutionStats() {
        statsContainer.getChildren().clear();

        if (selectedYears.isEmpty() || selectedPollutant == null) {
            statsContainer.getChildren().add(new Label("Select years and one pollutant to view stats."));
            return;
        }

        String directory = switch (selectedPollutant) {
                case "no2" -> "no2";
                case "pm10" -> "pm10";
                case "pm25" -> "pm2.5";
                default -> "";
            };

        double highestPollution = -1;
        String highestYear = "";
        double highestX = 0, highestY = 0;

        for (String year : selectedYears) {
            DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + selectedPollutant + year + ".csv");
            if (dataSet == null) continue;

            for (DataPoint point : dataSet.getData()) {
                if (point.value() > highestPollution) {
                    highestPollution = point.value();
                    highestYear = year;
                    highestX = point.x();
                    highestY = point.y();
                }
            }
        }

        if (highestPollution >= 0) {
            statsContainer.getChildren().add(new Label("Highest Pollution: " + highestPollution + " in year " + highestYear));
            statsContainer.getChildren().add(new Label("Coordinates: (" + highestX + ", " + highestY + ")"));
        } else {
            statsContainer.getChildren().add(new Label("No valid data found."));
        }
    }

    /**
     * Sets a custom DataLoader useful for testing
     */

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * @return The set of selected years.
     */
    public Set<String> getSelectedYears() {
        return selectedYears;
    }

    /**
     * @return The selected pollutant.
     */
    public String getSelectedPollutant() {
        return selectedPollutant;
    }

    /**
     * @return The created bar chart.
     */
    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}