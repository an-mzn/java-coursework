import javafx.scene.control.TabPane;
import java.net.URL;

/**
 * Write a description of class TabStyler here.
 *
 * @author Ayan Mamun  
 * @version 1.0
 */

public class TabStyler {

    /**
     * Applies the header style to the given TabPane.
     *
     * @param tabPane The TabPane to style.
     */
    public static void applyHeaderStyle(TabPane tabPane) {
        //Load the css stylesheet
        URL cssURL = TabStyler.class.getResource("header_style.css");
        if (cssURL != null) {
            tabPane.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.out.println("CSS file not found: header_style.css");
        }
    }
}
