import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents the welcome tab for the application, displaying a welcome message and instructions.
 * @author Karim He 
 *
 * @version 2.0
 */
public class WelcomeTab extends Tab {
    /**
     * Constructor for the WelcomeTab.
     *
     * @param tabPane The TabPane to which this tab will be added.
     */
    public WelcomeTab(TabPane tabPane) {
        super("Welcome");
        this.setClosable(false);

        // Welcome image
        Image image = new Image("welcome_image.jpg");
        ImageView welcomeImage = new ImageView(image);
        welcomeImage.setPreserveRatio(false); // Allows image to stretch and fill the tab
        welcomeImage.setSmooth(true);

        // Instructions
        Text part1 = new Text("Welcome to the\nLondon Air Pollution Viewer!\n");
        part1.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;"); // Bold and slightly bigger
        Text part2 = new Text("\nUse the 'Go to Map' button to navigate to the map view.\n"
                + "In the map view, pollution data will be displayed based on your selection.\n"
                + "Use the pollution stats tab for a graphical representation of the data.\n"
                + "Selecting multiple years allows you to compare their values.\n");
        part2.setStyle("-fx-font-size: 16px;"); // Style for the rest of the instructions

        part1.setFill(Color.WHITE);
        part2.setFill(Color.WHITE);

        TextFlow instructions = new TextFlow(part1, part2);
        instructions.setMaxWidth(500); // Sets a maximum width for text wrapping
        instructions.setTextAlignment(TextAlignment.CENTER);

        // VBox to vertically align instructions
        VBox instructionsBox = new VBox(instructions);
        instructionsBox.setAlignment(Pos.TOP_CENTER);
        instructionsBox.setPadding(new Insets(50, 20, 0, 20)); // Top, Right, Bottom, Left padding

        // Go to Map Button
        Button goToMapButton = new Button("Go to the Map");
        goToMapButton.setOnAction(e -> goToMapTab(tabPane));
        goToMapButton.setId("general-button"); // Styles the button using CSS

        // Used to position button at the bottom of the tab
        Translate translateY = new Translate(0, -100);
        goToMapButton.getTransforms().add(translateY);

        // Overlay to place instructions and the "Go to Map" button on top of the image
        StackPane imageOverlay = new StackPane(welcomeImage, instructionsBox, goToMapButton);
        imageOverlay.setAlignment(instructionsBox, Pos.TOP_CENTER);
        imageOverlay.setAlignment(goToMapButton, Pos.BOTTOM_CENTER);

        // Image fills the entire tab
        welcomeImage.fitWidthProperty().bind(tabPane.widthProperty());
        welcomeImage.fitHeightProperty().bind(tabPane.heightProperty());

        this.setContent(imageOverlay); // Sets the content of the tab to the image overlay
    }

    /**
     * Navigates to the Map tab when the button is pressed
     *
     * @param tabPane The parent TabPane.
     */
    private void goToMapTab(TabPane tabPane) {
        Tab mapTab = findTab(tabPane, "Map");
        if (mapTab == null) {
            mapTab = new MapTab("Map");
            tabPane.getTabs().add(mapTab);
        }
        tabPane.getSelectionModel().select(mapTab);
    }

    /**
     * Finds a tab by its name.
     *
     * @param parent  The TabPane to search within.
     * @param tabName The name of the tab to find.
     * @return The Tab object if found, null otherwise.
     */
    private Tab findTab(TabPane parent, String tabName) {
        for (Tab tab : parent.getTabs()) {
            if (tab.getText().equals(tabName)) {
                return tab;
            }
        }
        return null;
    }

}