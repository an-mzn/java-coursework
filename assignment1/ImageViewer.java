/**
 * This class implements the logic of the image viewer, including 
 * displaying images, navigating through the album and applying effects
 * 
 * @author Michael Kölling and Ayan Mamun
 * @version 1.4[challenge 1, 2, 3 & extra + base tasks]
 */
public class ImageViewer
{    
    private ImageViewerGUI gui;     // the Graphical User Interface
    private Album album;            // album containing images
    private int currentIndex;   // index of current image displayed
    private int viewsIndex;     // index representing number of views [challenge 1]
    private int averageWidth;   // average width value of all images


    /**
     * Create an ImageViewer and display its GUI on screen.
     * Constructor of ImageViewer. 
     */
    public ImageViewer()
    {
        gui = new ImageViewerGUI(this);
        album = new Album("images");
        currentIndex = 0;   // 0 represents the first image in the album
        displayCurrentImage(); // as soon as ImageViewer is created, image is displayed
        viewsIndex = 1;     // initialise views index to 1 since image displayed on startup [challenge 1]
        averageImageWidth(); // computes the average image width at startup [challenge 2]
        maxImageSize(); // finds the maximum height and width of all images in album [challenge 3]
    }

    /**
     * Checks if there are images in the album. 
     * If there aren't, display shows "No images"
     * Set as boolean so that bugs can be detected in later methods via if statements
     */
    private boolean isAlbumEmpty() {
        if(album.numberOfImages() == 0) {
            gui.showStatus("No images");
            return true;
        }
        return false;
    }

    /**
     * Displaying [on the GUI] the current image from album
     * Shows image name at the top and its size in the status bar
     * Checks if album is empty beforehand
     */
    private void displayCurrentImage() {
        if (isAlbumEmpty()) { 
            return;
        }
        Image currentImage = album.getImage(currentIndex);
        gui.showImage(currentImage);
        gui.showName(currentImage.getName());
        gui.showStatus("Size: " + currentImage.getWidth() + " x " +currentImage.getHeight());
    }

    /**
     * GUI displays next image in album
     */
    public void nextImage()
    {
        if (isAlbumEmpty()) { 
            return;     // ends method early if album is empty
        }
        currentIndex = (++currentIndex) % (album.numberOfImages()); // wraps around to first image after last image
        viewsIndex++;   // index increases by 1 since it views another image
        displayCurrentImage();  // displays next image
    }

    /**
     * GUI displays previous image in album
     */
    public void previousImage()
    {
        if (isAlbumEmpty()) { 
            return;      // ends method early if album is empty
        }
        currentIndex = (--currentIndex + album.numberOfImages()) % (album.numberOfImages()); // wraps around to last image after first image
        viewsIndex++;   // index increases by 1 since it views another image
        displayCurrentImage();  // displays previous image
        
    }

    /**
     * GUI applies fish eye effect to current image
     */
    public void fishEye()
    {
        if (isAlbumEmpty()) { 
            gui.showStatus("No images to apply effect to ");
            return;
        }
        Image currentImage = album.getImage(currentIndex);
        currentImage.fishEye();          // applies effect
        gui.showImage(currentImage);     // shows modified/new image
        gui.showStatus("Fish eye effect applied to " + currentImage.getName());
    }
    
    /**
     * [extra challenge task]
     * Opens dialogue and prints statistics of number of images viewed and average image width in album
     */
    public void statistics() 
    {
        if (isAlbumEmpty()) { 
            return;
        }
    
        // store results with one method call
        int numberOfImagesViewed = getNumberOfImagesViewed();
        int averageWidth = averageImageWidth();

        // print stats
        System.out.println("Number of images viewed: " + numberOfImagesViewed);
        System.out.println("Average width of images in album: " + averageWidth);
    }

    /**
     * [challenge task 1]
     * Returns the total number of images viewed so far
     * This count increments each time the user navigates to a new image
     */
    public int getNumberOfImagesViewed() { 
        if (isAlbumEmpty()) { 
            return 0;
        }
        return viewsIndex; //returns the number of images viewed
    }
    
    /**
     * [challenge task 2]
     * Returns the average value of all the images' widths
     */
    public int averageImageWidth() { 
        if (isAlbumEmpty()) { 
            return 0;   // avoids errors diving by 0
        }
        int totalWidth = 0;
        for (int i = 0; i < album.numberOfImages(); i++) {
            totalWidth += album.getImage(i).getWidth(); // adding up the width of each image
        }
        averageWidth = totalWidth / album.numberOfImages(); // average calculation
        return averageWidth; //returns the average width
    }

    /**
     * [challenge task 3]
     * Calculating the maximum heights and widths of the image
     * -> will set the gui to be that size so the window size doesn't change as different images appear
     */
    private void maxImageSize() {
        int maxWidth = 0;
        int maxHeight = 0;
        
        // Goes through all images in album and iteratively changes maximum sizes
        for (int j = 0; j < album.numberOfImages(); j++) {
            Image instance = album.getImage(j);
            if (instance.getWidth() > maxWidth) {
                maxWidth = instance.getWidth();
            }
            if ( instance.getHeight() > maxHeight) {
                maxHeight = instance.getHeight();
            }
        }
        gui.setImageSize(maxWidth, maxHeight);  // sets maximum height and width of gui
    }
}
