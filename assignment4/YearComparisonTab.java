import javafx.scene.control.Tab;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.geometry.Side;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;

/**
 * The `YearComparisonTab` class provides a user interface for comparing
 * average pollution levels of a selected pollutant between two different years.
 * It allows users to select a pollutant and two years from dropdown menus,
 * then calculates and displays a comparison message and a line chart
 * indicating which year had a higher average pollution level. It now includes
 * two pie charts showing the proportion of all selected pollution values.
 *
 * @author Ayan Mamun
 * @version 1.0
 */
public class YearComparisonTab extends Tab {

    private ComboBox<String> pollutantComboBox;
    private ComboBox<String> year1ComboBox;
    private ComboBox<String> year2ComboBox;
    private Label resultLabel;
    private DataLoader dataLoader;
    private final String[] years = {"2018", "2019", "2020", "2021", "2022", "2023"};
    private final String[] pollutants = {"no2", "pm25", "pm10"};
    private LineChart<String, Number> lineChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> series1;
    private XYChart.Series<String, Number> series2;
    private VBox layout;
    private PieChart pieChart1; // Pie chart for year 1
    private PieChart pieChart2; // Pie chart for year 2

    /**
     * Constructor for the `YearComparisonTab` class.
     * @param tabName The name to be displayed on the tab.
     */
    public YearComparisonTab(String tabName) {
        super(tabName);
        this.setClosable(false);
        dataLoader = new DataLoader();

        // UI Elements
        Label pollutantLabel = new Label("Select Pollutant:");
        pollutantComboBox = new ComboBox<>();
        pollutantComboBox.getItems().addAll(pollutants);

        Label year1Label = new Label("Select Year 1:");
        year1ComboBox = new ComboBox<>();
        year1ComboBox.getItems().addAll(years);

        Label year2Label = new Label("Select Year 2:");
        year2ComboBox = new ComboBox<>();
        year2ComboBox.getItems().addAll(years);

        // HBox for Pollutant and Year Selection
        HBox selectionBox = new HBox(10);
        selectionBox.getChildren().addAll(pollutantLabel, pollutantComboBox,
                                            year1Label, year1ComboBox,
                                            year2Label, year2ComboBox);
        selectionBox.setAlignment(Pos.CENTER);

        //Compare Label Title
        Label compareLabel = new Label("Compare Averages");
        compareLabel.setFont(new Font("Arial", 20));

        resultLabel = new Label("");
        resultLabel.setFont(new Font("Arial", 16));

        // Create chart - Initial Chart Creation
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        yAxis.setUpperBound(10);  //Force the scale to be less than 10
        yAxis.setTickUnit(1);
        yAxis.setAutoRanging(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Pollution Level Comparison");
        lineChart.setAnimated(false); // Disable animation
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(true);

        series1 = new XYChart.Series<>();
        series2 = new XYChart.Series<>();
        series1.setName("Year 1");
        series2.setName("Year 2");
        lineChart.getData().addAll(series1, series2);

        // Create pie charts
        pieChart1 = new PieChart();
        pieChart1.setTitle("Pollutant Distribution - Year 1");
      //Remove all keys to display data
        pieChart1.setLegendSide(Side.RIGHT);
        pieChart1.setLabelsVisible(true);

        pieChart2 = new PieChart();
        pieChart2.setTitle("Pollutant Distribution - Year 2");
        //Remove all keys to display data
        pieChart2.setLegendSide(Side.LEFT);
        pieChart2.setLabelsVisible(true);

        // Layout
        HBox pieChartContainer = new HBox(10, pieChart1, pieChart2);
        pieChartContainer.setAlignment(Pos.CENTER);

        layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                compareLabel,
                selectionBox, // Replaced individual labels and combo boxes with HBox
                resultLabel,
                lineChart,
                pieChartContainer // Horizontal container for pie charts
        );

        VBox.setVgrow(lineChart, Priority.ALWAYS); // Allow chart to grow vertically

        // Event Listeners for Combo Boxes to Trigger Update
        pollutantComboBox.setOnAction(e -> compareAverages());
        year1ComboBox.setOnAction(e -> compareAverages());
        year2ComboBox.setOnAction(e -> compareAverages());

        //Initial Compare Averages to show before clicking an option.
        compareAverages();

        this.setContent(layout);
    }

    /**
     * Compares the average pollution levels for the selected pollutant and years
     * and updates the result label and the line chart.
     */
    private void compareAverages() {
        String pollutant = pollutantComboBox.getValue();
        String year1 = year1ComboBox.getValue();
        String year2 = year2ComboBox.getValue();

        if (pollutant == null || year1 == null || year2 == null) {
            resultLabel.setText("Please select a pollutant and two years.");
            clearChart(); // Clear the chart when no data is selected
            clearPieCharts();
            return;
        }

        double avg1 = computeAverage(pollutant, year1);
        double avg2 = computeAverage(pollutant, year2);

        if (avg1 == -1 || avg2 == -1) {
            resultLabel.setText("Could not retrieve data for the selected years and pollutant.");
             clearChart(); // Clear the chart when data retrieval fails
             clearPieCharts();
            return;
        }

        // Comparison logic
        if (avg1 > avg2) {
            double multiplier = avg1 / avg2;
            resultLabel.setText(year1 + " had a higher average " + pollutant.toUpperCase() + " level by " + String.format("%.2f", multiplier) + " times compared to " + year2 + ".");
        } else if (avg2 > avg1) {
            double multiplier = avg2 / avg1;
            resultLabel.setText(year2 + " had a higher average " + pollutant.toUpperCase() + " level by " + String.format("%.2f", multiplier) + " times compared to " + year1 + ".");
        } else {
            resultLabel.setText("The average " + pollutant.toUpperCase() + " levels were the same in " + year1 + " and " + year2 + ".");
        }

        // Update the chart
        updateChart(year1, avg1, year2, avg2);
        updatePieCharts(year1, year2);
    }

    //Helper Method to clear the chart when no data is selected
     private void clearChart() {
         Platform.runLater(() -> {
             lineChart.getData().clear();
         });
     }

     private void clearPieCharts() {
          Platform.runLater(() -> {
                pieChart1.setData(null);
                pieChart2.setData(null);
         });
     }

    /**
     * Computes the average pollution level for a given pollutant and year.
     * @param pollutant The pollutant to compute the average for (e.g., "no2", "pm25").
     * @param year The year to compute the average for (e.g., "2018").
     * @return The average pollution level, or -1 if an error occurs or data is not found.
     */
    private double computeAverage(String pollutant, String year) {
        String directory;
        switch (pollutant) {
            case "no2":
                directory = "no2";
                break;
            case "pm10":
                directory = "pm10";
                break;
            case "pm25":
                directory = "pm2.5";
                break;
            default:
                return -1; // Invalid pollutant
        }

        DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv");

        if (dataSet == null) {
            return -1; // Data not found
        }

        List<DataPoint> dataPoints = dataSet.getData();
        if (dataPoints.isEmpty()) {
            return -1; // No data points found
        }

        double sum = 0;
        for (DataPoint point : dataPoints) {
            sum += point.value();
        }

        return sum / dataPoints.size();
    }

    /**
     * Displays an alert with the given title and message.
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

     /**
     * Updates the line chart by redrawing it.
     * @param year1 The first year.
     * @param avg1 The average pollution level for the first year.
     * @param year2 The second year.
     * @param avg2 The average pollution level for the second year.
     */
    private void updateChart(String year1, double avg1, String year2, double avg2) {
         Platform.runLater(() -> {
             // 1. Clear existing data
             lineChart.getData().clear();

             // 2. Set Y-Axis Upper Bound
             yAxis.setUpperBound(10);  // Set fixed upper bound
             yAxis.setTickUnit(1);
             yAxis.setAutoRanging(false);

             // 3. Create new series with the new data
             series1 = new XYChart.Series<>();
             series2 = new XYChart.Series<>();
             series1.setName("Year 1");
             series2.setName("Year 2");

             // 4. Add data points and labels
             XYChart.Data<String, Number> data1 = new XYChart.Data<>(year1, avg1);
             XYChart.Data<String, Number> data2 = new XYChart.Data<>(year2, avg2);

             data1.setNode(createLabel(avg1, "right"));
             data2.setNode(createLabel(avg2, "left"));

             series1.getData().add(data1);
             series2.getData().add(data2);

             lineChart.getData().addAll(series1, series2);
         });
     }

    // Helper method to create labels
    private Region createLabel(double value, String side) {
        Label label = new Label(String.format("%.2f", value));
        label.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        StackPane region = new StackPane(label);

        if ("right".equals(side)) {
            StackPane.setAlignment(label, Pos.CENTER_LEFT);
            region.setMinWidth(50); // Adjust as needed
        } else {
            StackPane.setAlignment(label, Pos.CENTER_RIGHT);
            region.setMinWidth(50); // Adjust as needed
        }

        return region;
    }

    /**
     * Updates the pie charts with pollutant distribution data for the selected years.
     * @param year1 The first year selected for comparison.
     * @param year2 The second year selected for comparison.
     */
    private void updatePieCharts(String year1, String year2) {
        Platform.runLater(() -> {
            // Prepare data for Pie Chart 1 (Year 1)
            ObservableList<PieChart.Data> pieChartData1 = FXCollections.observableArrayList();

            //Ensure the data isn't lost from a memory issue
            String stringLabel1 = String.valueOf(year1);
            //This calls that there is a chart that needs to be displayed.
            pieChart1.setTitle("Pollutant Distribution in " + stringLabel1);

            //Prepare new data in the chart
            double totalPollution1 = 0;
             //Create array as local class property to be used
            List<String> dataTo = new ArrayList<String>();
            for (String pollutant : pollutants) {
                double avgPollution = computeAverage(pollutant, year1);
                //Ensure average of code is not null
                if(avgPollution >-1){
                   totalPollution1+=avgPollution;
                }
            }

            //For each pollutant create its slice
            for (String pollutant : pollutants) {
                double avgPollution = computeAverage(pollutant, year1);
                //If average pollution code could not compile the change it to code 0 so the app doesnt crash.
                if(avgPollution <= -1){
                    avgPollution = 0;
                }
                double percentage1 = (totalPollution1 > 0) ? (avgPollution / totalPollution1) * 100 : 0; // Prevent division by zero
                pieChartData1.add(new PieChart.Data(pollutant.toUpperCase() + " " + String.format("%.2f%%", percentage1), avgPollution));
            }
            pieChart1.setData(pieChartData1);

            // Prepare data for Pie Chart 2 (Year 2)
            ObservableList<PieChart.Data> pieChartData2 = FXCollections.observableArrayList();
            pieChart2.setTitle("Pollutant Distribution in " + year2);
            double totalPollution2 = 0;
            for (String pollutant : pollutants) {
                double avgPollution = computeAverage(pollutant, year2);
                //Ensure average of code is not null
                if(avgPollution >-1){
                    totalPollution2+=avgPollution;
                }
            }
                // For each pollutant create its slice
            for (String pollutant : pollutants) {
                double avgPollution = computeAverage(pollutant, year2);
                //If average pollution code could not compile the change it to code 0 so the app doesnt crash.
                if(avgPollution <= -1){
                    avgPollution = 0;
                }
                double percentage2 = (totalPollution2 > 0) ? (avgPollution / totalPollution2) * 100 : 0;  // Prevent division by zero
                PieChart.Data slice = new PieChart.Data(pollutant.toUpperCase() + " " + String.format("%.2f%%", percentage2), avgPollution);
                 pieChartData2.add(slice);
            }
             // Set the piechart property for the correct data
            pieChart2.setData(pieChartData2);

            // force layout
            pieChart1.applyCss();
            pieChart1.layout();
            pieChart2.applyCss();
            pieChart2.layout();
             pieChart1.setLegendVisible(false);
             pieChart2.setLegendVisible(false);
        });
    }
}