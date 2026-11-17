import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * The 'MapTab' class creates the tab which displays the map with air pollution data points. 
 * Handles loading, filtering and visualizing pollution data based on the users input.
 * 
 * @author Raihan Islam
 * @version 1.0
 */
public class MapTab extends Tab {
    private Canvas canvas;
    private Image image;
    private int xMax = 553297; // highest x coordinate of map
    private int xMin = 510394; // lowest x coordinate of map
    private int yMax = 193305; // highest y coordinate of map
    private int yMin = 168504; // lowest y coordinate of map 
    private double xScale; // x-scale for mapping the data points coordinates onto the images pixels
    private double yScale; // y-scale for mapping the data points coordinates onto the images pixels
    private DataLoader dataLoader;
    private GraphicsContext gc;
    private String selectedYear; // current year selected by user
    private String selectedPollutant; // current pollutant selected by user
    private String selectedDirectory; // current directory of pollutant
     private Label pollutantInfo; // label to display the selected pollutant
    private Label yearInfo; // label to display the selected year
    private PollutantColour currentPollutantColour; // current colour shown on map depending on pollutant
    private HBox key; // key to display information
    private PollutantFilter pollutantFilter;
    private String filterType; // current filter 
    
    /**
     * Constructor for the map tab
     * @param tabName the name of the tab
     */
    public MapTab(String tabName) 
    {
        super(tabName);
        System.out.println("MapTab created");
        this.setClosable(false); // makes it so you cant close the tab
        dataLoader = new DataLoader();
        image = new Image("London.png"); // image of map
        canvas = new Canvas(image.getWidth(), image.getHeight());
        pollutantFilter = new PollutantFilter();
        filterType = "All";
        xScale = (xMax - xMin) / image.getWidth(); // x-scale to map data points onto map
        yScale = (yMax - yMin) / image.getHeight(); // y-scale to map data points onto map
        
    
        gc = canvas.getGraphicsContext2D();
        gc.drawImage(image, 0, 0);
        canvas.setOnMouseClicked(this::displayDataInfo); // displays data info on mouse click
        
    
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(canvas);
    
        
        pollutantInfo = new Label("Pollutant: ");
        yearInfo = new Label("Year: ");
        
        
        VBox dataInfo = new VBox(pollutantInfo, yearInfo);
    
        VBox root = new VBox(10); // 10 pixel space between contents of root
        makeMenuBar(root);
        
        key = new HBox(10);
        key.setPadding(new Insets(5));
        
        root.getChildren().addAll(scroll, dataInfo, key);
        
    
        root.setPadding(new Insets(5)); // 5 pixel space between root border and contents
        this.setContent(root);
    }
    
    /**
     * method to draw data points from csv file onto the map
     * @param gc the graphics context of the canvas in order to draw on the map
     * @param directory the directory where the file is stored
     * @param pollutant the pollutant type
     * @param year the year of the file
     */
    private void displayDataPoints(GraphicsContext gc, String directory, String pollutant, String year) 
    {
        DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv"); // loads appropiate data file
        if (dataSet != null) {
            for (DataPoint point : dataSet.getData()) {
                double value = point.value(); // value of data point
                
                
        
                if (pollutantFilter.shouldDrawDataPoint(value, currentPollutantColour)) {
                    double x = (point.x() - xMin) / xScale; // x pixel
                    double y = (yMax - point.y()) / yScale; // y pixel
                    currentPollutantColour.drawDataPoint(gc, x, y, value); // draw data point onto map, colour is determined by currentPollutantColour
                }
            }
        }
    }
    
    /**
     * displays information of a data point in an alert box
     * @param event receives a mouse event
     */
    private void displayDataInfo(MouseEvent event) {
        double mouseX = event.getX(); // x position of mouse click
        double mouseY = event.getY(); // y position of mouse click
    
        
    
        DataPoint dataPoint = getDataPoint(mouseX, mouseY, selectedDirectory, selectedPollutant, selectedYear);
        
        //display information in alert box
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Data Value");
        alert.setHeaderText("Pollutant Level");
        alert.setContentText("Pollutant: " + selectedPollutant.toUpperCase() + 
                             "\nValue: " + dataPoint.value() +
                             "\nX-Coord: " + dataPoint.x() +
                             "\nY-Coord: " + dataPoint.y() +
                             "\nGridcode: " + dataPoint.gridCode());
        alert.showAndWait();  
    }
    
    /**
     * gets the data point of where the user clicked
     * @param mousePosX the x position of when the mouse was clicked
     * @param mousePosY the y position of when the mouse was clicked
     * @param directory the directory where the file is stored
     * @param pollutant the pollutant type
     * @param year the year of the file
     */
    private DataPoint getDataPoint(double mousePosX, double mousePosY,String directory, String pollutant, String year) {
        DataPoint dataPoint = null;
        DataSet dataSet = dataLoader.loadDataFile("UKAirPollutionData/" + directory + "/map" + pollutant + year + ".csv");
        if (dataSet == null) {
            return dataPoint;
        }
    
    
        for (DataPoint point : dataSet.getData()) {
            double x = (point.x() - xMin) / xScale;
            double y = (yMax - point.y()) / yScale;
            
            //check if mouse click is within 43 pixels of where the data point is drawn from
            // each data point is represetnted by a 43x43 pixel box
            if(mousePosX <= x + 43 && mousePosY <= y + 43)
            {
                dataPoint = point;
                break;
            }
        }
        return dataPoint;
    }
    
    /**
     * creates the menu bar for the tab to select the pollutant and year and filters
     * @param parent the pane the menubar will be added to
     */
    private void makeMenuBar(Pane parent)
    {
        MenuBar menubar = new MenuBar();
        parent.getChildren().add(menubar);
        
        
        Menu pollutantMenu = new Menu("Pollutant");
        ToggleGroup pollutantGroup = new ToggleGroup(); // ensures only one pollutant can be selected
        
        RadioMenuItem no2Item = new RadioMenuItem("NO2");
        no2Item.setToggleGroup(pollutantGroup);
        no2Item.setOnAction(e -> updateData("NO2", "no2", selectedYear));
        
        RadioMenuItem pm25Item = new RadioMenuItem("PM2.5");
        pm25Item.setToggleGroup(pollutantGroup);
        pm25Item.setOnAction(e -> updateData("pm2.5", "pm25", selectedYear));
        
        RadioMenuItem pm10Item = new RadioMenuItem("PM10");
        pm10Item.setToggleGroup(pollutantGroup);
        pm10Item.setOnAction(e -> updateData("pm10", "pm10", selectedYear));
        
        pollutantMenu.getItems().addAll(no2Item, pm25Item, pm10Item);
        
        
        Menu yearMenu = new Menu("Year");
        ToggleGroup yearGroup = new ToggleGroup(); // ensures only one year can be selected
        
        RadioMenuItem year2018Item = new RadioMenuItem("2018");
        year2018Item.setToggleGroup(yearGroup);
        year2018Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2018"));
        
        RadioMenuItem year2019Item = new RadioMenuItem("2019");
        year2019Item.setToggleGroup(yearGroup);
        year2019Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2019"));
    
        RadioMenuItem year2020Item = new RadioMenuItem("2020");
        year2020Item.setToggleGroup(yearGroup);
        year2020Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2020"));
    
        RadioMenuItem year2021Item = new RadioMenuItem("2021");
        year2021Item.setToggleGroup(yearGroup);
        year2021Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2021"));
    
        RadioMenuItem year2022Item = new RadioMenuItem("2022");
        year2022Item.setToggleGroup(yearGroup);
        year2022Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2022"));
    
        RadioMenuItem year2023Item = new RadioMenuItem("2023");
        year2023Item.setToggleGroup(yearGroup);
        year2023Item.setOnAction(e -> updateData(selectedDirectory, selectedPollutant, "2023"));
        
        yearMenu.getItems().addAll(year2018Item, year2019Item, year2020Item, year2021Item, year2022Item, year2023Item);
        
        
        Menu filterMenu = new Menu ("Filters");
        ToggleGroup filterGroup = new ToggleGroup(); // ensures only one filter can be chosen
        
        
        RadioMenuItem showAllItem = new RadioMenuItem("Show All");
        showAllItem.setToggleGroup(filterGroup);
        showAllItem.setOnAction(e -> {
            pollutantFilter.setFilterType("All");
            updateData(selectedDirectory, selectedPollutant, selectedYear);
        });
        
        RadioMenuItem showHighItem = new RadioMenuItem("High values");
        showHighItem.setToggleGroup(filterGroup);
        showHighItem.setOnAction(e -> {
            pollutantFilter.setFilterType("High");
            updateData(selectedDirectory, selectedPollutant, selectedYear);
        });
        
        RadioMenuItem showMidItem = new RadioMenuItem("Mid values");
        showMidItem.setToggleGroup(filterGroup);
        showMidItem.setOnAction(e -> {
            pollutantFilter.setFilterType("Mid");
            updateData(selectedDirectory, selectedPollutant, selectedYear);
        });
        
        RadioMenuItem showLowItem = new RadioMenuItem("Low values");
        showLowItem.setToggleGroup(filterGroup);
        showLowItem.setOnAction(e -> {
            pollutantFilter.setFilterType("Low");
            updateData(selectedDirectory, selectedPollutant, selectedYear);
        });
        
        filterMenu.getItems().addAll(showAllItem, showHighItem, showMidItem, showLowItem);
        
        menubar.getMenus().addAll(pollutantMenu, yearMenu, filterMenu);
    }
    
    
    /**
     * updates the data displayed whenever the pollutant or year changes
     * @param directory the directory where the file is stored
     * @param pollutant the pollutant type
     * @param year the year of the file
     */
    private void updateData(String directory, String pollutant ,String year) {
        selectedDirectory = directory;
        selectedPollutant = pollutant;
        selectedYear = year;
        
        // check what colour to use depending on pollutant
        switch (pollutant) {
            case "no2": 
                currentPollutantColour = new NO2Colour(); 
                break;
            case "pm25": 
                currentPollutantColour = new PM25Colour(); 
                break;
            case "pm10": 
                currentPollutantColour = new PM10Colour(); 
                break;
        }
        
        // only displays information when both year and pollutant are selected
        if(selectedYear != null && selectedPollutant != null)
        {
            pollutantInfo.setText("Pollutant: " + selectedPollutant.toUpperCase());
            yearInfo.setText("Year: " + selectedYear);
        }
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // gets rid of all drawings made
        gc.drawImage(image, 0, 0); // places original image back on canvas
        displayDataPoints(gc,directory, pollutant, year); // re-draw data points
        updateKey();
    }
    
    /**
     * adds an element to the key
     * @param key the key the elemen is added to
     * @param color the color of the rectangle to represent the key item
     * @param labelText the text to define what the color shows
     */
    private void addKeyItem(HBox key, Color color, String labelText) 
    {
        Rectangle colourKey= new Rectangle(20, 20, color);
        Label keyLabel = new Label(labelText);
        HBox keyItem = new HBox(5, colourKey, keyLabel); 
        key.getChildren().add(keyItem);
    }   
    
    
    /**
     * updates the key to show the correct color and values depending on what the current pollutant is
     */
    private void updateKey()
    {
        //reset the key
        key.getChildren().clear();
        
        // add elements back to key depending on what the current pollutant is
        if (currentPollutantColour != null)
        {
            addKeyItem(key, currentPollutantColour.getColor(currentPollutantColour.getLowValue()), "Low (<=" + currentPollutantColour.getLowValue() + ")");
            addKeyItem(key, currentPollutantColour.getColor(currentPollutantColour.getMidValue()), "Moderate(" + currentPollutantColour.getLowValue() + "-" + currentPollutantColour.getMidValue() + ")");
            addKeyItem(key, currentPollutantColour.getColor(currentPollutantColour.getMidValue()+1), "High(" + currentPollutantColour.getMidValue() + "<)");
        }
    }
}